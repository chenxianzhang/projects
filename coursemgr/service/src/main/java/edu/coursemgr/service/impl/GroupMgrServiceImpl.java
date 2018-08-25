package edu.coursemgr.service.impl;

import edu.coursemgr.dao.CourseStudentsMapper;
import edu.coursemgr.service.interfaces.GroupMgrService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/26 0026 上午 12:47
 */
public class GroupMgrServiceImpl implements GroupMgrService {

    @Autowired
    private CourseStudentsMapper courseStudentsMapper;

    @Override
    public boolean freeGroup(String courseId, String memberCnt) {
        // 获取当前所有未分组的学生学号,随机排序
        List<String> studentNoList = courseStudentsMapper.getUngroupedStudent(
                Integer.valueOf(courseId));
        if (studentNoList == null) {
            return true;
        }
        // 遍历剩下的学生信息，进行分组
        int memCnt = Integer.valueOf(memberCnt);
        int cnt = 0;
        // 插入一个分组信息
        // 先获取当前分组序号

        studentNoList.forEach(studentNo -> {

        });
        return false;
    }
}
