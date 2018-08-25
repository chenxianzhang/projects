package edu.coursemgr.service.impl;

import edu.coursemgr.dao.CourseStudentsMapper;
import edu.coursemgr.dao.UserMapper;
import edu.coursemgr.model.CourseStudents;
import edu.coursemgr.model.User;
import edu.coursemgr.pojo.UserEditModel;
import edu.coursemgr.service.interfaces.UserMgrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/23 0023 下午 10:58
 */
@Service
public class UserMgrServiceImpl implements UserMgrService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseStudentsMapper courseStudentsMapper;

    @Override
    public List<User> getStudentsByCourseId(String courseId) {
        return userMapper.selectSomeByCourseId(Integer.valueOf(courseId));
    }

    @Override
    public int addStudent(UserEditModel userEditModel) throws Exception {
        // 检查数据库中是否存在当前用户
        User user = userMapper.selectBySerialNo(userEditModel.getUser().getSerialNo());
        if (null != user) {
            throw new Exception("当前用户已存在，不可重复添加");
        }
        if (userMapper.insert(userEditModel.getUser()) > 0) {
            // 保存课程与学员关系
            CourseStudents cs = new CourseStudents();
            cs.setCourseId(Integer.valueOf(userEditModel.getCourseId()));
            cs.setStudentNo(userEditModel.getUser().getSerialNo());
            if (this.courseStudentsMapper.insert(cs) == 0) {
                throw new Exception("将学员加入到当前课程中发生异常");
            }
        }
        return 1;
    }
}
