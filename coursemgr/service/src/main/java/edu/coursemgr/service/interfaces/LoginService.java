package edu.coursemgr.service.interfaces;

import edu.coursemgr.model.Role;
import edu.coursemgr.model.User;
import edu.coursemgr.pojo.UserInfo;

import java.util.List;


/**
 * Created by chenxianzhang on 2018/8/8 0008 下午 9:14
 */
public interface LoginService {

    /**
     * 登录操作
     * @param serialNo
     * @param password
     * @param roleId
     * @return
     * @throws Exception
     */
    String login(String serialNo, String password, String roleId) throws Exception;

    /**
     * 获取用户信息
     * @param serialNo
     * @return
     * @throws Exception
     */
    UserInfo getUserInfo(String serialNo) throws Exception;

    /**
     * 获取所有角色
     * @return
     * @throws Exception
     */
    List<Role> getRoleList() throws Exception;

    /**
     *
     * @param userInfo
     * @return
     */
    int update(User userInfo);

    /**
     *
     * @param serialNo
     * @return
     * @throws Exception
     */
    User findUser(String serialNo) throws Exception;

    /**
     * 注册
     * @param user
     * @return
     */
    int register(User user) throws Exception ;

}
