package edu.coursemgr.pojo;

import java.io.Serializable;

/**
 * Created by chenxianzhang on 2018/9/10 0010 下午 11:45
 */
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer taskId;

    private String taskName;

    private String markPersonSerialNo;

    private String markPersonName;

    private String targetSerialNo;

    private String targetSerialName;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getMarkPersonSerialNo() {
        return markPersonSerialNo;
    }

    public void setMarkPersonSerialNo(String markPersonSerialNo) {
        this.markPersonSerialNo = markPersonSerialNo;
    }

    public String getMarkPersonName() {
        return markPersonName;
    }

    public void setMarkPersonName(String markPersonName) {
        this.markPersonName = markPersonName;
    }

    public String getTargetSerialNo() {
        return targetSerialNo;
    }

    public void setTargetSerialNo(String targetSerialNo) {
        this.targetSerialNo = targetSerialNo;
    }

    public String getTargetSerialName() {
        return targetSerialName;
    }

    public void setTargetSerialName(String targetSerialName) {
        this.targetSerialName = targetSerialName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
