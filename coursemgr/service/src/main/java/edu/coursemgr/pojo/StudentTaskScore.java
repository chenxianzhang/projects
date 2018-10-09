package edu.coursemgr.pojo;

import java.io.Serializable;

/**
 * Created by chenxianzhang on 2018/10/9 0009 下午 11:25
 */
public class StudentTaskScore implements Serializable {

    private static final long serialVersionUID = 1L;

    private Float taskWeight;

    private Float score;

    private Integer taskId;

    private String studentNo;

    public Float getTaskWeight() {
        return taskWeight;
    }

    public void setTaskWeight(Float taskWeight) {
        this.taskWeight = taskWeight;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }
}
