package edu.coursemgr.service.impl;

import edu.coursemgr.dao.UserMapper;
import edu.coursemgr.model.User;
import edu.coursemgr.service.interfaces.UserMgrService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/23 0023 下午 10:58
 */
public class UserMgrServiceImpl implements UserMgrService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getStudentsByCourseId(String courseId) {
        return userMapper.selectSomeByCourseId(Integer.valueOf(courseId));
    }

    @Override
    public int addStudent(User user) {
        return userMapper.insert(user);
    }
}
