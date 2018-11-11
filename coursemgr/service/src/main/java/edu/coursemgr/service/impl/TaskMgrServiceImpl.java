package edu.coursemgr.service.impl;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.common.Constant;
import edu.coursemgr.dao.*;
import edu.coursemgr.freemarker.WordExportUtil;
import edu.coursemgr.model.*;
import edu.coursemgr.pojo.*;
import edu.coursemgr.service.interfaces.GradeMgrService;
import edu.coursemgr.service.interfaces.TaskMgrService;
import edu.coursemgr.utils.CollectionUtils;
import edu.coursemgr.utils.CommonUtils;
import edu.coursemgr.utils.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        CourseTasks tmpTask = taskDetail.getTask();
        if (CommonUtils.isEmpty(tmpTask.getName())) {
            throw new Exception(Constant.ExceptionMessage.TASK_NAME_EMPTY);
        }
        if (tmpTask.getStartTime() == null || tmpTask.getDeadline() == null) {
            throw new Exception(Constant.ExceptionMessage.TASK_DATE_EMPTY);
        }
        if (tmpTask.getStartTime().compareTo(tmpTask.getDeadline()) > 0) {
            throw new Exception(Constant.ExceptionMessage.TASK_DATE_EXCEPTION);
        }
        // 判断是否有重名的任务名称
        CourseTasks task = courseTasksMapper.selectByTaskName(taskDetail.getTask().getName());
        if (task != null) {
            throw new Exception(Constant.ExceptionMessage.TASK_NAME_UNIQUE);
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
        if (!task.getMarkType().isEmpty()) {
//            task.setMarkType(CommonEnum.GradeType.valueOf(task.getMarkType()).getText());
            task.setMarkType(task.getMarkType());
        }
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

        // 删除待办
        gradeRelateMapper.deleteByTaskId(Integer.valueOf(taskId));

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

        if (task.getMarkType().equals(CommonEnum.GradeType.SELF_EVA.getValue())) {
            User user = userMapper.selectBySerialNo(studentNo);
            resultMap.put("markUser", user);
        } else {
            GradeRelate relate = gradeRelateMapper.selectByStudent(params);
            if (relate != null) {
                User user = userMapper.selectBySerialNo(relate.getStudentNo());
                resultMap.put("markUser", user);
            }
        }
        if (studentTasks != null) {
            resultMap.put("studentTotalScore", studentTasks.getScore());
            resultMap.put("status", studentTasks.getStatus());
        }

        return resultMap;
    }

    @Override
    public boolean checkStuTaskFinished(String studentNo, String taskId) {
        Map params = new HashMap();
        params.put("studentNo", studentNo);
        params.put("taskId", taskId);
        StudentTasks task = studentTasksMapper.selectByStudent(params);
        if (task == null || task.getStatus().equals(CommonEnum.StudentTaskStatus.UNCOMMITTED.getValue())) {
            return false;
        }

        return true;
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

            temp = transferTemplateData(taskDetail, request.getServletContext().getRealPath("/"));
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

    private Map<String, Object> transferTemplateData(Map taskDetail, String rootPath) {
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

            temp.put("questionStemsList", transferImgContent(questionStems, rootPath));
            if (markModel.getQuestionType().equals(CommonEnum.QuestionType.JUDGE.getValue())) {
                List<Map<String, Object>> tempOptList = new ArrayList<>();
                Map<String, Object> tmpMap = new HashMap<>();
                tmpMap.put("itemList", transferImgContent("是", rootPath));
                tempOptList.add(tmpMap);
                tmpMap = new HashMap<>();
                tmpMap.put("itemList", transferImgContent("否", rootPath));
                tempOptList.add(tmpMap);

                temp.put("optionList", tempOptList);
            } else {
                temp.put("optionList", getOptionList(questionList.get(i).getOptionList(), rootPath));
            }
            temp.put("studentAnswerList", transferImgContent(questionList.get(i).getTaskQuestions().getAnswers(),
                    rootPath));
            temp.put("standardAnswerList", transferImgContent(questionList.get(i).getTaskQuestions().getStandardAnswer(),
                    rootPath));

            tempQuestionList.add(temp);
        }
        resultMap.put("questionList", tempQuestionList);
        resultMap.put("studentTotalScore", studentTotalScore);

        return resultMap;
    }

    private List<Map<String, Object>> getOptionList(List<QuestionOptions> optionList,
                                                    String rootPath) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (optionList == null || optionList.size() == 0) {
            return resultList;
        }
        String tmpOption = "";
        for (QuestionOptions option : optionList) {
            tmpOption = option.getOptionDes();
            if (!option.getOptionTag().isEmpty()) {
                tmpOption = String.format("%s、%s", option.getOptionTag(), tmpOption);
            }
            Map<String, Object> tmpMap = new HashMap<>();
            tmpMap.put("itemList", transferImgContent(tmpOption, rootPath));
            resultList.add(tmpMap);
        }
        return resultList;
    }

    private List<Map<String, Object>> transferImgContent(String content, String rootPath) {
        content = content.replaceAll("<p>", "");
        content = content.replaceAll("</p>", "");
        List<Map<String, Object>> stemsList = new ArrayList<>();
        Map<String, Object> stems = new HashMap<>();
        if (!content.contains("img")) {
            stems.put("type", "text");
            stems.put("content", content);
            stemsList.add(stems);
            return stemsList;
        }
        String regx = "src=\"(.*?)\"";
        while (content.indexOf("<img") >= 0) {
            String img = content.substring(content.indexOf("<img"), content.indexOf("/>") + 2);
            if (content.indexOf("<img") > 0) {
                String partText = content.substring(0, content.indexOf("<img"));
                content = content.substring(content.indexOf("<img"));
                stems = new HashMap<>();
                stems.put("type", "text");
                stems.put("content", partText);
                stemsList.add(stems);
            }
            content = content.replace(img, "");

            Pattern pattern = Pattern.compile(regx);// 匹配的模式
            Matcher m = pattern.matcher(img);
            String src = "";
            if (m.find()){
                src = m.group(1);
            }
            if (src.isEmpty()) {
                continue;
            }
            String imgPath = src.substring(src.indexOf("upload\\"));
            imgPath = String.format("%s%s", rootPath, imgPath);
            stems = new HashMap<>();
            stems.put("type", "img");
            stems.put("content", CommonUtils.imgToBase64(imgPath));
            stemsList.add(stems);
        }
        if (!content.isEmpty()) {
            stems = new HashMap<>();
            stems.put("type", "text");
            stems.put("content", content);
            stemsList.add(stems);
        }

        return stemsList;
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
