package edu.coursemgr.service.interfaces;

import edu.coursemgr.model.CourseTasks;
import edu.coursemgr.model.GradeRelate;
import edu.coursemgr.model.User;
import edu.coursemgr.pojo.Schedule;
import edu.coursemgr.pojo.SubjectGradeModel;

import java.util.List;

/**
 * Created by chenxianzhang on 2018/9/1 0001 下午 9:17
 */
public interface GradeMgrService {


    /**
     * 更新组成员评分对象
     * @param courseId
     * @param markType
     * @param task
     * @return
     */
    boolean updateGroupMemberGradeObj(Integer courseId, String markType,
                                      CourseTasks task);

    /**
     * 根据课程id和学号获取该学生的待办任务
     * @param courseId
     * @param studentNo
     * @return
     */
    List<Schedule> getMySchedule(String courseId, String studentNo);

    /**
     * 更新主题分数
     * @param subjectGradeModel
     * @return
     */
    int updateSubjectScore(SubjectGradeModel subjectGradeModel);

    /**
     * 根据任务id和学生学号获取该学生的主观题评分人是谁
     * @param taskId
     * @param studentNo
     * @return
     */
    User getSubjectGradePerson(String taskId, String studentNo);

    /**
     * 获取指定学生下的所有待办
     * @param studentNo
     * @return
     */
    List<Schedule> getScheduleByStudent(String studentNo, String courseId);

    /**
     * 移交待办
     * @param originStuNo
     * @param dstStuNo
     */
    void handOverSchedule(String courseId, String originStuNo, String dstStuNo, List<Schedule> sheduleList);
}
