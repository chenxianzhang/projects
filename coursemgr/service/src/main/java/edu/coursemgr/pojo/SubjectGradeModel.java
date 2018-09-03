package edu.coursemgr.pojo;

import edu.coursemgr.model.StudentPaper;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/9/4 0004 上午 12:24
 */
public class SubjectGradeModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer taskId;

    private String studentNo;

    List<StudentPaper> subjectList;


    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public List<StudentPaper> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<StudentPaper> subjectList) {
        this.subjectList = subjectList;
    }
}
