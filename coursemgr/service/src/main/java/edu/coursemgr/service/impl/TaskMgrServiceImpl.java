package edu.coursemgr.service.impl;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.common.Constant;
import edu.coursemgr.dao.*;
import edu.coursemgr.freemarker.WordExportUtil;
import edu.coursemgr.freemarker.WordGenerator;
import edu.coursemgr.jsoup.JsoupWordOper;
import edu.coursemgr.model.*;
import edu.coursemgr.pojo.*;
import edu.coursemgr.service.interfaces.GradeMgrService;
import edu.coursemgr.service.interfaces.GroupMgrService;
import edu.coursemgr.service.interfaces.TaskMgrService;
import edu.coursemgr.utils.CollectionUtils;
import edu.coursemgr.utils.CommonUtils;
import edu.coursemgr.utils.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by chenxianzhang on 2018/8/24 0024 上午 12:14
 */
@Service
@Transactional
public class TaskMgrServiceImpl implements TaskMgrService {

    @Autowired
    private CourseTasksMapper courseTasksMapper;

    @Autowired
    private TaskQuestionsMapper taskQuestionsMapper;

    @Autowired
    private StudentTasksMapper studentTasksMapper;

    @Autowired
    private StudentPaperMapper studentPaperMapper;

    @Autowired
    private GradeMgrService gradeMgrService;

    @Autowired
    private QuestionOptionsMapper questionOptionsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private GradeRelateMapper gradeRelateMapper;

