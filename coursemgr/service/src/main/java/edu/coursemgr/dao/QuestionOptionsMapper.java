package edu.coursemgr.dao;

import edu.coursemgr.model.QuestionOptions;

import java.util.List;

/**
 * Created by chenxianzhang on 2018/9/7 0007 下午 10:17
 */
public interface QuestionOptionsMapper {

    int insert(QuestionOptions record);

    int insertSelective(QuestionOptions record);

    int updateSelective(QuestionOptions record);

    List<QuestionOptions> selectByQuestionId(Long questionId);
}
