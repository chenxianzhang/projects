package edu.coursemgr.service.impl;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.common.Constant;
import edu.coursemgr.dao.*;
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

    @Override
    public Map<String, Object>  saveTask(CourseTaskDetail taskDetail) throws Exception {
        boolean illegal = taskDetail.getTask() == null || taskDetail.getQuestionList() == null;
        if (illegal) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EXCEPTION);
        }
        // 保存task任务信息，并返回任务id
        Integer taskId = taskDetail.getTask().getId();
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
    public List<CourseTaskSituation> getCourseTaskSituation(String courseId) {
        return courseTasksMapper.getCourseTaskSituation(Integer.valueOf(courseId));
    }

    @Override
    public List<StudentTaskSituation> getMyTaskSituation(String courseId, String studentNo) {

        Map<String ,Object> params = new HashMap<>(2);
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

        List<StudentPaper> studentPapers = CollectionUtils.arrayListCast(
                stuPaperAnswer.getQuestionList(), question -> {
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
                    }

                    return paper;
                });
        if (studentPapers == null) {
            return true;
        }

        insertBatch(studentPapers);

        // 插入学生任务信息
        StudentTasks studentTasks = new StudentTasks();
        studentTasks.setStatus(CommonEnum.StudentTaskStatus.TO_REVIEW.getValue());
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
    public CourseTaskDetail getStuTaskDetail(String taskId, String studentNo) {

        CourseTasks task = courseTasksMapper.selectByTaskId(Integer.valueOf(taskId));
        Map params = new HashMap();
        params.put("taskId", taskId);
        params.put("studentNo", studentNo);
        StudentTasks studentTasks = studentTasksMapper.selectByStudent(params);

        List<TaskQuestions> taskQuestions = taskQuestionsMapper.selectStuTaskPaper(params);
        List<TaskPaper> taskPaperList = CollectionUtils.arrayListCast(taskQuestions,
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
        taskDetail.setStatus(CommonEnum.StudentTaskStatus.UNCOMMITTED.getValue());
        if (studentTasks != null) {
            taskDetail.setStudentTotalScore(studentTasks.getScore());
            taskDetail.setStatus(studentTasks.getStatus());
        }

        return taskDetail;
    }

    @Override
    public void exportCourseProcess(HttpServletResponse response, String courseId,
                                    String realPath) throws Exception {
        List<CourseTasks> tasksList = getCourseTasksByCourseId(courseId);
        if (tasksList == null) {
            return;
        }
        // 获取所有学员信息
        List<User> userList = userMapper.selectSomeByCourseId(Integer.valueOf(courseId));
        Course course = courseMapper.selectById(Integer.valueOf(courseId));
        String unpackDir = CommonUtils.combinePath(realPath, Constant.Common.DOWNLOAD_TEMP_DIR,
                course.getName());
        CommonUtils.createDir(unpackDir);
        for (CourseTasks task : tasksList) {
            generateWord(task.getId(), userList, unpackDir);
        }

        // 压缩
        String zipName = String.format("所有学生%s任务过程文件包.zip", course.getName());
//        String zipName = "course.zip";
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition",
                "attachment; filename=".concat(
                        String.valueOf(URLEncoder.encode(zipName, "UTF-8"))));
        ZipUtils.toZip(unpackDir, response.getOutputStream(),true);

        // 删除当前目录及文件
        CommonUtils.deleteDir(unpackDir);
    }

    @Override
    public void exportStuCourseProcess(HttpServletResponse response, String courseId,
                                       String realPath, String studentNo) throws Exception {
        List<CourseTasks> tasksList = getCourseTasksByCourseId(courseId);
        if (tasksList == null) {
            return;
        }
        User user = userMapper.selectBySerialNo(studentNo);
        Course course = courseMapper.selectById(Integer.valueOf(courseId));
        String unpackDir = CommonUtils.combinePath(realPath, Constant.Common.DOWNLOAD_TEMP_DIR,
                course.getName());
        CommonUtils.createDir(unpackDir);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        for (CourseTasks task : tasksList) {
            generateWord(task.getId(), userList, unpackDir);
        }

        // 压缩
        String zipName = String.format("%s(%s)%s任务过程文件包.zip", user.getName(),
                user.getSerialNo(), course.getName());
//        String zipName = "course.zip";
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition",
                "attachment; filename=".concat(
                        String.valueOf(URLEncoder.encode(zipName, "UTF-8"))));
        ZipUtils.toZip(unpackDir, response.getOutputStream(),true);

        // 删除当前目录及文件
        CommonUtils.deleteDir(unpackDir);
    }

    private void generateWord(Integer taskId, List<User> studentList, String unpackDir) {
        if (studentList == null) {
            return;
        }
        studentList.forEach(student -> {
            CourseTaskDetail taskDetail = getStuTaskDetail(taskId.toString(),
                    student.getSerialNo());
            if (!taskDetail.getStatus().equals(CommonEnum.StudentTaskStatus.FINISHED.getValue())) {
                return;
            }
            String html = transfer2Html(taskDetail);

            String fileName = String.format("%s/%s(%s%s).doc", unpackDir, taskDetail.getTask().getName(),
                    student.getName(), student.getSerialNo());
            new JsoupWordOper().html2Word(html, unpackDir, fileName);
        });
    }

    private String transfer2Html(CourseTaskDetail taskDetail) {
        String html = String.format("<p style=\"text-align:center;font-weight: bolder;font-size:20px;\">%s</p>",
                taskDetail.getTask().getName());
        for (TaskPaper question : taskDetail.getQuestionList()) {
            html += "<div>";
            html += String.format("<p style=\"font-weight: bolder;\">%s</p>",
                    question.getTaskQuestions().getStems());
            Integer index = 1;

            html += "<div style=\"margin-left: 20px;font-size:14px;\">";
            for (QuestionOptions option : question.getOptionList()) {
                html += String.format("<span style=\"margin-right:30px\">%s、%s</span>", index.toString(),
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

        taskQuestions.forEach(taskQuestion ->{
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

}
