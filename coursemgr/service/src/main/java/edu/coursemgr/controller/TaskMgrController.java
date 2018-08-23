package edu.coursemgr.controller;

import edu.coursemgr.common.Constant;
import edu.coursemgr.pojo.CourseTaskDetail;
import edu.coursemgr.service.interfaces.TaskMgrService;
import edu.coursemgr.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
