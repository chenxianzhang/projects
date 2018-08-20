package edu.coursemgr.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 用于与前端交互的pojo类
 * Created by chenxianzhang on 2018/8/11 0011 上午 1:24
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serialNo;

    private String name;

    private String college;

    private String cellphone;

    private String email;

    private List<String> roles;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
