package edu.coursemgr.controller;

import edu.coursemgr.service.interfaces.StatMgrService;
import edu.coursemgr.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * Created by chenxianzhang on 2018/10/7 0007 下午 9:18
 */
@Controller
@RequestMapping("/stat")
public class StatMgrController extends BaseController {

    @Autowired
    private StatMgrService statMgrService;

    @RequestMapping(value="/statAllGrade", method=RequestMethod.POST)
    @ResponseBody
    public Object statAllGrade(@RequestBody Map<String, Object> requestMap) throws Exception {
        String courseId = getParam(requestMap, "courseId");
        String sort = getParam(requestMap, "sort");
        if (CommonUtils.isEmpty(courseId)) {
            throw new Exception("验证码输入有误");
        }

        return statMgrService.statAllGrade(courseId, sort);
    }
}
