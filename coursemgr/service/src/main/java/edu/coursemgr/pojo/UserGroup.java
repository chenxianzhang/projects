package edu.coursemgr.pojo;

import java.io.Serializable;

/**
 * Created by chenxianzhang on 2018/8/26 0026 上午 3:49
 */
public class UserGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    private String studentName;

    private String studentNo;

    private Integer groupNo;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }
}
