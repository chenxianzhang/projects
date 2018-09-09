package edu.coursemgr.pojo;

import edu.coursemgr.model.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/26 0026 上午 3:11
 */
public class GroupDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer groupNo;

    private String groupLeaderNo;

    private String groupLeaderName;

    private Integer groupId;

    private String groupedType;

    private List<User> groupMemberList;


    public String getGroupLeaderNo() {
        return groupLeaderNo;
    }

    public void setGroupLeaderNo(String groupLeaderNo) {
        this.groupLeaderNo = groupLeaderNo;
    }

    public String getGroupLeaderName() {
        return groupLeaderName;
    }

    public void setGroupLeaderName(String groupLeaderName) {
        this.groupLeaderName = groupLeaderName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupedType() {
        return groupedType;
    }

    public void setGroupedType(String groupedType) {
        this.groupedType = groupedType;
    }

    public List<User> getGroupMemberList() {
        return groupMemberList;
    }

    public void setGroupMemberList(List<User> groupMemberList) {
        this.groupMemberList = groupMemberList;
    }

    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }
}
