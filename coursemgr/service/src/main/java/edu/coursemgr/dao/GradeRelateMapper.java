package edu.coursemgr.dao;

import edu.coursemgr.model.GradeRelate;

import java.util.List;
import java.util.Map;

/**
 * Created by chenxianzhang on 2018/9/1 0001 下午 10:40
 */
public interface GradeRelateMapper {

    int insert(GradeRelate record);

    int insertSelective(GradeRelate record);

    int deleteByTaskId(Integer taskId);

    List<GradeRelate> selectSomeByStudent(Map<String ,Object> params);

}