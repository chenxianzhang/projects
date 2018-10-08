package edu.coursemgr.pojo;

import java.io.Serializable;

/**
 * Created by chenxianzhang on 2018/10/8 0008 下午 10:43
 */
public class CourseTaskStatModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer finishedCnt;

    private Integer submitCnt;

    private Integer taskId;

    private String taskName;

    public Integer getFinishedCnt() {
        return finishedCnt;
    }

    public void setFinishedCnt(Integer finishedCnt) {
        this.finishedCnt = finishedCnt;
    }

    public Integer getSubmitCnt() {
        return submitCnt;
    }

    public void setSubmitCnt(Integer submitCnt) {
        this.submitCnt = submitCnt;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
