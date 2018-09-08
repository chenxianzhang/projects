package edu.coursemgr.model;

import java.io.Serializable;

/**
 * Created by chenxianzhang on 2018/9/7 0007 下午 10:14
 */
public class QuestionOptions implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long questionId;

    private String optionTag;

    private String optionDes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getOptionTag() {
        return optionTag;
    }

    public void setOptionTag(String optionTag) {
        this.optionTag = optionTag;
    }

    public String getOptionDes() {
        return optionDes;
    }

    public void setOptionDes(String optionDes) {
        this.optionDes = optionDes;
    }
}
