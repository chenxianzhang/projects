package edu.coursemgr.service.impl;

import edu.coursemgr.dao.UserMapper;
import edu.coursemgr.model.User;
import edu.coursemgr.service.interfaces.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenxianzhang on 2018/8/8 0008 下午 9:16
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean login(String serialNo, String password) throws Exception {
        boolean illegal = serialNo == null || serialNo.isEmpty()
                || password == null || password.isEmpty();
        if (illegal) {
            throw new Exception("用户名和密码不能为空");
        }
        User user = userMapper.selectBySerialNo(serialNo);
        if (null == user) {
            throw new Exception("当前用户不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new Exception("密码错误, 请重新输入");
        }
        return true;
    }
}
