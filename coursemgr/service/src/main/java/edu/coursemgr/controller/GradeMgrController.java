package edu.coursemgr.controller;

import edu.coursemgr.common.Constant;
import edu.coursemgr.pojo.SubjectGradeModel;
import edu.coursemgr.service.interfaces.GradeMgrService;
import edu.coursemgr.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value="/getScheduleByStudent", method=RequestMethod.POST)
    @ResponseBody
    public Object getScheduleByStudent(@RequestBody Map<String, Object> requestMap)
            throws Exception {

        String studentNo = getParam(requestMap, "studentNo");
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(studentNo) || CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return gradeMgrService.getScheduleByStudent(studentNo, courseId);

    }

    @RequestMapping(value="/handOverSchedule", method=RequestMethod.POST)
    @ResponseBody
    public Object handOverSchedule(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String courseId = getParam(requestMap, "courseId");
        String originStudentNo = getParam(requestMap, "originStudentNo");
        String dstStudentNo = getParam(requestMap, "dstStudentNo");
        if (CommonUtils.isEmpty(originStudentNo) ||
                CommonUtils.isEmpty(dstStudentNo) || CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        gradeMgrService.handOverSchedule(courseId, originStudentNo, dstStudentNo);

        return Constant.Common.SUCCESS;

    }

}
