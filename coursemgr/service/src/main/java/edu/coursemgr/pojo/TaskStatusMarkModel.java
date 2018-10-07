package edu.coursemgr.pojo;

import java.io.Serializable;

/**
 * Created by chenxianzhang on 2018/10/7 0007 下午 4:54
 */
public class TaskStatusMarkModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer taskId;

    private String markType;

    private String status;

    private Integer subjectCnt;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getMarkType() {
        return markType;
    }

    public void setMarkType(String markType) {
        this.markType = markType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSubjectCnt() {
        return subjectCnt;
    }

    public void setSubjectCnt(Integer subjectCnt) {
        this.subjectCnt = subjectCnt;
    }
}
