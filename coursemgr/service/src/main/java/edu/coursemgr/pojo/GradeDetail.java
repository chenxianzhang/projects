package edu.coursemgr.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/26 0026 上午 3:38
 */
public class GradeDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private String studentName;

    private String studentNo;

    private Integer groupNo;

    List<StudentTaskInfo> studentTaskInfos;

    private Float totalScore;

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

    public List<StudentTaskInfo> getStudentTaskInfos() {
        return studentTaskInfos;
    }

    public void setStudentTaskInfos(List<StudentTaskInfo> studentTaskInfos) {
        this.studentTaskInfos = studentTaskInfos;
    }

    public Float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Float totalScore) {
        this.totalScore = totalScore;
    }
}
