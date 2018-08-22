package edu.coursemgr.dao;

import edu.coursemgr.model.CourseTasks;

public interface CourseTasksMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CourseTasks record);

    int insertSelective(CourseTasks record);

    CourseTasks selectByTaskId(Integer id);

    int updateByPrimaryKeySelective(CourseTasks record);

    int updateByPrimaryKey(CourseTasks record);
}