package edu.coursemgr.service.impl;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.dao.CourseStudentsMapper;
import edu.coursemgr.dao.GroupMapper;
import edu.coursemgr.dao.GroupMemberMapper;
import edu.coursemgr.dao.UserMapper;
import edu.coursemgr.model.Group;
import edu.coursemgr.model.GroupMember;
import edu.coursemgr.model.User;
import edu.coursemgr.pojo.AssignGroupModel;
import edu.coursemgr.pojo.GroupDetail;
import edu.coursemgr.service.interfaces.GroupMgrService;
import edu.coursemgr.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/26 0026 上午 12:47
 */
@Service
@Transactional
public class GroupMgrServiceImpl implements GroupMgrService {

    @Autowired
    private CourseStudentsMapper courseStudentsMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean randGroup(String courseId, String memberCnt) {
        Integer cseId = Integer.valueOf(courseId);
        // 获取当前所有未分组的学生学号,随机排序
        List<String> studentNoList = courseStudentsMapper.getUngroupedStudent(
                cseId);
        if (studentNoList == null || studentNoList.size() == 0) {
            return true;
        }
        // 遍历剩下的学生信息，进行分组
        int memCnt = Integer.valueOf(memberCnt);

        // 先获取当前分组序号
        Integer groupNo = groupMapper.getLastGroupNo(cseId);
        if (null == groupNo) {
            groupNo = 1;
        }
        // 插入一个分组信息,并获取其主键id
        Group group = new Group();
        List<GroupMember> groupMembers = new ArrayList<>(memCnt);
        for (String studentNo : studentNoList) {
            if (groupMembers.size() == 0) {
                groupNo += 1;
                group.setCourseId(cseId);
                group.setCreateDate(new Date());
                group.setGroupLeaderNo(studentNo);
                group.setGroupNo(groupNo);
                group.setGroupedType(CommonEnum.GroupedType.RAND.getName());
                int flag = groupMapper.insert(group);
                if (flag == 0) {
                    return false;
                }
            }
            GroupMember member = new GroupMember();
            member.setGroupId(group.getId());
            member.setStudentNo(studentNo);
            groupMembers.add(member);
            if (groupMembers.size() == 10) {
                groupMemberMapper.insertBatch(groupMembers);
                groupMembers.clear();
            }
        }
        if (groupMembers.size() > 0) {
            groupMemberMapper.insertBatch(groupMembers);
            groupMembers.clear();
        }
        return true;
    }

    @Override
    public int appendStudent2Group(String groupId, String studentNo) {
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(Integer.valueOf(groupId));
        groupMember.setStudentNo(studentNo);
        return groupMemberMapper.insert(groupMember);
    }

    @Override
    public boolean assignGroup(AssignGroupModel assignGroupModel, CommonEnum.GroupedType type) {
        // 获取组号
        Integer groupNo = groupMapper.getLastGroupNo(assignGroupModel.getCourseId());
        if (null == groupNo) {
            groupNo = 1;
        }
        // 新增组
        groupNo += 1;
        final Group group = new Group();
        group.setCourseId(assignGroupModel.getCourseId());
        group.setCreateDate(new Date());
        group.setGroupLeaderNo(assignGroupModel.getGroupLeaderNo());
        group.setGroupNo(groupNo);
        group.setGroupedType(type.getName());
        int flag = groupMapper.insert(group);
        if (flag == 0) {
            return false;
        }
        // 新增组员
        List<GroupMember> groupMembers = CollectionUtils.arrayListCast(assignGroupModel.getStudentNoList(),
                studentNo -> {
                    GroupMember member = new GroupMember();
                    member.setGroupId(group.getId());
                    member.setStudentNo(studentNo);
                    return member;
                });
        if (groupMembers != null) {
            groupMemberMapper.insertBatch(groupMembers);
        }
        return true;
    }

    @Override
    public int changeGroupLeader(String groupId, String studentNo) {

        Group group = new Group();
        group.setGroupLeaderNo(studentNo);
        group.setId(Integer.valueOf(groupId));
        return groupMapper.updateByIdSelective(group);
    }

    @Override
    public boolean relieveGroup(String groupId, String groupNo) {
        // 删除组，并删除组员信息
        if (groupMapper.deleteById(Integer.valueOf(groupId)) == 0) {
            return false;
        }
        // 更新组号
        Group group = new Group();
        group.setId(Integer.valueOf(groupId));
        group.setGroupNo(Integer.valueOf(groupNo));
        return groupMapper.updateGroupNo(group) == 0 ? false : true;
    }

    @Override
    public List<GroupDetail> getGroupDetail(String courseId) {

        // 根据课程id获取所有组信息
        List<Group> groupList = groupMapper.getGroupList(
                Integer.valueOf(courseId));
        if (groupList == null) {
            return null;
        }
        List<GroupDetail> groupDetails = CollectionUtils.arrayListCast(groupList,
                group -> {
                    GroupDetail detail = new GroupDetail();
                    detail.setGroupedType(group.getGroupedType());
                    detail.setGroupId(group.getId());
                    detail.setGroupLeaderNo(group.getGroupLeaderNo());
                    List<User> members = userMapper.selectSomByGroupId(group.getId());
                    detail.setGroupMemberList(members);

                    return detail;
                });
        return groupDetails;
    }
}
