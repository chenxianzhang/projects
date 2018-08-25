package edu.coursemgr.dao;

import edu.coursemgr.model.User;
import edu.coursemgr.pojo.UserGroup;
import edu.coursemgr.pojo.UserInfo;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectBySerialNo(String serialNo);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int updateBySerialNo(User userInfo);

    List<User> selectSomeByCourseId(Integer courseId);

    List<User> selectSomByGroupId(Integer groupId);

    List<UserGroup> selectUserGroup(Integer courseId);
}