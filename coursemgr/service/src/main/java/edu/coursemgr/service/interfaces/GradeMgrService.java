package edu.coursemgr.service.interfaces;

import edu.coursemgr.model.CourseTasks;
import edu.coursemgr.model.GradeRelate;

import java.util.List;

/**
 * Created by chenxianzhang on 2018/9/1 0001 下午 9:17
 */
public interface GradeMgrService {


//    List<GroupMember> getMySchedule();

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
    List<GradeRelate> getMySchedule(String courseId, String studentNo);
}
