package edu.coursemgr.pojo;

import java.io.Serializable;

/**
 * Created by chenxianzhang on 2018/9/2 0002 下午 9:50
 */
public class StudentQuestion  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long questionId;

    private String questionType;

    private String standardAnswers;

    private String answers;

    private Float score;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getStandardAnswers() {
        return standardAnswers;
    }

    public void setStandardAnswers(String standardAnswers) {
        this.standardAnswers = standardAnswers;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
}
