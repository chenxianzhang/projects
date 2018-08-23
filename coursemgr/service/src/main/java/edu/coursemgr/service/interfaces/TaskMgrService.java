package edu.coursemgr.service.interfaces;

import edu.coursemgr.model.CourseTasks;
import edu.coursemgr.pojo.CourseTaskDetail;
import edu.coursemgr.pojo.StudentTaskDetail;

import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/24 0024 上午 12:13
 */
public interface TaskMgrService {

    /**
     * 保存task详细信息
     * @param taskDetail
     * @return
     */
    int saveTask(CourseTaskDetail taskDetail) throws Exception;

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

}
