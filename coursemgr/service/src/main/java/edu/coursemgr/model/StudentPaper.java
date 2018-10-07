package edu.coursemgr.model;

import java.io.Serializable;

public class StudentPaper implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer taskId;

    private String studentNo;

    private Long questionId;

    private String questionType;

    private String answers;

    private Float score;

    private Float teacherScore;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo == null ? null : studentNo.trim();
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers == null ? null : answers.trim();
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Float getTeacherScore() {
        return teacherScore;
    }

    public void setTeacherScore(Float teacherScore) {
        this.teacherScore = teacherScore;
    }
}