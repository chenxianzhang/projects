package edu.coursemgr.pojo;

import edu.coursemgr.model.User;

import java.io.Serializable;

/**
 * Created by chenxianzhang on 2018/8/26 0026 上午 12:21
 */
public class UserEditModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user;

    private String courseId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
