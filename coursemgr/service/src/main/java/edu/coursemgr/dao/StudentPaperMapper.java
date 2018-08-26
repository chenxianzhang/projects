package edu.coursemgr.dao;

import edu.coursemgr.model.StudentPaper;

import java.util.List;

public interface StudentPaperMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StudentPaper record);

    int insertSelective(StudentPaper record);

    StudentPaper selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StudentPaper record);

    int updateByPrimaryKey(StudentPaper record);

    /**
     * 批量插入试题信息
     * @param groupMembers
     * @return
     */
    int insertBatch(List<StudentPaper> studentPapers);
}