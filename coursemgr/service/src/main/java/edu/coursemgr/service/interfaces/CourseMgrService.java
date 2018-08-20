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
}
