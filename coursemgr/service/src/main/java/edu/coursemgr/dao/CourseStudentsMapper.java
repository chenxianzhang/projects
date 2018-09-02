package edu.coursemgr.dao;

import edu.coursemgr.model.CourseStudents;

import java.util.List;
import java.util.Map;

public interface CourseStudentsMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByStudent(Map params);

    int insert(CourseStudents record);

    int insertSelective(CourseStudents record);

    CourseStudents selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CourseStudents record);

    int updateByPrimaryKey(CourseStudents record);

    List<String> getUngroupedStudent(Integer courseId);
}