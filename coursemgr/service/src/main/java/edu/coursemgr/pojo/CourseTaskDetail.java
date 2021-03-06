package edu.coursemgr.pojo;

import edu.coursemgr.model.CourseTasks;
import edu.coursemgr.model.TaskQuestions;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/22 0022 下午 11:52
 */
public class CourseTaskDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private CourseTasks task;

    private List<TaskPaper> questionList;

    private Float studentTotalScore;

    private String status;


    public CourseTasks getTask() {
        return task;
    }

    public void setTask(CourseTasks task) {
        this.task = task;
    }

    public List<TaskPaper> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<TaskPaper> questionList) {
        this.questionList = questionList;
    }

    public Float getStudentTotalScore() {
        return studentTotalScore;
    }

    public void setStudentTotalScore(Float studentTotalScore) {
        this.studentTotalScore = studentTotalScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
