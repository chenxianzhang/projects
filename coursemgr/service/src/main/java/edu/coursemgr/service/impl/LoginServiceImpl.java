package edu.coursemgr.service.impl;

import edu.coursemgr.common.Constant;
import edu.coursemgr.common.interfaces.Function;
import edu.coursemgr.dao.RoleMapper;
import edu.coursemgr.dao.UserMapper;
import edu.coursemgr.model.Role;
import edu.coursemgr.model.User;
import edu.coursemgr.pojo.UserInfo;
import edu.coursemgr.service.interfaces.LoginService;
import edu.coursemgr.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/8 0008 下午 9:16
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public String login(String serialNo, String password) throws Exception {
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
        return serialNo;
    }

    @Override
    public UserInfo getUserInfo(String serialNo) throws Exception {
        // 获取用户信息
        User user = userMapper.selectBySerialNo(serialNo);
        if (user == null) {
            throw new Exception("用户信息丢失");
        }
        Function<UserInfo, User> transfer = param -> {
            UserInfo userInfo = new UserInfo();
            String[] roles = param.getRoles().split(Constant.Common.SEPARATE_COMMA);
            List<Integer> roleIdList = CollectionUtils.arrayListTypeCast(Arrays.asList(roles),
                    id -> Integer.valueOf(id));
            if (roleIdList == null) {
                return userInfo;
            }
            List<Role> roleList = roleMapper.selectByIds(roleIdList);
            userInfo.setName(param.getName());
            userInfo.setSerialNo(param.getSerialNo());
            userInfo.setRoles(CollectionUtils.arrayListTypeCast(roleList,
                    role -> role.getValue()));
            return userInfo;
        };
        return transfer.call(user);
    }
}
