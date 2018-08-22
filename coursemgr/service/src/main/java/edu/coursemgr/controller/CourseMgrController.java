package edu.coursemgr.controller;

import edu.coursemgr.common.Constant;
import edu.coursemgr.pojo.CourseTaskDetail;
import edu.coursemgr.service.interfaces.CourseMgrService;
import edu.coursemgr.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value="/saveTask", method=RequestMethod.POST)
    @ResponseBody
    public Object saveTask(@RequestBody CourseTaskDetail taskDetail) throws Exception {
        if (taskDetail == null) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EXCEPTION);
        }
        return courseMgrService.saveTask(taskDetail);
    }

    @RequestMapping(value="/getTaskDetailByTaskId", method=RequestMethod.POST)
    @ResponseBody
    public CourseTaskDetail getTaskDetailByTaskId(@RequestBody Map<String, Object> requestMap)
        throws Exception {
        String taskId = getParam(requestMap, "taskId");
        if (CommonUtils.isEmpty(taskId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return courseMgrService.getTaskDetailByTaskId(Integer.valueOf(taskId));
    }
}
