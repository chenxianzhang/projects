package edu.coursemgr.dao;

import edu.coursemgr.model.Group;

import java.util.List;
import java.util.Map;

public interface GroupMapper {
    int deleteById(Integer id);

    int insert(Group record);

    int insertSelective(Group record);

    Group selectByPrimaryKey(Integer id);

    int updateByIdSelective(Group record);

    int updateByPrimaryKey(Group record);

    Integer getLastGroupNo(Integer courseId);

    int updateGroupNo(Group group);

    List<Group> getGroupList(Integer courseId);

    Group selectByStudent(Map params);
}