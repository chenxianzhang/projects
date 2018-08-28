package edu.coursemgr.service.impl;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.common.Constant;
import edu.coursemgr.dao.CourseTasksMapper;
import edu.coursemgr.dao.StudentPaperMapper;
import edu.coursemgr.dao.StudentTasksMapper;
import edu.coursemgr.dao.TaskQuestionsMapper;
import edu.coursemgr.model.CourseTasks;
import edu.coursemgr.model.StudentPaper;
import edu.coursemgr.model.TaskQuestions;
import edu.coursemgr.pojo.*;
import edu.coursemgr.service.interfaces.TaskMgrService;
import edu.coursemgr.utils.CollectionUtils;
import edu.coursemgr.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenxianzhang on 2018/8/24 0024 上午 12:14
 */
@Service
public class TaskMgrServiceImpl implements TaskMgrService {

    @Autowired
    private CourseTasksMapper courseTasksMapper;

    @Autowired
    private TaskQuestionsMapper taskQuestionsMapper;

    @Autowired
    private StudentTasksMapper studentTasksMapper;

    @Autowired
    private StudentPaperMapper studentPaperMapper;

    @Override
    public int saveTask(CourseTaskDetail taskDetail) throws Exception {
        boolean illegal = taskDetail.getTask() == null || taskDetail.getQuestionList() == null;
        if (illegal) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EXCEPTION);
        }
        // 保存task任务信息，并返回任务id
        int recordCnt = courseTasksMapper.insert(taskDetail.getTask());

        illegal = recordCnt == 0 || taskDetail.getTask().getId() == null;
        if (illegal) {
            throw new Exception(Constant.ExceptionMessage.DATA_SAVE_EXCEPTION);
        }

        taskDetail.getQuestionList().forEach(question -> {
            question.setTaskId(taskDetail.getTask().getId());
        });
        insertBatch(taskDetail.getQuestionList());
        return 1;
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

        CourseTaskDetail taskDetail = new CourseTaskDetail();
        taskDetail.setTask(task);
        taskDetail.setQuestionList(questionsList);
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
                    item.setStatus(CommonEnum.StudentTaskStatus.UNCOMMITTED.getName());
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
        return studentTasksMapper.getSelfTaskSituation(params);
    }

    @Override
    public boolean submitTaskPaper(StudentPaperAnswer stuPaperAnswer) {

        List<StudentPaper> studentPapers = CollectionUtils.arrayListCast(
                stuPaperAnswer.getQuestionList(), question -> {
                    StudentPaper paper = new StudentPaper();
                    paper.setStudentNo(stuPaperAnswer.getStudentNo());
                    paper.setAnswers(question.getAnswers());
                    paper.setQuestionId(question.getId());
                    return paper;
                });
        if (studentPapers == null) {
            return true;
        }
        if (studentPaperMapper.insertBatch(studentPapers) == 0 ) {
            return false;
        }
        return true;
    }

    private void insertBatch(List<TaskQuestions> taskQuestions) {
        if (taskQuestions == null) {
            return;
        }

        taskQuestions.forEach(taskQuestion -> taskQuestionsMapper.insert(taskQuestion));
    }
}
