package edu.coursemgr.controller;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.common.Constant;
import edu.coursemgr.excel.ExcelReader;
import edu.coursemgr.excel.ExcelUtil;
import edu.coursemgr.model.User;
import edu.coursemgr.pojo.UserEditModel;
import edu.coursemgr.service.interfaces.CourseMgrService;
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

    @Autowired
    private CourseMgrService courseMgrService;

    @RequestMapping(value="/getStudentsByCourseId", method=RequestMethod.POST)
    @ResponseBody
    public Object getStudentsByCourseId(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String courseId = getParam(requestMap, "courseId");
        String pageSize = getParam(requestMap, "pageSize");
        String currPage = getParam(requestMap, "currPage");
        boolean illegal = CommonUtils.isEmpty(courseId) || CommonUtils.isEmpty(pageSize)
                || CommonUtils.isEmpty(currPage);
        if (illegal) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return userMgrService.getStudentsByCourseId(courseId, pageSize, currPage);
    }

    @RequestMapping(value="/getUserByRole", method=RequestMethod.POST)
    @ResponseBody
    public Object getUserByRole(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String role = getParam(requestMap, "role");
        String nameOrNo = getParam(requestMap, "nameOrNo");
        String pageSize = getParam(requestMap, "pageSize");
        String currPage = getParam(requestMap, "currPage");
        boolean illegal = CommonUtils.isEmpty(role) || CommonUtils.isEmpty(pageSize)
                || CommonUtils.isEmpty(currPage);
        if (illegal) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return userMgrService.getUserByRole(role, pageSize, currPage, nameOrNo);
    }

    @RequestMapping(value="/getAllStudentsByCourseId", method=RequestMethod.GET)
    @ResponseBody
    public Object getAllStudentsByCourseId(@RequestParam String courseId)
            throws Exception {
        if (CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return userMgrService.getStudentList(courseId);
    }

    @RequestMapping(value="/addStudent", method=RequestMethod.POST)
    @ResponseBody
    public int addStudent(@RequestBody UserEditModel user)
            throws Exception {
        if (user == null || CommonUtils.isEmpty(user.getCourseId())) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return userMgrService.addStudent(user);
    }

    @RequestMapping(value="/addUser", method=RequestMethod.POST)
    @ResponseBody
    public int addUser(@RequestBody User user)
            throws Exception {
        if (user == null) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return userMgrService.addUser(user);
    }

    @RequestMapping(value="/updateUser", method=RequestMethod.POST)
    @ResponseBody
    public int updateUser(@RequestBody User user)
            throws Exception {
        if (user == null) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return userMgrService.updateUser(user);
    }

    @RequestMapping(value="/resetPwd", method=RequestMethod.GET)
    @ResponseBody
    public int resetPwd(@RequestParam String serialNo)
            throws Exception {
        if (CommonUtils.isEmpty(serialNo)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        User user = new User();
        user.setSerialNo(serialNo);
        user.setPassword(serialNo);
        return userMgrService.updateUser(user);
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

    @RequestMapping(value="/deleteUser", method=RequestMethod.POST)
    @ResponseBody
    public Object deleteUser(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String serialNo = getParam(requestMap, "serialNo");
        String role = getParam(requestMap, "role");
        String levelPwd = getParam(requestMap, "levelPwd");
        boolean illegal = CommonUtils.isEmpty(serialNo) ||
                CommonUtils.isEmpty(levelPwd) || CommonUtils.isEmpty(role);
        if (illegal) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        // 判断而二级密码是否OK
        User user = userMgrService.checkLevelPwd(serialNo, levelPwd);

        // 删除教师用户
        if (CommonEnum.Role.TEACHER.getValue().equals(role)) {
            courseMgrService.deleteCourseByTeacherNo(serialNo);
        } else {
            // 学生用户
            userMgrService.deleteStudent(serialNo);
        }
        return userMgrService.deleteUser(user);
    }

}
