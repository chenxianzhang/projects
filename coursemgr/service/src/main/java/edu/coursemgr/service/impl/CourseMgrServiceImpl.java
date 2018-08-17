package edu.coursemgr.service.impl;

import edu.coursemgr.dao.CourseMapper;
import edu.coursemgr.model.Course;
import edu.coursemgr.service.interfaces.CourseMgrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/16 0016 下午 10:43
 */
@Service
public class CourseMgrServiceImpl implements CourseMgrService {

    @Autowired
    private CourseMapper courseMapper;


    @Override
    public List<Course> getTeacherCourseList(String teacherNo) {
        return courseMapper.selectTeacherCourse(teacherNo);
    }

    @Override
    public int saveCourse(String name, String credit, String description,
                              String userNo) {
        Course course = new Course();
        course.setName(name);
        course.setCredit(Integer.valueOf(credit));
        course.setDescription(description);
        course.setCreateDate(new Date());
//        course.setGroupingType();
        course.setCreatorNo(userNo);

        return courseMapper.insert(course);
    }

    @Override
    public List<Course> getStuCourseList(String stuNo) {
        return courseMapper.selectStuCourse(stuNo);
    }
}
