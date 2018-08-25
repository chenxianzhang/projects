package edu.coursemgr.dao;

import edu.coursemgr.model.CourseStudents;

import java.util.List;

public interface CourseStudentsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CourseStudents record);

    int insertSelective(CourseStudents record);

    CourseStudents selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CourseStudents record);

    int updateByPrimaryKey(CourseStudents record);

    List<String> getUngroupedStudent(Integer courseId);
}