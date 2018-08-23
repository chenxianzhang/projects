package edu.coursemgr.dao;

import edu.coursemgr.model.StudentTasks;
import edu.coursemgr.pojo.StudentTaskDetail;

import java.util.List;
import java.util.Map;

public interface StudentTasksMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StudentTasks record);

    int insertSelective(StudentTasks record);

    StudentTasks selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StudentTasks record);

    int updateByPrimaryKey(StudentTasks record);

    List<StudentTaskDetail> getStudentTaskSituation(Map params);

}