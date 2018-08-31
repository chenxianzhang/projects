package edu.coursemgr.controller;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.common.Constant;
import edu.coursemgr.pojo.AssignGroupModel;
import edu.coursemgr.pojo.GroupDetail;
import edu.coursemgr.service.interfaces.GroupMgrService;
import edu.coursemgr.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by chenxianzhang on 2018/8/24 0024 上午 12:09
 */
@Controller
@RequestMapping("/groupMgr")
public class GroupMgrController extends BaseController {

    @Autowired
    private GroupMgrService groupMgrService;

    @RequestMapping(value="/randGroup", method=RequestMethod.POST)
    @ResponseBody
    public Object randGroup(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String courseId = getParam(requestMap, "courseId");
        String memberCnt = getParam(requestMap, "memberCnt");
        if (CommonUtils.isEmpty(courseId) || CommonUtils.isEmpty(memberCnt)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return groupMgrService.randGroup(courseId, memberCnt);
    }


    @RequestMapping(value="/appendStudent2Group", method=RequestMethod.POST)
    @ResponseBody
    public Object appendStudent2Group(@RequestBody Map<String, Object> requestMap)
        throws Exception {
        String groupId = getParam(requestMap, "groupId");
        String studentNo = getParam(requestMap, "studentNo");
        if (CommonUtils.isEmpty(groupId) || CommonUtils.isEmpty(studentNo)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return groupMgrService.appendStudent2Group(groupId, studentNo);
    }

    @RequestMapping(value="/assignGroup", method=RequestMethod.POST)
    @ResponseBody
    public Object assignGroup(@RequestBody AssignGroupModel assignGroupModel)
            throws Exception{
        if (null == assignGroupModel) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        if (!groupMgrService.assignGroup(assignGroupModel, CommonEnum.GroupedType.ASSIGN)) {
            throw new Exception("指定分组异常");
        }
        return true;
    }

    @RequestMapping(value="/changeGroupLeader", method=RequestMethod.POST)
    @ResponseBody
    public Object changeGroupLeader(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String groupId = getParam(requestMap, "groupId");
        String studentNo = getParam(requestMap, "studentNo");
        String studentName = getParam(requestMap, "studentName");
        boolean illegal = CommonUtils.isEmpty(groupId) || CommonUtils.isEmpty(studentNo)
                || CommonUtils.isEmpty(studentName);
        if (illegal) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return groupMgrService.changeGroupLeader(groupId, studentNo, studentName);
    }

    @RequestMapping(value="/relieveGroup", method=RequestMethod.POST)
    @ResponseBody
    public Object relieveGroup(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String groupId = getParam(requestMap, "groupId");
        String groupNo = getParam(requestMap, "groupNo");
        if (CommonUtils.isEmpty(groupId) || CommonUtils.isEmpty(groupNo)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        if (!groupMgrService.relieveGroup(groupId, groupNo)) {
            throw new Exception("解散当前组，出现异常");
        }
        return true;
    }

    @RequestMapping(value="/getGroupDetail", method=RequestMethod.POST)
    @ResponseBody
    public List<GroupDetail> getGroupDetail(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return groupMgrService.getGroupDetail(courseId);
    }

    @RequestMapping(value="/getGroupDetailByStudent", method=RequestMethod.POST)
    @ResponseBody
    public GroupDetail getGroupDetailByStudent(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String courseId = getParam(requestMap, "courseId");
        String studentNo = getParam(requestMap, "studentNo");
        if (CommonUtils.isEmpty(courseId) || CommonUtils.isEmpty(studentNo)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return groupMgrService.getGroupDetailByStudent(courseId, studentNo);
    }

    @RequestMapping(value="/checkCourseGrouped", method=RequestMethod.POST)
    @ResponseBody
    public Integer checkCourseGrouped(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return groupMgrService.checkCourseGrouped(courseId);
    }

}
