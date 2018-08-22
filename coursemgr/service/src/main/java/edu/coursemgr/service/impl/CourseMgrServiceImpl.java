package edu.coursemgr.service.impl;

import edu.coursemgr.common.Constant;
import edu.coursemgr.dao.CourseMapper;
import edu.coursemgr.dao.CourseTasksMapper;
import edu.coursemgr.dao.TaskQuestionsMapper;
import edu.coursemgr.model.Course;
import edu.coursemgr.model.CourseTasks;
import edu.coursemgr.model.TaskQuestions;
import edu.coursemgr.pojo.CourseTaskDetail;
import edu.coursemgr.service.interfaces.CourseMgrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/16 0016 下午 10:43
 */
@Service
public class CourseMgrServiceImpl implements CourseMgrService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseTasksMapper courseTasksMapper;

    @Autowired
    private TaskQuestionsMapper taskQuestionsMapper;


    @Override
    public List<Course> getTeacherCourseList(String teacherNo) {
        return courseMapper.selectTeacherCourse(teacherNo);
    }

    @Override
    public int saveCourse(String name, String credit, String description,
                              String userNo) {
        Course course = new Course();
        course.setName(name);
        course.setCredit(Integer.valueOf(credit));
        course.setDescription(description);
        course.setCreateDate(new Date());
//        course.setGroupingType();
        course.setCreatorNo(userNo);

        return courseMapper.insert(course);
    }

    @Override
    public List<Course> getStuCourseList(String stuNo) {
        return courseMapper.selectStuCourse(stuNo);
    }

    @Override
    public int saveTask(CourseTaskDetail taskDetail) throws Exception {
        boolean illegal = taskDetail.getTask() == null || taskDetail.getQuestionList() == null;
        if (illegal) {
            throw new Exception(Constant.ExceptionMessage.PARAM_EXCEPTION);
        }
        // 保存task任务信息，并返回任务id
        int recordCnt = courseTasksMapper.insert(taskDetail.getTask());

        illegal = recordCnt == 0 || taskDetail.getTask().getId() == null;
        if (illegal) {
            throw new Exception(Constant.ExceptionMessage.DATA_SAVE_EXCEPTION);
        }

        taskDetail.getQuestionList().forEach(question -> {
            question.setTaskId(taskDetail.getTask().getId());
        });

        return taskQuestionsMapper.insertBatch(taskDetail.getQuestionList());
    }

    @Override
    public CourseTaskDetail getTaskDetailByTaskId(Integer taskId) throws Exception {

        // 获取任务基本信息
        CourseTasks task = courseTasksMapper.selectByTaskId(taskId);
        if (task == null) {
            throw new Exception(Constant.ExceptionMessage.DATA_QUERY_EXCEPTION);
        }
        // 获取任务下的所有试题信息
        List<TaskQuestions> questionsList = taskQuestionsMapper.selectSomeByTaskId(taskId);

        if (questionsList == null) {
            throw new Exception(Constant.ExceptionMessage.DATA_QUERY_EXCEPTION);
        }

        CourseTaskDetail taskDetail = new CourseTaskDetail();
        taskDetail.setTask(task);
        taskDetail.setQuestionList(questionsList);
        return taskDetail;
    }
}
