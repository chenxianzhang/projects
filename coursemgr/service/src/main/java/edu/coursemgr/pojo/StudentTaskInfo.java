package edu.coursemgr.pojo;

import java.io.Serializable;

/**
 * Created by chenxianzhang on 2018/8/26 0026 上午 3:41
 */
public class StudentTaskInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String taskName;

    private Float taskWeight;

    private Float score;

    private Integer taskId;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

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
}
