package edu.coursemgr.service.impl;

import edu.coursemgr.common.CommonEnum;
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

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/8 0008 下午 9:16
 */
@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public String login(String serialNo, String password, String role) throws Exception {
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
        illegal = role.isEmpty() || !user.getRoles().contains(role);
        if (illegal) {
            throw new Exception("当前所选角色不匹配");
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
            userInfo.setName(param.getName());
            userInfo.setSerialNo(param.getSerialNo());
            userInfo.setCellphone(param.getCellphone());
            userInfo.setCollege(param.getCollege());
            userInfo.setEmail(param.getEmail());
            userInfo.setRoles(Arrays.asList(roles));
            return userInfo;
        };
        return transfer.call(user);
    }

    @Override
    public List<Role> getRoleList() throws Exception {
        return roleMapper.selectAll();
    }

    @Override
    public int update(User userInfo) {
        userInfo.setHasLogin(1);
        return userMapper.updateBySerialNo(userInfo);
    }

    @Override
    public User findUser(String serialNo) throws Exception {
        // 获取用户信息
        User user = userMapper.selectBySerialNo(serialNo);
        if (user == null) {
            throw new Exception("用户信息丢失");
        }
        return user;
    }

    @Override
    public int register(User user) throws Exception {

        user.setRoles(CommonEnum.Role.TEACHER.getValue());

        // 检测是否已经注册
        User hasUser = userMapper.selectBySerialNo(user.getSerialNo());
        if (hasUser != null) {
            throw new Exception("已经存在该用户");
        }
        user.setHasLogin(1);
        return userMapper.insert(user);
    }
}
