package edu.coursemgr.service.interfaces;

import edu.coursemgr.model.Course;
import edu.coursemgr.pojo.CourseTaskDetail;

import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/16 0016 下午 10:43
 */
public interface CourseMgrService {

    List<Course> getTeacherCourseList(String tearcherNo);

    int saveCourse(String name, String credit, String description,
                       String userNo);

    List<Course> getStuCourseList(String stuNo);

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
}
