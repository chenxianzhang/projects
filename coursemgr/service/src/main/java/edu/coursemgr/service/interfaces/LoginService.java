package edu.coursemgr.service.interfaces;

/**
 * Created by chenxianzhang on 2018/8/8 0008 下午 9:14
 */
public interface LoginService {

    boolean login(String serialNo, String password) throws Exception;

}
