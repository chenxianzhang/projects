package edu.coursemgr.dao;

import edu.coursemgr.model.TaskQuestions;
import edu.coursemgr.pojo.SubjectMarkModel;

import java.util.List;
import java.util.Map;

public interface TaskQuestionsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TaskQuestions record);

    int insertSelective(TaskQuestions record);

    TaskQuestions selectByPrimaryKey(Long id);

    int updateByIdSelective(TaskQuestions record);

    int updateByPrimaryKeyWithBLOBs(TaskQuestions record);

    int updateByPrimaryKey(TaskQuestions record);

    /**
     * 批量插入任务问题信息
     * @param taskQuestions
     * @return
     */
    int insertBatch(List<TaskQuestions> taskQuestions);

    /**
     * 根据任务id，获取当前任务下所有题目信息
     * @param taskId
     * @return
     */
    List<TaskQuestions> selectSomeByTaskId(Integer taskId);

    int deleteByTaskId(Integer taskId);

    List<SubjectMarkModel> selectStuTaskPaper(Map params);

    int deleteByIds(String ids);
}