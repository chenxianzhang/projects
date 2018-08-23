package edu.coursemgr.dao;

import edu.coursemgr.model.CourseTasks;

import java.util.List;

public interface CourseTasksMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CourseTasks record);

    int insertSelective(CourseTasks record);

    CourseTasks selectByTaskId(Integer id);

    int updateByPrimaryKeySelective(CourseTasks record);

    int updateByPrimaryKey(CourseTasks record);

    List<CourseTasks> selectSomeByCourseId(Integer courseId);
}