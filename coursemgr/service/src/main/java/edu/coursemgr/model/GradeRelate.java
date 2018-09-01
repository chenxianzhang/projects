package edu.coursemgr.model;

import java.io.Serializable;

/**
 * Created by chenxianzhang on 2018/9/1 0001 下午 10:37
 */
public class GradeRelate implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String studentNo;

    private String gradeObjNo;

    private String gradeObjName;

    private Integer taskId;

    private String taskName;

    private Integer courseId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getGradeObjNo() {
        return gradeObjNo;
    }

    public void setGradeObjNo(String gradeObjNo) {
        this.gradeObjNo = gradeObjNo;
    }

    public String getGradeObjName() {
        return gradeObjName;
    }

    public void setGradeObjName(String gradeObjName) {
        this.gradeObjName = gradeObjName;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
