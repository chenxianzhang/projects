package edu.coursemgr.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chenxianzhang on 2018/8/27 0027 上午 1:22
 */
public class StudentTaskSituation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer taskId;

    private String publishTime;

    private String deadline;

    private String finishStatus;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getFinishStatus() {
        return finishStatus;
    }

    public void setFinishStatus(String finishStatus) {
        this.finishStatus = finishStatus;
    }
}
