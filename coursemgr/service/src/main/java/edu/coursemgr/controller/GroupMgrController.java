package edu.coursemgr.controller;

import edu.coursemgr.common.Constant;
import edu.coursemgr.service.interfaces.GroupMgrService;
import edu.coursemgr.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by chenxianzhang on 2018/8/24 0024 上午 12:09
 */
@Controller
@RequestMapping("/groupMgr")
public class GroupMgrController extends BaseController {

    @Autowired
    private GroupMgrService groupMgrService;

    @RequestMapping(value="/freeGroup", method=RequestMethod.POST)
    @ResponseBody
    public Object freeGroup(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String courseId = getParam(requestMap, "courseId");
        String memberCnt = getParam(requestMap, "memberCnt");
        if (CommonUtils.isEmpty(courseId) || CommonUtils.isEmpty(memberCnt)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return groupMgrService.freeGroup(courseId, memberCnt);
    }


    @RequestMapping(value="/appendStudent2Group", method=RequestMethod.POST)
    @ResponseBody
    public Object appendStudent2Group(@RequestBody Map<String, Object> requestMap) {
        return null;
    }

}
