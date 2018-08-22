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

    CourseTasks task;

    List<TaskQuestions> questionList;


    public CourseTasks getTask() {
        return task;
    }

    public void setTask(CourseTasks task) {
        this.task = task;
    }

    public List<TaskQuestions> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<TaskQuestions> questionList) {
        this.questionList = questionList;
    }
}
