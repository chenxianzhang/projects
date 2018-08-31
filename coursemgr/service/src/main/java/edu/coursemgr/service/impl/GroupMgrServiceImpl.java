package edu.coursemgr.service.impl;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.dao.*;
import edu.coursemgr.model.Course;
import edu.coursemgr.model.Group;
import edu.coursemgr.model.GroupMember;
import edu.coursemgr.model.User;
import edu.coursemgr.pojo.AssignGroupModel;
import edu.coursemgr.pojo.GroupDetail;
import edu.coursemgr.service.interfaces.GroupMgrService;
import edu.coursemgr.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

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

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public boolean randGroup(String courseId, String memberCnt) {
        Integer cseId = Integer.valueOf(courseId);
        // 获取当前所有未分组的学生,随机排序
        List<User> studentList =userMapper.selectSomeNoGroup(cseId);
        if (studentList == null || studentList.size() == 0) {
            return true;
        }
        // 遍历剩下的学生信息，进行分组
        int memCnt = Integer.valueOf(memberCnt);

        // 先获取当前分组序号
        Integer groupNo = groupMapper.getLastGroupNo(cseId);
        if (null == groupNo) {
            groupNo = 0;
        }
        // 插入一个分组信息,并获取其主键id
        Group group = new Group();
        List<GroupMember> groupMembers = new ArrayList<>(memCnt);
        for (User student : studentList) {
            if (groupMembers.size() == 0) {
                groupNo += 1;
                group.setCourseId(cseId);
                group.setCreateDate(new Date());
                group.setGroupLeaderNo(student.getSerialNo());
                group.setLeaderName(student.getName());
                group.setGroupNo(groupNo);
                group.setGroupedType(CommonEnum.GroupedType.RAND.getName());
                int flag = groupMapper.insert(group);
                if (flag == 0) {
                    return false;
                }
            }
            GroupMember member = new GroupMember();
            member.setGroupId(group.getId());
            member.setStudentNo(student.getSerialNo());
            member.setCourseId(cseId);
            groupMembers.add(member);
            if (groupMembers.size() == memCnt) {
                insertBatch(groupMembers);
                groupMembers.clear();
                group = new Group();
            }
        }
        if (groupMembers.size() > 0) {
            insertBatch(groupMembers);
            groupMembers.clear();
        }

        // 更新分组模式
        Course course = new Course();
        course.setId(cseId);
        course.setGroupingType(CommonEnum.GroupedType.RAND.getName());

        return courseMapper.updateByIdSelective(course) > 0 ? true : false;
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
        group.setLeaderName(assignGroupModel.getLeaderName());
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
                    member.setCourseId(assignGroupModel.getCourseId());
                    return member;
                });
        if (groupMembers != null) {
            insertBatch(groupMembers);
        }

        // 更新分组模式
        Course course = new Course();
        course.setId(assignGroupModel.getCourseId());
        course.setGroupingType(CommonEnum.GroupedType.ASSIGN.getName());
        return courseMapper.updateByIdSelective(course) > 0 ? true : false;
    }

    @Override
    public int changeGroupLeader(String groupId, String studentNo, String studentName) {

        Group group = new Group();
        group.setGroupLeaderNo(studentNo);
        group.setLeaderName(studentName);
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
                    detail.setGroupLeaderName(group.getLeaderName());
                    List<User> members = userMapper.selectSomByGroupId(group.getId());
                    detail.setGroupMemberList(members);

                    return detail;
                });
        return groupDetails;
    }

    @Override
    public GroupDetail getGroupDetailByStudent(String courseId, String studentNo) {

        Map<String, Object> params = new HashMap<>(2);
        params.put("courseId", courseId);
        params.put("studentNo", studentNo);
        Group group = groupMapper.selectByStudent(params);
        if (group != null) {
            GroupDetail detail = new GroupDetail();
            detail.setGroupedType(group.getGroupedType());
            detail.setGroupId(group.getId());
            detail.setGroupLeaderNo(group.getGroupLeaderNo());
            detail.setGroupLeaderName(group.getLeaderName());
            List<User> members = userMapper.selectSomByGroupId(group.getId());
            detail.setGroupMemberList(members);
            return detail;
        }
        return null;
    }

    @Override
    public Integer checkCourseGrouped(String courseId) {
        List<User> userList = userMapper.selectSomeNoGroup(Integer.valueOf(courseId));
        if (userList != null) {
            return 0;
        }
        List<Group> groupList = groupMapper.getGroupList(Integer.valueOf(courseId));
        if (groupList == null) {
            return 0;
        }
        return groupList.size();
    }

    @Override
    public boolean updateGroupMemberGradeObj(Integer courseId, String markType) {

        Map<Integer, List<GroupMember>> memberMap = updateMemberGradeObj(
                courseId, markType);
        if (memberMap != null) {
            for (Map.Entry<Integer, List<GroupMember>> entry : memberMap.entrySet()) {
                updateBatch(entry.getValue());
            }
        }
        return true;
    }

    private Map<Integer, List<GroupMember>> updateMemberGradeObj(Integer courseId, String markType) {
        // 根据课程id获取所有组成员信息
        List<GroupMember> groupMembers = groupMemberMapper.selectByCourseId(courseId);
        if (groupMembers == null) {
            return null;
        }
        // 根据组分配信息
        Map<Integer, List<GroupMember>> memberMap = new HashMap<>();
        for (GroupMember member : groupMembers) {
            if (!memberMap.containsKey(member.getGroupId())) {
                memberMap.put(member.getGroupId(), new ArrayList<>());
            }
            memberMap.get(member.getGroupId()).add(member);
        }

        // 判断当前评分类型，根据不同的类型进行数据更新
        if (markType.equals(CommonEnum.GradeType.GROUP_INNER_EVA.getValue())) {
            for (Map.Entry<Integer, List<GroupMember>> entry : memberMap.entrySet()) {
                groupInnerEva(entry.getValue());
            }
        } else {
            List<Integer> keyList = new ArrayList<>();
            keyList.addAll(memberMap.keySet());
            for (int i = 0; i < keyList.size(); i++) {
                if (i == (keyList.size() - 1)) {
                    groupInterBlockEva(memberMap.get(keyList.get(i)), memberMap.get(keyList.get(0)));
                    break;
                }
                groupInterBlockEva(memberMap.get(keyList.get(i)), memberMap.get(keyList.get(i + 1)));
            }
        }

        return memberMap;
    }

    private void groupInnerEva(List<GroupMember> groupMembers) {
        for (int i = 0; i < groupMembers.size(); i++) {
            if (i == (groupMembers.size() - 1)) {
                groupMembers.get(i).setGradeObjNo(groupMembers.get(0).getStudentNo());
                break;
            }
            groupMembers.get(i).setGradeObjNo(groupMembers.get(i + 1).getStudentNo());
        }
    }

    /**
     *
     * @param prevList  数量大的组  p1,p2,p3,p4,p5,p6
     * @param lastList  数量稍小的组 l1,l2         ->p1-l1-p2-l2-p3 l1-p1 l2-p4 l1-p5 l2-p6
     */
    private void groupInterBlockEva(List<GroupMember> prevList, List<GroupMember> lastList) {
        for (int i = 0; i < prevList.size(); i++) {
            if (i >= lastList.size()) {
                if ((i + 1) < prevList.size()) {
                    lastList.get(i % lastList.size()).setGradeObjNo(getGradeObjNo(
                            lastList.get(i % lastList.size()).getGradeObjNo(),
                            prevList.get(i + 1).getStudentNo()));
                } else {
                    lastList.get(i % lastList.size()).setGradeObjNo(getGradeObjNo(
                            lastList.get(i % lastList.size()).getGradeObjNo(),
                            prevList.get(0).getStudentNo()));
                }

            } else {
                prevList.get(i).setGradeObjNo(lastList.get(i).getStudentNo());
                if ((i + 1) < prevList.size()) {
                    lastList.get(i).setGradeObjNo(prevList.get(i + 1).getStudentNo());
                } else {
                    lastList.get(i).setGradeObjNo(prevList.get(0).getStudentNo());
                }
            }
        }
    }

    private String getGradeObjNo(String origin, String dst) {
        if (origin.isEmpty()) {
            return dst;
        }
        return String.format("%s,%s", origin, dst);
    }


    private void insertBatch(List<GroupMember> groupMembers) {
        if (groupMembers == null) {
            return;
        }

        groupMembers.forEach(groupMember -> groupMemberMapper.insert(groupMember));
    }

    private void updateBatch(List<GroupMember> groupMembers) {
        if (groupMembers == null) {
            return;
        }

        groupMembers.forEach(groupMember -> groupMemberMapper.updateById(groupMember));
    }
}
