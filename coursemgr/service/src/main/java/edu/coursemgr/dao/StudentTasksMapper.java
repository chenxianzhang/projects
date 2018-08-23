package edu.coursemgr.dao;

import edu.coursemgr.model.StudentTasks;

public interface StudentTasksMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StudentTasks record);

    int insertSelective(StudentTasks record);

    StudentTasks selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StudentTasks record);

    int updateByPrimaryKey(StudentTasks record);



}