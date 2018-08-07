package edu.coursemgr.dao;

import edu.coursemgr.model.CourseStudents;

public interface CourseStudentsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CourseStudents record);

    int insertSelective(CourseStudents record);

    CourseStudents selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CourseStudents record);

    int updateByPrimaryKey(CourseStudents record);
}