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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
    public Object batchUploadStudents(@RequestParam(value="excelFile") MultipartFile file,
                                 HttpServletRequest request, HttpSession session)
            throws Exception {
        //判断文件是否为空
        if(file == null){
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        String name = file.getOriginalFilename();
        long size = file.getSize();
        if(name == null || ExcelUtil.EMPTY.equals(name) && size==0){
            throw new Exception(Constant.ExceptionMessage.EXCEL_EMPTY);
        }
        //读取Excel数据到List中
        List<ArrayList<String>> list = new ExcelReader().readExcel(file);
        //list中存的就是excel中的数据，可以根据excel中每一列的值转换成你所需要的值（从0开始）

        return true;
    }

}
