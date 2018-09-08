package edu.coursemgr.dao;

import edu.coursemgr.model.GroupMember;

import java.util.List;
import java.util.Map;

public interface GroupMemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupMember record);

    int insertSelective(GroupMember record);

    GroupMember selectById(Integer id);

    int updateByIdSelective(GroupMember record);

    int updateById(GroupMember record);

    /**
     * 批量插入组员信息
     * @param groupMembers
     * @return
     */
    int insertBatch(List<GroupMember> groupMembers);

    /**
     * 跟课程id获取其所有组中的所有组成员信息
     * @param courseId
     * @return
     */
    List<GroupMember> selectByCourseId(Integer courseId);

    /**
     * 根据课程id和学号获取其所在组的信息
     * @param params
     * @return
     */
    GroupMember selectByMember(Map<String, Object> params);

    List<GroupMember> selectOtherMember(Map params);

    int deleteByStudent(Map params);

    int deleteByGroupId(Integer groupId);
}