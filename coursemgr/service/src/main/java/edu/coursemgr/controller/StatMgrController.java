package edu.coursemgr.controller;

import edu.coursemgr.common.Constant;
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
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return statMgrService.statAllGrade(courseId, sort);
    }

    @RequestMapping(value="/statTaskSubmitReview", method=RequestMethod.POST)
    @ResponseBody
    public Object statTaskSubmitReview(@RequestBody Map<String, Object> requestMap) throws Exception {
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return statMgrService.statTaskSubmitReview(courseId);
    }

    @RequestMapping(value="/statQuestionTypeCnt", method=RequestMethod.POST)
    @ResponseBody
    public Object statQuestionTypeCnt(@RequestBody Map<String, Object> requestMap) throws Exception {
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return statMgrService.statQuestionTypeCnt(courseId);
    }

    @RequestMapping(value="/statStudentTaskScore", method=RequestMethod.POST)
    @ResponseBody
    public Object statStudentTaskScore(@RequestBody Map<String, Object> requestMap) throws Exception {
        String courseId = getParam(requestMap, "courseId");
        String studentNo = getParam(requestMap, "studentNo");
        if (CommonUtils.isEmpty(courseId) || CommonUtils.isEmpty(studentNo)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return statMgrService.statStudentTaskScore(courseId, studentNo);
    }

    @RequestMapping(value="/statStuSynthesizeInfo", method=RequestMethod.POST)
    @ResponseBody
    public Object statStuSynthesizeInfo(@RequestBody Map<String, Object> requestMap) throws Exception {
        String courseId = getParam(requestMap, "courseId");
        String studentNo = getParam(requestMap, "studentNo");
        if (CommonUtils.isEmpty(courseId) || CommonUtils.isEmpty(studentNo)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return statMgrService.statStuSynthesizeInfo(courseId, studentNo);
    }

}
