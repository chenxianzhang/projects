package edu.coursemgr.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chenxianzhang on 2018/8/25 0025 下午 11:42
 */
public class CourseTaskSituation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer courseId;

    private String name;

    private Date publishTime;

    private Date startTime;

    private Date deadline;

    private Integer finishPersonCnt;

    private Integer totalPersonCnt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Integer getFinishPersonCnt() {
        return finishPersonCnt;
    }

    public void setFinishPersonCnt(Integer finishPersonCnt) {
        this.finishPersonCnt = finishPersonCnt;
    }

    public Integer getTotalPersonCnt() {
        return totalPersonCnt;
    }

    public void setTotalPersonCnt(Integer totalPersonCnt) {
        this.totalPersonCnt = totalPersonCnt;
    }
}
