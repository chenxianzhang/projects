package edu.coursemgr.service.interfaces;

import edu.coursemgr.model.CourseTasks;
import edu.coursemgr.model.TaskQuestions;
import edu.coursemgr.pojo.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by chenxianzhang on 2018/8/24 0024 上午 12:13
 */
public interface TaskMgrService {

    /**
     * 保存task详细信息
     * @param taskDetail
     * @return
     */
    Map<String, Object> saveTask(CourseTaskDetail taskDetail) throws Exception;

    /**
     * 根据任务id获取当前任务详细信息
     * @param taskId
     * @return
     */
    CourseTaskDetail getTaskDetailByTaskId(Integer taskId) throws Exception;

    /**
     * 根据taskId获取任务信息，不包含试题信息
     * @param taskId
     * @return
     */
    CourseTasks getCourseTaskById(String taskId);

    /**
     * 根据课程id获取当前课程下所有任务信息
     * @param courseId
     * @return
     */
    List<CourseTasks> getCourseTasksByCourseId(String courseId);

    /**
     * 根据courseId, taskId获取指定课程下的任务学生完成情况
     * @param taskId
     * @return
     */
    List<StudentTaskDetail> getStudentTaskSituation(String taskId, String courseId);


    /**
     * 根据课程id获取课程任务详情
     * @param courseId
     * @return
     */
    List<CourseTaskSituation> getCourseTaskSituation(String courseId);

    /**
     * 获取我的任务情况
     * @param courseId
     * @param studentNo
     * @return
     */
    List<StudentTaskSituation> getMyTaskSituation(String courseId, String studentNo);

    /**
     * 提交任务试卷
     * @param stuPaperAnswer
     * @return
     */
    boolean submitTaskPaper(StudentPaperAnswer stuPaperAnswer);

    /**
     * 删除任务
     * @param courseId
     * @param taskId
     * @return
     */
    int deleteTask(String courseId, String taskId);

    /**
     * 获取学员任务详情
     * @param taskId
     * @param studentNo
     * @return
     */
    CourseTaskDetail getStuTaskDetail(String taskId, String studentNo);

    /**
     * 导出过程打包文件
     * @param response
     * @param courseId
     */
    void exportCourseProcess(HttpServletResponse response, String courseId);

}
