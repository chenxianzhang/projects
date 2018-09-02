package edu.coursemgr.controller;

import edu.coursemgr.common.Constant;
import edu.coursemgr.excel.ExcelReader;
import edu.coursemgr.excel.ExcelUtil;
import edu.coursemgr.model.User;
import edu.coursemgr.pojo.UserEditModel;
import edu.coursemgr.service.interfaces.UserMgrService;
import edu.coursemgr.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by chenxianzhang on 2018/8/23 0023 下午 10:55
 */
@Controller
@RequestMapping("/userMgr")
public class UserMgrController extends BaseController {

    @Autowired
    private UserMgrService userMgrService;

    @RequestMapping(value="/getStudentsByCourseId", method=RequestMethod.POST)
    @ResponseBody
    public List<User> getStudentsByCourseId(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return userMgrService.getStudentsByCourseId(courseId);
    }

    @RequestMapping(value="/addStudent", method=RequestMethod.POST)
    @ResponseBody
    public int addStudent(@RequestBody UserEditModel user)
            throws Exception {
        if (user == null) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return userMgrService.addStudent(user);
    }

    @RequestMapping("/batchUploadStudents")
    @ResponseBody
    public Object batchUploadStudents(HttpServletRequest request)
            throws Exception {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator iterator = multipartRequest.getFileNames();
        String courseId = multipartRequest.getParameter("courseId");
        if (CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        while (iterator.hasNext()) {
            String file = iterator.next().toString();
            userMgrService.importStudents(multipartRequest.getFile(file),
                    courseId);
        }
        return true;
    }

    @RequestMapping(value="/getNoGroupStuList", method=RequestMethod.POST)
    @ResponseBody
    public List<User> getNoGroupStuList(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return userMgrService.getNoGroupStuList(courseId);
    }

    @RequestMapping(value="/deleteStudent", method=RequestMethod.POST)
    @ResponseBody
    public Object deleteStudent(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String studentNo = getParam(requestMap, "studentNo");
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(studentNo) || CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return userMgrService.deleteStudent(courseId, studentNo);
    }

}
