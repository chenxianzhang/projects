package edu.coursemgr.dao;

import edu.coursemgr.model.Course;

import java.util.List;

public interface CourseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Course record);

    int insertSelective(Course record);

    Course selectById(Integer id);

    int updateByIdSelective(Course record);

    int updateByIdWithBLOBs(Course record);

    int updateById(Course record);

    List<Course> selectAll();

    List<Course> selectTeacherCourse(String teacherNo);

    List<Course> selectStuCourse(String stuNo);

    int deleteByCourseIds(String courseIds);

    String checkStudentBacklog(String studentNo);

    String checkStudentGroup(String studentNo);
}