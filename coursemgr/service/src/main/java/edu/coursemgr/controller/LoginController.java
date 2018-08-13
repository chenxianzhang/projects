package edu.coursemgr.controller;

import edu.coursemgr.service.interfaces.LoginService;
import edu.coursemgr.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value="/login", method=RequestMethod.POST)
    @ResponseBody
    public Object login(@RequestBody Map<String, Object> requestMap) throws Exception {
        // 校验验证码
        String code = getParam(requestMap, "verificationCode");
        String sessionCode = findSession(Constant.SessionKey.SESSION_KEY_OF_RAND_CODE);

        boolean illegal = code == null || code.isEmpty()
                || !code.toUpperCase().equals(sessionCode.toUpperCase());
        if (illegal) {
            throw new Exception("验证码输入有误");
        }
        String serialNo = getParam(requestMap,"username");
        String password = getParam(requestMap,"password");
        String roleId = getParam(requestMap,"role");
        return customResponse("token", loginService.login(serialNo, password, roleId));
    }

    @RequestMapping(value="/getUserInfo", method=RequestMethod.POST)
    @ResponseBody
    public Object getUserInfo(@RequestBody Map<String, Object> requestMap) throws Exception {
        String serialNo = getParam(requestMap, "serialNo");
        return loginService.getUserInfo(serialNo);
    }

    @RequestMapping(value="/getRoleList", method=RequestMethod.GET)
    @ResponseBody
    public Object getRoleList() throws Exception {
        return loginService.getRoleList();
    }
}
