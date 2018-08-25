package edu.coursemgr.service.impl;

import edu.coursemgr.dao.CourseStudentsMapper;
import edu.coursemgr.dao.GroupMapper;
import edu.coursemgr.dao.GroupMemberMapper;
import edu.coursemgr.model.Group;
import edu.coursemgr.model.GroupMember;
import edu.coursemgr.service.interfaces.GroupMgrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public boolean freeGroup(String courseId, String memberCnt) {
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
}
