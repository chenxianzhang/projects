package edu.coursemgr.controller;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.common.Constant;
import edu.coursemgr.model.Course;
import edu.coursemgr.pojo.GradeDetail;
import edu.coursemgr.service.interfaces.CourseMgrService;
import edu.coursemgr.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by chenxianzhang on 2018/8/16 0016 上午 12:16
 */
@Controller
@RequestMapping("/course")
public class CourseMgrController extends BaseController {

    @Autowired
    private CourseMgrService courseMgrService;

    @RequestMapping(value="/selectTeacherCourse", method=RequestMethod.POST)
    @ResponseBody
    public Object getTeacherCourseList(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String teacherNo = getParam(requestMap, "teacherNo");
        if (CommonUtils.isEmpty(teacherNo)) {
            throw new Exception("用户登录超时，请重新登录");
        }
        return courseMgrService.getTeacherCourseList(teacherNo);
    }

    @RequestMapping(value="/saveCourse", method=RequestMethod.POST)
    @ResponseBody
    public Object saveCourse(@RequestBody Map<String, Object> requestMap) {
        String name = getParam(requestMap, "name");
        String credit = getParam(requestMap, "credit");
        String description = getParam(requestMap, "description");
        String userNo = getParam(requestMap, "userNo");

        return courseMgrService.saveCourse(name, credit, description, userNo);
    }

    @RequestMapping(value="/selectStuCourse", method=RequestMethod.POST)
    @ResponseBody
    public Object getStuCourseList(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String studentNo = getParam(requestMap, "studentNo");
        if (CommonUtils.isEmpty(studentNo)) {
            throw new Exception(Constant.ExceptionMessage.LOGIN_TIMEOUT);
        }
        return courseMgrService.getStuCourseList(studentNo);
    }

    @RequestMapping(value="/getCourseById", method=RequestMethod.POST)
    @ResponseBody
    public Course getCourseById(@RequestBody Map<String, Object> requestMap)
        throws Exception {
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return courseMgrService.getCourseById(courseId);
    }

    @RequestMapping(value="/updateCourseSelective", method=RequestMethod.POST)
    @ResponseBody
    public int updateCourseSelective(@RequestBody Course course) throws Exception {
        if (course == null) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EXCEPTION);
        }
        return courseMgrService.updateCourse(course, true);
    }

    @RequestMapping(value="/updateCourse", method=RequestMethod.POST)
    @ResponseBody
    public int updateCourse(@RequestBody Course course) throws Exception {
        if (course == null) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EXCEPTION);
        }
        return courseMgrService.updateCourse(course, false);
    }

    @RequestMapping(value="/getAllGradeInfo", method=RequestMethod.POST)
    @ResponseBody
    public List<GradeDetail> getAllGradeInfo(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return courseMgrService.getAllGradeInfo(courseId);
    }

    @RequestMapping(value="/updateFreedomGroup", method=RequestMethod.POST)
    @ResponseBody
    public int updateFreedomGroup(@RequestBody Course course) throws Exception {
        if (null == course) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        if (courseMgrService.updateCourseGroupType(course,
                CommonEnum.GroupedType.FREEDOM) == 0) {
            throw new Exception("修改为自由分组出错");
        }
        return 1;
    }

    @RequestMapping(value="/getStuGradeInfo", method=RequestMethod.POST)
    @ResponseBody
    public GradeDetail getStuGradeInfo(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String courseId = getParam(requestMap, "courseId");
        String studentNo = getParam(requestMap, "studentNo");
        if (CommonUtils.isEmpty(courseId) || CommonUtils.isEmpty(studentNo)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return courseMgrService.getStuGradeInfo(courseId, studentNo);
    }

    @RequestMapping(value="/exportCourseGrade", method=RequestMethod.POST)
    @ResponseBody
    public void exportCourseGrade(@RequestBody Map<String, Object> requestMap,
                                  HttpServletResponse response)
            throws Exception {
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        courseMgrService.exportCourseGrade(courseId, response);
    }
}
