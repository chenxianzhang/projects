package edu.coursemgr.controller;

import edu.coursemgr.common.Constant;
import edu.coursemgr.model.CourseTasks;
import edu.coursemgr.pojo.*;
import edu.coursemgr.service.interfaces.TaskMgrService;
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
 * Created by chenxianzhang on 2018/8/24 0024 上午 12:13
 */
@Controller
@RequestMapping("/taskMgr")
public class TaskMgrController extends BaseController {

    @Autowired
    private TaskMgrService taskMgrService;

    @RequestMapping(value="/saveTask", method=RequestMethod.POST)
    @ResponseBody
    public Object saveTask(@RequestBody CourseTaskDetail taskDetail) throws Exception {
        if (taskDetail == null) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EXCEPTION);
        }
        return taskMgrService.saveTask(taskDetail);
    }


    @RequestMapping(value="/getTaskDetailByTaskId", method=RequestMethod.POST)
    @ResponseBody
    public CourseTaskDetail getTaskDetailByTaskId(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String taskId = getParam(requestMap, "taskId");
        if (CommonUtils.isEmpty(taskId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return taskMgrService.getTaskDetailByTaskId(Integer.valueOf(taskId));
    }

    @RequestMapping(value="/getCourseTaskById", method=RequestMethod.POST)
    @ResponseBody
    public CourseTasks getCourseTaskById(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String taskId = getParam(requestMap, "taskId");
        if (CommonUtils.isEmpty(taskId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return taskMgrService.getCourseTaskById(taskId);
    }

    @RequestMapping(value="/getCourseTasksByCourseId", method=RequestMethod.POST)
    @ResponseBody
    public List<CourseTasks> getCourseTasksByCourseId(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return taskMgrService.getCourseTasksByCourseId(courseId);
    }

    @RequestMapping(value="/getStudentTaskSituation", method=RequestMethod.POST)
    @ResponseBody
    public List<StudentTaskDetail> getStudentTaskSituation(
            @RequestBody Map<String, Object> requestMap) throws Exception {
        String taskId = getParam(requestMap, "taskId");
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(taskId) || CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return taskMgrService.getStudentTaskSituation(taskId, courseId);
    }

    @RequestMapping(value="/getCourseTaskSituation", method=RequestMethod.POST)
    @ResponseBody
    public List<CourseTaskSituation> getCourseTaskSituation(
            @RequestBody Map<String, Object> requestMap) throws Exception {
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return taskMgrService.getCourseTaskSituation(courseId);
    }

    @RequestMapping(value="/getMyTaskSituation", method=RequestMethod.POST)
    @ResponseBody
    public List<StudentTaskSituation> getMyTaskSituation(
            @RequestBody Map<String, Object> requestMap) throws Exception {

        String courseId = getParam(requestMap, "courseId");
        String studentNo = getParam(requestMap, "studentNo");
        if (CommonUtils.isEmpty(courseId) || CommonUtils.isEmpty(studentNo)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return taskMgrService.getMyTaskSituation(courseId, studentNo);
    }

    @RequestMapping(value="/submitTaskPaper", method=RequestMethod.POST)
    @ResponseBody
    public Object submitTaskPaper(@RequestBody StudentPaperAnswer studentPaperAnswer)
            throws Exception {
        if (null == studentPaperAnswer) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }

        return taskMgrService.submitTaskPaper(studentPaperAnswer);
    }

    @RequestMapping(value="/deleteTask", method=RequestMethod.POST)
    @ResponseBody
    public Object deleteTask(@RequestBody Map<String, Object> requestMap)
            throws Exception {
        String taskId = getParam(requestMap, "taskId");
        String courseId = getParam(requestMap, "courseId");
        if (CommonUtils.isEmpty(taskId) || CommonUtils.isEmpty(courseId)) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        return taskMgrService.deleteTask(courseId, taskId);
    }

}
