package edu.coursemgr.dao;

import edu.coursemgr.model.Course;

import java.util.List;

public interface CourseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Course record);

    int insertSelective(Course record);

    Course selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Course record);

    int updateByPrimaryKeyWithBLOBs(Course record);

    int updateByPrimaryKey(Course record);

    List<Course> selectAll();

    List<Course> selectTeacherCourse(String teacherNo);

    List<Course> selectStuCourse(String stuNo);
}