package edu.coursemgr.pojo;

import java.io.Serializable;

/**
 * Created by chenxianzhang on 2018/10/8 0008 下午 11:00
 */
public class StuCourseSynthesizeStat implements Serializable {

    private static final long serialVersionUID = 1L;

    private Float totalScore;

    private Integer rank;

    private Float objectiveItemScoreRate;

    private Float subjectScoreRate;

    public Float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Float totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Float getObjectiveItemScoreRate() {
        return objectiveItemScoreRate;
    }

    public void setObjectiveItemScoreRate(Float objectiveItemScoreRate) {
        this.objectiveItemScoreRate = objectiveItemScoreRate;
    }

    public Float getSubjectScoreRate() {
        return subjectScoreRate;
    }

    public void setSubjectScoreRate(Float subjectScoreRate) {
        this.subjectScoreRate = subjectScoreRate;
    }
}
