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

    List<TaskQuestions> questionList;


    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public List<TaskQuestions> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<TaskQuestions> questionList) {
        this.questionList = questionList;
    }
}
