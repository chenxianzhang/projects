package edu.coursemgr.pojo;

import java.io.Serializable;

/**
 * Created by chenxianzhang on 2018/10/8 0008 下午 10:49
 */
public class QuestionTypeStatModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer count;

    private String questionTypeValue;

    private String getQuestionTypeText;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getQuestionTypeValue() {
        return questionTypeValue;
    }

    public void setQuestionTypeValue(String questionTypeValue) {
        this.questionTypeValue = questionTypeValue;
    }

    public String getGetQuestionTypeText() {
        return getQuestionTypeText;
    }

    public void setGetQuestionTypeText(String getQuestionTypeText) {
        this.getQuestionTypeText = getQuestionTypeText;
    }
}
