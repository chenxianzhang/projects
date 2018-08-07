package edu.coursemgr.dao;

import edu.coursemgr.model.TaskQuestions;

public interface TaskQuestionsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TaskQuestions record);

    int insertSelective(TaskQuestions record);

    TaskQuestions selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TaskQuestions record);

    int updateByPrimaryKeyWithBLOBs(TaskQuestions record);

    int updateByPrimaryKey(TaskQuestions record);
}