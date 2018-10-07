package edu.coursemgr.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/10/7 0007 下午 9:23
 */
public class StatAllGradeModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Float maxScore;

    private Float minScore;

    private Float avgScore;

    private List<StudentScore> studentScoreList;


    public Float getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Float maxScore) {
        this.maxScore = maxScore;
    }

    public Float getMinScore() {
        return minScore;
    }

    public void setMinScore(Float minScore) {
        this.minScore = minScore;
    }

    public Float getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(Float avgScore) {
        this.avgScore = avgScore;
    }

    public List<StudentScore> getStudentScoreList() {
        return studentScoreList;
    }

    public void setStudentScoreList(List<StudentScore> studentScoreList) {
        this.studentScoreList = studentScoreList;
    }
}
