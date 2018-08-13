package edu.coursemgr.service.interfaces;

import edu.coursemgr.pojo.UserInfo;


/**
 * Created by chenxianzhang on 2018/8/8 0008 下午 9:14
 */
public interface LoginService {

    /**
     * 登录操作
     * @param serialNo
     * @param password
     * @return
     * @throws Exception
     */
    String login(String serialNo, String password) throws Exception;

    /**
     * 获取用户信息
     * @param serialNo
     * @return
     * @throws Exception
     */
    UserInfo getUserInfo(String serialNo) throws Exception;

}
