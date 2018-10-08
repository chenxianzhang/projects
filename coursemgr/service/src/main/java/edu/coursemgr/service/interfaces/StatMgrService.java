package edu.coursemgr.service.interfaces;

import edu.coursemgr.pojo.*;

import java.util.List;

/**
 * Created by chenxianzhang on 2018/10/7 0007 下午 9:21
 */
public interface StatMgrService {


    /**
     * 统计所有成绩
     * @param courseId
     * @param sort
     * @return
     */
    StatAllGradeModel statAllGrade(String courseId, String sort);

    /**
     * 统计任务的提交数和完成数
     * @param courseId
     * @return
     */
    List<CourseTaskStatModel> statTaskSubmitReview(String courseId);


    /**
     * 统计当前课程下所有题型的数量
     * @param courseId
     * @return
     */
    List<QuestionTypeStatModel> statQuestionTypeCnt(String courseId);

    /**
     * 统计学生课程下所有任务的得分
     * @param courseId
     * @param studentNo
     * @return
     */
    List<StudentTaskScoreStat> statStudentTaskScore(String courseId, String studentNo);

    /**
     * 统计学生综合信息
     * @param courseId
     * @param studentNo
     * @return
     */
    StuCourseSynthesizeStat statStuSynthesizeInfo(String courseId, String studentNo);
}
