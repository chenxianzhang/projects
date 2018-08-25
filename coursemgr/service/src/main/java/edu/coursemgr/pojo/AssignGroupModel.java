package edu.coursemgr.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/26 0026 上午 2:26
 */
public class AssignGroupModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String groupLeaderNo;

    private Integer courseId;

    private List<String> studentNoList;

    public String getGroupLeaderNo() {
        return groupLeaderNo;
    }

    public void setGroupLeaderNo(String groupLeaderNo) {
        this.groupLeaderNo = groupLeaderNo;
    }

    public List<String> getStudentNoList() {
        return studentNoList;
    }

    public void setStudentNoList(List<String> studentNoList) {
        this.studentNoList = studentNoList;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
