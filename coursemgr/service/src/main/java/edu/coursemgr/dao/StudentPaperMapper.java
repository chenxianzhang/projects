package edu.coursemgr.dao;

import edu.coursemgr.model.StudentPaper;

import java.util.List;
import java.util.Map;

public interface StudentPaperMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StudentPaper record);

    int insertSelective(StudentPaper record);

    StudentPaper selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StudentPaper record);

    int updateByPrimaryKey(StudentPaper record);

    /**
     * 批量插入试题信息
     * @param studentPapers
     * @return
     */
    int insertBatch(List<StudentPaper> studentPapers);

    /**
     * 根据学号和试题id更新学生主观题分数
     * @param record
     * @return
     */
    int updateByStudent(StudentPaper record);

    /**
     * 根据学号和任务id获取学生试卷信息
     * @param params
     * @return
     */
    List<StudentPaper> getPaperByStudent(Map params);
}