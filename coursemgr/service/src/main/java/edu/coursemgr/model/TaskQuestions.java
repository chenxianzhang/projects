package edu.coursemgr.model;

import java.io.Serializable;

public class TaskQuestions implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer taskId;

    private Integer questionNo;

    private String stems;

    private String questionType;

    private Float score;

    private String answers;

    private String markType;

    private String options;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(Integer questionNo) {
        this.questionNo = questionNo;
    }

    public String getStems() {
        return stems;
    }

    public void setStems(String stems) {
        this.stems = stems == null ? null : stems.trim();
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType == null ? null : questionType.trim();
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers == null ? null : answers.trim();
    }

    public String getMarkType() {
        return markType;
    }

    public void setMarkType(String marktype) {
        this.markType = markType == null ? null : markType.trim();
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options == null ? null : options.trim();
    }
}