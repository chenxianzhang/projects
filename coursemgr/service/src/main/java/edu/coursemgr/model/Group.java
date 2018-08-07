package edu.coursemgr.model;

import java.io.Serializable;
import java.util.Date;

public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String groupLeaderNo;

    private Integer courseId;

    private Date createDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupLeaderNo() {
        return groupLeaderNo;
    }

    public void setGroupLeaderNo(String groupLeaderNo) {
        this.groupLeaderNo = groupLeaderNo == null ? null : groupLeaderNo.trim();
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}