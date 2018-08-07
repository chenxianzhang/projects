package edu.coursemgr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
    @RequestMapping("/login")
public class LoginController {

    @RequestMapping(value="/login", method=RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object login(@RequestBody Map<String, Object> requestMap) {
        int result = 3;
        return result;
    }
}
