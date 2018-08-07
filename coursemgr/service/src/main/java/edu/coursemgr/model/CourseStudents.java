package edu.coursemgr.model;

import java.io.Serializable;

public class CourseStudents implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer courseId;

    private String studentNo;


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

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo == null ? null : studentNo.trim();
    }
}