package edu.coursemgr.dao;

import edu.coursemgr.model.CourseTasks;
import edu.coursemgr.pojo.CourseTaskSituation;
import edu.coursemgr.pojo.StudentTaskInfo;
import edu.coursemgr.pojo.TaskStatusMarkModel;

import java.util.List;
import java.util.Map;

public interface CourseTasksMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CourseTasks record);

    int insertSelective(CourseTasks record);

    CourseTasks selectByTaskId(Integer id);

    CourseTasks selectByTaskName(Map<String, Object> params);

    int updateByIdSelective(CourseTasks record);

    int updateByPrimaryKey(CourseTasks record);

    List<CourseTasks> selectSomeByCourseId(Integer courseId);

    List<CourseTaskSituation> getCourseTaskSituation(Integer courseId);

    List<StudentTaskInfo> selectStuTaskInfo(Map<String, Object> params);

    List<CourseTasks> selectSomeByMarkType(Map<String, Object> params);

    int deleteTask(Integer taskId);

    int deleteByCourseIds(String courseIds);

    TaskStatusMarkModel selectTaskStatusMark(Map params);
}