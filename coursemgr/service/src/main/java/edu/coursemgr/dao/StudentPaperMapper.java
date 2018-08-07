package edu.coursemgr.dao;

import edu.coursemgr.model.StudentPaper;

public interface StudentPaperMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StudentPaper record);

    int insertSelective(StudentPaper record);

    StudentPaper selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StudentPaper record);

    int updateByPrimaryKey(StudentPaper record);
}