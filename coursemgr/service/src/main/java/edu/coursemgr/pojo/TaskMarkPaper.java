package edu.coursemgr.pojo;

import edu.coursemgr.model.QuestionOptions;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/9/21 0021 下午 10:21
 */
public class TaskMarkPaper implements Serializable {

    private static final long serialVersionUID = 1L;

    private SubjectMarkModel taskQuestions;

    private List<QuestionOptions> optionList;


    public SubjectMarkModel getTaskQuestions() {
        return taskQuestions;
    }

    public void setTaskQuestions(SubjectMarkModel taskQuestions) {
        this.taskQuestions = taskQuestions;
    }

    public List<QuestionOptions> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<QuestionOptions> optionList) {
        this.optionList = optionList;
    }
}
