package edu.coursemgr.service.interfaces;

import edu.coursemgr.pojo.CourseTaskDetail;

/**
 * Created by chenxianzhang on 2018/8/24 0024 上午 12:13
 */
public interface TaskMgrService {

    /**
     * 保存task详细信息
     * @param taskDetail
     * @return
     */
    int saveTask(CourseTaskDetail taskDetail) throws Exception;

    /**
     * 根据任务id获取当前任务详细信息
     * @param taskId
     * @return
     */
    CourseTaskDetail getTaskDetailByTaskId(Integer taskId) throws Exception;

}
