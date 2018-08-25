package edu.coursemgr.service.impl;

import edu.coursemgr.dao.CourseMapper;
import edu.coursemgr.dao.CourseTasksMapper;
import edu.coursemgr.dao.UserMapper;
import edu.coursemgr.model.Course;
import edu.coursemgr.pojo.GradeDetail;
import edu.coursemgr.pojo.StudentTaskInfo;
import edu.coursemgr.pojo.UserGroup;
import edu.coursemgr.service.interfaces.CourseMgrService;
import edu.coursemgr.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenxianzhang on 2018/8/16 0016 下午 10:43
 */
@Service
@Transactional
public class CourseMgrServiceImpl implements CourseMgrService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseTasksMapper courseTasksMapper;

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
    public Course getCourseById(String courseId) {

        Integer id = Integer.valueOf(courseId);
        return courseMapper.selectById(id);
    }

    @Override
    public int updateCourse(Course course, boolean isSelective) {
        if (isSelective) {
            return courseMapper.updateByIdSelective(course);
        }
        return courseMapper.updateByIdWithBLOBs(course);
    }

    @Override
    public List<GradeDetail> getAllGradeInfo(String courseId) {

        // 获取学员与其组信息
        List<UserGroup> userGroups = userMapper.selectUserGroup(Integer.valueOf(courseId));

        // 在根据学员信息获取其所有任务信息
        List<GradeDetail> gradeDetails = CollectionUtils.arrayListCast(userGroups,
                userGroup -> {
                    GradeDetail detail = new GradeDetail();
                    detail.setGroupNo(userGroup.getGroupNo());
                    detail.setStudentName(userGroup.getStudentName());
                    detail.setStudentNo(userGroup.getStudentNo());
                    Map<String, Object> params = new HashMap<>(2);
                    params.put("studentNo", userGroup.getStudentNo());
                    params.put("courseId", courseId);
                    List<StudentTaskInfo> taskInfos = courseTasksMapper.selectStuTaskInfo(params);
                    detail.setStudentTaskInfos(taskInfos);
                    // 计算加权总分
                    Float totalScore = 0f;
                    if (taskInfos != null) {
                        for (StudentTaskInfo taskInfo : taskInfos) {
                            totalScore += taskInfo.getScore() * taskInfo.getTaskWeight() / 100;
                        }
                    }
                    detail.setTotalScore(totalScore);
                    return detail;
                });

        return gradeDetails;
    }
}