    @Override
    public Map<String, Object> saveTask(CourseTaskDetail taskDetail) throws Exception {
        boolean illegal = taskDetail.getTask() == null || taskDetail.getQuestionList() == null;
        if (illegal) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EXCEPTION);
        }
        // 保存task任务信息，并返回任务id
        Integer taskId = taskDetail.getTask().getId();
        if (!hasSubject(taskDetail)) {
            taskDetail.getTask().setMarkType(CommonEnum.GradeType.AUTO_EVA.getValue());
        }
        // 如果不存在则插入一条新的记录
        if (taskId == null) {
            courseTasksMapper.insert(taskDetail.getTask());
            taskId = taskDetail.getTask().getId();
        } else {
            courseTasksMapper.updateByIdSelective(taskDetail.getTask());
        }

        // 根据所选择的主题的评分规则，更新各组成员的评分对象
        illegal = gradeMgrService.updateGroupMemberGradeObj(taskDetail.getTask().getCourseId(),
                taskDetail.getTask().getMarkType(), taskDetail.getTask());
        if (!illegal) {
            throw new Exception(Constant.ExceptionMessage.DATA_SAVE_EXCEPTION);
        }


        illegal = taskDetail.getTask().getId() == null;
        if (illegal) {
            throw new Exception(Constant.ExceptionMessage.DATA_SAVE_EXCEPTION);
        }

        taskDetail.getQuestionList().forEach(question -> {
            if (question.getTaskQuestions() != null) {
                question.getTaskQuestions().setTaskId(taskDetail.getTask().getId());
            }
        });
        updateBatch(taskDetail.getQuestionList());

        Map<String, Object> resultMap = new HashMap<>(1);
        resultMap.put("taskId", taskId);
        return resultMap;
    }

    @Override
    public CourseTaskDetail getTaskDetailByTaskId(Integer taskId) throws Exception {

        // 获取任务基本信息
        CourseTasks task = courseTasksMapper.selectByTaskId(taskId);
        if (task == null) {
            throw new Exception(Constant.ExceptionMessage.DATA_QUERY_EXCEPTION);
        }
        // 获取任务下的所有试题信息
        List<TaskQuestions> questionsList = taskQuestionsMapper.selectSomeByTaskId(taskId);

        if (questionsList == null) {
            throw new Exception(Constant.ExceptionMessage.DATA_QUERY_EXCEPTION);
        }

        List<TaskPaper> taskPaperList = CollectionUtils.arrayListCast(questionsList,
                question -> {
                    List<QuestionOptions> optionsList = questionOptionsMapper.selectByQuestionId(
                            question.getId());
                    TaskPaper paper = new TaskPaper();
                    paper.setTaskQuestions(question);
                    paper.setOptionList(optionsList);

                    return paper;
                });

        CourseTaskDetail taskDetail = new CourseTaskDetail();
        taskDetail.setTask(task);
        taskDetail.setQuestionList(taskPaperList);
        return taskDetail;
    }

    @Override
    public CourseTasks getCourseTaskById(String taskId) {
        return courseTasksMapper.selectByTaskId(Integer.valueOf(taskId));
    }

    @Override
    public List<CourseTasks> getCourseTasksByCourseId(String courseId) {
        return courseTasksMapper.selectSomeByCourseId(Integer.valueOf(courseId));
    }

    @Override
    public List<StudentTaskDetail> getStudentTaskSituation(String taskId, String courseId) {
        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("taskId", taskId);
        paramMap.put("courseId", Integer.valueOf(courseId));
        List<StudentTaskDetail> studentTaskDetails = studentTasksMapper.getStudentTaskSituation(paramMap);

        if (studentTaskDetails != null) {
            studentTaskDetails.forEach(item -> {
                if (CommonUtils.isEmpty(item.getStatus())) {
                    item.setStatus(CommonEnum.StudentTaskStatus.UNCOMMITTED.getValue());
                }
            });
        }

        return studentTaskDetails;
    }

    @Override
    public PageModel getTaskSituationList(String taskId, String courseId, String pageSize,
                                          String currPage) {

        int totalCount = userMapper.selectTotalCntByCourseId(Integer.valueOf(courseId));

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("courseId", courseId);
        paramMap.put("pageSize", pageSize);
        int offset = (Integer.valueOf(currPage) - 1) * Integer.valueOf(pageSize);
        paramMap.put("currSize", offset);
        // 获取课程下所有学员
        List<User> userList = userMapper.selectSomeByPage(paramMap);
        final String tmpTaskId = taskId;
        List<StudentTaskDetail> dataList = CollectionUtils.arrayListCast(userList,
                user -> {
                    StudentTaskDetail detail = new StudentTaskDetail();
                    detail.setStudentNo(user.getSerialNo());
                    detail.setStudentName(user.getName());

                    // 获取该任务的状态，及该任务是否包含主观题和评分类型
                    Map params = new HashMap<>();
                    params.put("taskId", tmpTaskId);
                    params.put("studentNo", user.getSerialNo());
                    TaskStatusMarkModel model = courseTasksMapper.selectTaskStatusMark(params);
                    if (CommonUtils.isEmpty(model.getStatus())) {
                        detail.setStatus(CommonEnum.StudentTaskStatus.UNCOMMITTED.getValue());
                        detail.setStatusText(CommonEnum.StudentTaskStatus.UNCOMMITTED.getName());
                    } else {
                        CommonEnum.StudentTaskStatus taskStatus = CommonEnum.StudentTaskStatus.valueOf(model.getStatus());
                        detail.setStatus(taskStatus.getValue());
                        detail.setStatusText(taskStatus.getName());
                    }
                    if (model.getSubjectCnt() == null || model.getSubjectCnt() == 0) {
                        detail.setReviewer("系统");
                    } else {
                        if (model.getMarkType().equals(CommonEnum.GradeType.SELF_EVA.getValue())) {
                            detail.setReviewer(user.getName());
                        } else {
                            GradeRelate relate = gradeRelateMapper.selectByStudent(params);
                            if (relate != null) {
                                User userTmp = userMapper.selectBySerialNo(relate.getStudentNo());
                                detail.setReviewer(userTmp.getName());
                            }
                        }
                    }
                    return detail;
                });

        PageModel pageModel = new PageModel();
        pageModel.setTotalCount(totalCount);
        pageModel.setPageData(dataList);
        return pageModel;
    }

    @Override
    public List<CourseTaskSituation> getCourseTaskSituation(String courseId) {
        List<CourseTaskSituation> situationList =
                courseTasksMapper.getCourseTaskSituation(Integer.valueOf(courseId));
        for (CourseTaskSituation situation : situationList) {
            int cnt = studentTasksMapper.selectTaskFinshedCnt(situation.getId().toString());
            situation.setFinishPersonCnt(cnt);
        }
        return situationList;
    }

    @Override
    public List<StudentTaskSituation> getMyTaskSituation(String courseId, String studentNo) {

        Map<String, Object> params = new HashMap<>(2);
        params.put("courseId", courseId);
        params.put("studentNo", studentNo);

        // 根据courseId获取所有taskId
        List<CourseTasks> tasksList = courseTasksMapper.selectSomeByCourseId(Integer.valueOf(courseId));

        if (tasksList == null) {
            return null;
        }

        List<StudentTaskSituation> resultList = CollectionUtils.arrayListCast(tasksList,
                task -> {

                    StudentTaskSituation situation = new StudentTaskSituation();
                    if (task.getDeadline() != null) {
                        situation.setDeadline(task.getDeadline().toString());
                    }
                    if (task.getPublishTime() != null) {
                        situation.setPublishTime(task.getPublishTime().toString());
                    }
                    situation.setCanAnswer(false);
                    if (task.getStartTime() != null) {
                        situation.setStartTime(task.getStartTime().toString());
                        situation.setCanAnswer(task.getStartTime().compareTo(new Date()) < 0
                                && task.getDeadline().compareTo(new Date()) >= 0);
                    }
                    situation.setTaskId(task.getId());
                    situation.setTaskName(task.getName());

                    // 根据taskId 和 studentNo查询学生的任务状态
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("taskId", task.getId());
                    paramMap.put("studentNo", studentNo);

                    StudentTasks studentTasks = studentTasksMapper.selectByStudent(paramMap);

                    if (studentTasks == null) {
                        situation.setFinishStatus(CommonEnum.StudentTaskStatus.UNCOMMITTED.getValue());
                        return situation;
                    }
                    situation.setFinishStatus(studentTasks.getStatus());

                    return situation;
                });

//        return studentTasksMapper.getSelfTaskSituation(params);
        return resultList;
    }

    @Override
    public boolean submitTaskPaper(StudentPaperAnswer stuPaperAnswer) {

        CourseTasks task = courseTasksMapper.selectByTaskId(
                Integer.valueOf(stuPaperAnswer.getTaskId()));

        Float totalScore = 0f;
        List<StudentPaper> studentPapers = new ArrayList<>();
        for (StudentQuestion question : stuPaperAnswer.getQuestionList()) {
            StudentPaper paper = new StudentPaper();
            paper.setTaskId(Integer.valueOf(stuPaperAnswer.getTaskId()));
            paper.setStudentNo(stuPaperAnswer.getStudentNo());
            paper.setAnswers(question.getAnswers());
            paper.setQuestionId(question.getQuestionId());
            paper.setQuestionType(question.getQuestionType());

            // 计算得分
            paper.setScore(0f);
            TaskQuestions taskQuestions = taskQuestionsMapper.selectByPrimaryKey(
                    question.getQuestionId());
            if (isRight(taskQuestions.getAnswers(), question.getAnswers())) {
                paper.setScore(taskQuestions.getScore());
                totalScore += paper.getScore();
            }
            studentPapers.add(paper);
        }

        insertBatch(studentPapers);

        // 插入学生任务信息
        StudentTasks studentTasks = new StudentTasks();
        studentTasks.setStatus(CommonEnum.StudentTaskStatus.TO_REVIEW.getValue());
        if (task.getMarkType().equals(CommonEnum.GradeType.AUTO_EVA.getValue())) {
            studentTasks.setStatus(CommonEnum.StudentTaskStatus.FINISHED.getValue());
            studentTasks.setScore(totalScore);
        }
        studentTasks.setStudentNo(stuPaperAnswer.getStudentNo());
        studentTasks.setSubmitTime(new Date());
        studentTasks.setTaskId(stuPaperAnswer.getTaskId());
        studentTasksMapper.insertSelective(studentTasks);
        return true;
    }

    @Override
    public int deleteTask(String courseId, String taskId) {
        // 先删除问题options
        questionOptionsMapper.deleteByTaskId(Integer.valueOf(taskId));
        // 在删除试题
        taskQuestionsMapper.deleteByTaskId(Integer.valueOf(taskId));
        // 其次学生试题
        studentPaperMapper.deleteByTaskId(Integer.valueOf(taskId));
        // 再次学生任务
        studentTasksMapper.deleteByTaskId(Integer.valueOf(taskId));

        // 最后任务
        return courseTasksMapper.deleteTask(Integer.valueOf(taskId));
    }

    @Override
    public int deleteQuestions(String questionIds) {
        return taskQuestionsMapper.deleteByIds(questionIds);
    }

    @Override
    public Map getStuTaskDetail(String taskId, String studentNo) {

        CourseTasks task = courseTasksMapper.selectByTaskId(Integer.valueOf(taskId));
        Map params = new HashMap();
        params.put("taskId", taskId);
        params.put("studentNo", studentNo);
        StudentTasks studentTasks = studentTasksMapper.selectByStudent(params);

        List<SubjectMarkModel> taskQuestions = taskQuestionsMapper.selectStuTaskPaper(params);
        List<TaskMarkPaper> taskPaperList = CollectionUtils.arrayListCast(taskQuestions,
                question -> {
                    List<QuestionOptions> optionsList = questionOptionsMapper.selectByQuestionId(
                            question.getId());
                    TaskMarkPaper paper = new TaskMarkPaper();
                    paper.setTaskQuestions(question);
                    paper.setOptionList(optionsList);

                    return paper;
                });

        Map resultMap = new HashMap();
        resultMap.put("task", task);
        resultMap.put("questionList", taskPaperList);
        resultMap.put("status", CommonEnum.StudentTaskStatus.UNCOMMITTED.getValue());
        GradeRelate relate = gradeRelateMapper.selectByStudent(params);
        if (relate != null) {
            User user = userMapper.selectBySerialNo(relate.getStudentNo());
            resultMap.put("markUser", user);
        }
        if (studentTasks != null) {
            resultMap.put("studentTotalScore", studentTasks.getScore());
            resultMap.put("status", studentTasks.getStatus());
        }

        return resultMap;
    }

    @Override
    public void exportCourseProcess(HttpServletResponse response, HttpServletRequest request, String courseId) throws Exception {
        List<CourseTasks> tasksList = getCourseTasksByCourseId(courseId);
        if (tasksList == null) {
            return;
        }
        // 获取所有学员信息
        List<User> userList = userMapper.selectSomeByCourseId(Integer.valueOf(courseId));
        Course course = courseMapper.selectById(Integer.valueOf(courseId));
        String unpackDir = CommonUtils.combinePath(request.getRealPath("/"), Constant.Common.DOWNLOAD_TEMP_DIR,
                course.getName());
        CommonUtils.createDir(unpackDir);
//        for (CourseTasks task : tasksList) {
//            generateWord(task.getId(), userList, unpackDir);
//        }

        for (User user : userList) {
            generateWord(user, tasksList, unpackDir, request);
        }

        // 压缩
        String zipName = String.format("所有学生%s任务过程文件包.zip", course.getName());
//        String zipName = "course.zip";
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition",
                "attachment; filename=".concat(
                        String.valueOf(URLEncoder.encode(zipName, "UTF-8"))));
        ZipUtils.toZip(unpackDir, response.getOutputStream(), true);

        // 删除当前目录及文件
        CommonUtils.deleteDir(unpackDir);
    }

    @Override
    public void exportStuCourseProcess(HttpServletResponse response,HttpServletRequest request, String courseId, String studentNo) throws Exception {
        List<CourseTasks> tasksList = getCourseTasksByCourseId(courseId);
        if (tasksList == null) {
            return;
        }
        User user = userMapper.selectBySerialNo(studentNo);
        Course course = courseMapper.selectById(Integer.valueOf(courseId));
        String unpackDir = CommonUtils.combinePath(request.getRealPath("/"), Constant.Common.DOWNLOAD_TEMP_DIR,
                course.getName());
        CommonUtils.createDir(unpackDir);
//        List<User> userList = new ArrayList<>();
//        userList.add(user);
//        for (CourseTasks task : tasksList) {
//            generateWord(task.getId(), userList, unpackDir);
//        }

        generateWord(user, tasksList, unpackDir, request);

        // 压缩
        String zipName = String.format("%s（%s）%s任务过程文件包.zip", user.getName(),
                user.getSerialNo(), course.getName());
//        String zipName = "course.zip";
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition",
                "attachment; filename=".concat(
                        String.valueOf(URLEncoder.encode(zipName, "UTF-8"))));
        ZipUtils.toZip(unpackDir, response.getOutputStream(), true);

        // 删除当前目录及文件
        CommonUtils.deleteDir(unpackDir);
    }


    private void generateWord(User student, List<CourseTasks> taskList,
                              String unpackDir, HttpServletRequest request) {
//        String html = "";
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> temp = null;
        for (CourseTasks task : taskList) {
            Map taskDetail = getStuTaskDetail(task.getId().toString(),
                    student.getSerialNo());
            String status = taskDetail.get("status").toString();
            if (!status.equals(CommonEnum.StudentTaskStatus.FINISHED.getValue())) {
                return;
            }
//            html += transfer2Html(taskDetail);
//            if (html == null) {
//                continue;
//            }
//            html += "<br/>";

            temp = transferTemplateData(taskDetail);
            if (temp == null) {
                continue;
            }
            resultList.add(temp);
        }

        Map<String, Object> root = new HashMap<>();
        root.put("taskList", resultList);
//        if (html == null) {
//            return;
//        }
        String fileName = String.format("%s(%s)", student.getName(), student.getSerialNo());
//        String fileName = String.format("%s/%s(%s).doc", unpackDir, student.getName(), student.getSerialNo());
//        new JsoupWordOper().html2Word(html, unpackDir, fileName);

        // 生成word文档
        WordExportUtil.generatorWord(request, WordExportUtil.WORD_2003, fileName, "wordTemplate.ftl",
                unpackDir, root);
    }

    private Map<String, Object> transferTemplateData(Map taskDetail) {
        CourseTasks task = (CourseTasks) taskDetail.get("task");
        List<TaskMarkPaper> questionList = (List<TaskMarkPaper>) taskDetail.get("questionList");
        Float studentTotalScore = (Float) taskDetail.get("studentTotalScore");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("taskName", task.getName());
        resultMap.put("taskTotalScore", task.getTotalScore());
        List<Map<String, Object>> tempQuestionList = new ArrayList<>();
        for (int i = 0; i < questionList.size(); i++) {
            Map<String, Object> temp = new HashMap<>();
            // 题干
            String questionNo = "" + (i + 1);
            SubjectMarkModel markModel = questionList.get(i).getTaskQuestions();
            String questionStems = String.format("%s、%s   (分数：%s)", questionNo, markModel.getStems(),
                    markModel.getScore().toString());

            temp.put("questionStems", questionStems);
            if (markModel.getQuestionType().equals(CommonEnum.QuestionType.JUDGE.getValue())) {
                List<QuestionOptions> judgeOptions = new ArrayList<>();
                QuestionOptions option = new QuestionOptions();
                option.setOptionDes("是");
                judgeOptions.add(option);

                option = new QuestionOptions();
                option.setOptionDes("否");
                judgeOptions.add(option);
                temp.put("optionList", judgeOptions);
            } else {
                temp.put("optionList", questionList.get(i).getOptionList());
            }
            temp.put("studentAnswer", questionList.get(i).getTaskQuestions().getAnswers());
            temp.put("standardAnswer", questionList.get(i).getTaskQuestions().getStandardAnswer());

            tempQuestionList.add(temp);
        }
        resultMap.put("questionList", tempQuestionList);
        resultMap.put("studentTotalScore", studentTotalScore);

        return resultMap;
    }

    private void generateWord(Integer taskId, List<User> studentList, String unpackDir) {
        if (studentList == null) {
            return;
        }
        studentList.forEach(student -> {
            Map taskDetail = getStuTaskDetail(taskId.toString(),
                    student.getSerialNo());
            if (!taskDetail.get("status").toString().equals(CommonEnum.StudentTaskStatus.FINISHED.getValue())) {
                return;
            }
            String html = transfer2Html(taskDetail);
            CourseTasks task = (CourseTasks) taskDetail.get("task");
            String fileName = String.format("%s/%s(%s%s).doc", unpackDir, task.getName(),
                    student.getName(), student.getSerialNo());
            new JsoupWordOper().html2Word(html, unpackDir, fileName);
        });
    }

    private String transfer2Html(Map taskDetail) {
        CourseTasks task = (CourseTasks) taskDetail.get("task");
        List<TaskMarkPaper> questionList = (List<TaskMarkPaper>) taskDetail.get("questionList");
        String html = String.format("<p style=\"text-align:center;font-weight: bolder;font-size:20px;\">%s</p>",
                task.getName());
        for (TaskMarkPaper question : questionList) {
            html += "<div>";
            html += String.format("<p style=\"font-weight: bolder;\">%s</p>",
                    question.getTaskQuestions().getStems());
            Integer index = 1;

            html += "<div style=\"margin-left: 20px;font-size:14px;\">";
            for (QuestionOptions option : question.getOptionList()) {
                html += String.format("<span style=\"margin-right:30px\">%s、%s </span>", index.toString(),
                        option.getOptionDes());
                index++;
            }
            html += "</div>";

            html += "<div style=\"margin-top: 10px;font-size:14px;color:red\">";
            html += String.format("<span>所选答案：%s</span>", question.getTaskQuestions().getAnswers());
            html += "</div>";

            html += "</div>";
        }
        return html;
    }


    private boolean isRight(String standardAnswer, String answer) {
        List<String> answers = Arrays.asList(answer.split(","));
        boolean right = false;
        for (String aws : answers) {
            if (!standardAnswer.contains(aws)) {
                right = false;
                break;
            }
            right = true;
        }
        return right;
    }

    private void insertBatch(List<StudentPaper> studentPapers) {
        if (studentPapers == null) {
            return;
        }
        studentPapers.forEach(paper -> {
            studentPaperMapper.insert(paper);
        });
    }

    private void updateBatch(List<TaskPaper> taskQuestions) {
        if (taskQuestions == null) {
            return;
        }

        taskQuestions.forEach(taskQuestion -> {
            TaskQuestions question = taskQuestion.getTaskQuestions();
            if (question != null) {
                if (question.getId() == null) {
                    taskQuestionsMapper.insert(question);
                } else {
                    taskQuestionsMapper.updateByIdSelective(question);
                }

                // 保存或更新题目选项信息
                updateBatch(taskQuestion.getOptionList(), question.getId());
            }

        });
    }

    private void updateBatch(List<QuestionOptions> optionsList, Long questionId) {
        if (optionsList == null) {
            return;
        }
        optionsList.forEach(option -> {
            option.setQuestionId(questionId);
            if (option.getId() == null) {
                questionOptionsMapper.insertSelective(option);
            } else {
                questionOptionsMapper.updateSelective(option);
            }
        });

    }

    private boolean hasSubject(CourseTaskDetail taskDetail) {
        if (taskDetail == null || taskDetail.getQuestionList() == null) {
            return false;
        }
        boolean flag = false;
        for (TaskPaper paper : taskDetail.getQuestionList()) {
            if (paper.getTaskQuestions().getQuestionType().equals(
                    CommonEnum.QuestionType.SUBJECTIVE_ITEM.getValue())) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
