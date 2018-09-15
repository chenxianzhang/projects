package edu.coursemgr.service.interfaces;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.model.CourseTasks;
import edu.coursemgr.pojo.AssignGroupModel;
import edu.coursemgr.pojo.GroupDetail;

import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/26 0026 上午 12:47
 */
public interface GroupMgrService {

    /**
     * 随机分组
     * @param courseId
     * @param memberCnt
     * @return
     */
    boolean randGroup(String courseId, String memberCnt);

    /**
     * 添加到已有分组
     * @param groupId
     * @param studentNos
     * @return
     */
    int appendStudent2Group(String groupId, String studentNos);

    /**
     * 指定分组
     * @param assignGroupModel
     * @return
     */
    boolean assignGroup(AssignGroupModel assignGroupModel, CommonEnum.GroupedType type);

    /**
     * 更换组长
     * @param groupId
     * @param studentNo
     * @param studentName
     * @return
     */
    int changeGroupLeader(String groupId, String studentNo, String studentName);

    /**
     * 解除分组
     * @param groupId
     * @return
     */
    boolean relieveGroup(String groupId, String groupNo);

    /**
     * 获取课程下面所有组信息
     * @param courseId
     * @return
     */
    List<GroupDetail> getGroupDetail(String courseId);

    /**
     * 查询我的分组
     * @param courseId
     * @param studentNo
     * @return
     */
    GroupDetail getGroupDetailByStudent(String courseId, String studentNo);

    /**
     * 确定当前课程是否已完成分组
     * @param courseId
     * @return
     */
    Integer checkCourseGrouped(String courseId);
}
