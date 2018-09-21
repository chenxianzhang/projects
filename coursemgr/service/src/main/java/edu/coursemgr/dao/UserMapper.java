package edu.coursemgr.dao;

import edu.coursemgr.model.User;
import edu.coursemgr.pojo.UserGroup;
import edu.coursemgr.pojo.UserInfo;

import java.util.List;
import java.util.Map;

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

    List<User> selectSomeNoGroup(Integer courseId);

    int selectTotalCntByCourseId(Integer courseId);

    List<User> selectSomeByPage(Map params);

    int selectUserGroupTotalCnt(Integer courseId);

    List<UserGroup> selectUserGroupPage(Map params);

    int getFinishedTaskStuTotalCnt(Integer courseId);

    List<User> selectFinishedTaskStu(Map params);
}