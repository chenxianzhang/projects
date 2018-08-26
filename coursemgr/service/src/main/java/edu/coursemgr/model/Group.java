package edu.coursemgr.model;

import java.io.Serializable;
import java.util.Date;

public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer groupNo;

    private String groupLeaderNo;

    private String leaderName;

    private Integer courseId;

    private Date createDate;

    private String groupedType;


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

    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    public String getGroupedType() {
        return groupedType;
    }

    public void setGroupedType(String groupedType) {
        this.groupedType = groupedType;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }
}