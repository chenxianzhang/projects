package edu.coursemgr.controller;

import edu.coursemgr.common.Constant;
import edu.coursemgr.pojo.SubjectGradeModel;
import edu.coursemgr.service.interfaces.GradeMgrService;
import edu.coursemgr.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by chenxianzhang on 2018/8/31 0031 下午 9:53
 */
@Controller
@RequestMapping("/gradeMgr")
public class GradeMgrController extends BaseController {

    @Autowired
    private GradeMgrService gradeMgrService;

    @RequestMapping(value="/getMySchedule", method=RequestMethod.POST)
    @ResponseBody
    public Object getMySchedule(@RequestBody Map<String, Object> requestMap)
            throws Exception {

        String courseId = getParam(requestMap, "courseId");
        String studentNo = getParam(requestMap, "studentNo");
        if (CommonUtils.isEmpty(courseId) || CommonUtils.isEmpty(studentNo)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return gradeMgrService.getMySchedule(courseId, studentNo);

    }

    @RequestMapping(value="/updateSubjectScore", method=RequestMethod.POST)
    @ResponseBody
    public Object updateSubjectScore(@RequestBody SubjectGradeModel subjectGradeModel)
            throws Exception {
        if (subjectGradeModel == null) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return gradeMgrService.updateSubjectScore(subjectGradeModel);
    }

    @RequestMapping(value="/getSubjectGradePerson", method=RequestMethod.POST)
    @ResponseBody
    public Object getSubjectGradePerson(@RequestBody Map<String, Object> requestMap)
            throws  Exception {

        String taskId = getParam(requestMap, "taskId");
        String studentNo = getParam(requestMap, "studentNo");
        if (CommonUtils.isEmpty(taskId) || CommonUtils.isEmpty(studentNo)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return gradeMgrService.getSubjectGradePerson(taskId, studentNo);
    }

}
