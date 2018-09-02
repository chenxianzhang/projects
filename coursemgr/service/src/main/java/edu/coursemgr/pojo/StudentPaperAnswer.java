package edu.coursemgr.pojo;

import edu.coursemgr.model.TaskQuestions;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/27 0027 上午 2:14
 */
public class StudentPaperAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    private String studentNo;

    private String courseId;

    private String taskId;

    List<StudentQuestion> questionList;


    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public List<StudentQuestion> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<StudentQuestion> questionList) {
        this.questionList = questionList;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
