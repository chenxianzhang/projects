package edu.coursemgr.pojo;

import edu.coursemgr.model.QuestionOptions;
import edu.coursemgr.model.TaskQuestions;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/9/7 0007 下午 11:13
 */
public class TaskPaper implements Serializable {

    private static final long serialVersionUID = 1L;

    private TaskQuestions taskQuestions;

    private List<QuestionOptions> optionList;

    public TaskQuestions getTaskQuestions() {
        return taskQuestions;
    }

    public void setTaskQuestions(TaskQuestions taskQuestions) {
        this.taskQuestions = taskQuestions;
    }

    public List<QuestionOptions> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<QuestionOptions> optionList) {
        this.optionList = optionList;
    }
}
