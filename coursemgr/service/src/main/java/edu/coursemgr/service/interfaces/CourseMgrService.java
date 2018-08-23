package edu.coursemgr.service.interfaces;

import edu.coursemgr.model.Course;

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
     * 根据课程id获取课程信息
     * @param courseId
     * @return
     */
    Course getCourseById(String courseId);

    /**
     * 更新课程信息
     * @param course
     * @param isSelective  为true则选择性的更新，及如果对象属性值为空，则不更新，
     *                     反之则不管是否为空都更新
     * @return
     */
    int updateCourse(Course course, boolean isSelective);


}
