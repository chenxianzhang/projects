package edu.coursemgr.service.impl;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.dao.CourseMapper;
import edu.coursemgr.dao.CourseTasksMapper;
import edu.coursemgr.dao.GroupMemberMapper;
import edu.coursemgr.dao.UserMapper;
import edu.coursemgr.excel.ExcelReader;
import edu.coursemgr.model.Course;
import edu.coursemgr.model.GroupMember;
import edu.coursemgr.model.User;
import edu.coursemgr.pojo.GradeDetail;
import edu.coursemgr.pojo.StudentTaskInfo;
import edu.coursemgr.pojo.UserGroup;
import edu.coursemgr.service.interfaces.CourseMgrService;
import edu.coursemgr.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;

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

    @Autowired
    private GroupMemberMapper groupMemberMapper;

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

    @Override
    public int updateCourseGroupType(Course course, CommonEnum.GroupedType type) {

        course.setGroupingType(type.getName());

        return courseMapper.updateByIdSelective(course);
    }

    @Override
    public GradeDetail getStuGradeInfo(String courseId, String studentNo) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("courseId", courseId);
        params.put("studentNo", studentNo);
        GroupMember groupMember = groupMemberMapper.selectByMember(params);

        if (groupMember != null) {
            GradeDetail detail = new GradeDetail();
            detail.setGroupNo(groupMember.getGroupNo());
            detail.setStudentName(groupMember.getStudentName());
            detail.setStudentNo(groupMember.getStudentNo());

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
        }

        return null;
    }

    @Override
    public void exportCourseGrade(String courseId, HttpServletResponse response)
            throws Exception {
        List<GradeDetail> gradeDetailList = getAllGradeInfo(courseId);

        if(gradeDetailList == null) {
            throw new Exception("课程成绩信息为空");
        }

        // 获取列
        List<String> columnList = new ArrayList<>();
        columnList.add("姓名");
        columnList.add("学号");
        columnList.add("所在小组");

        List<ArrayList<String>> dataList = new ArrayList<>();
        boolean flag = false;
        for (GradeDetail detail : gradeDetailList) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(detail.getStudentName());
            arrayList.add(detail.getStudentNo());
            arrayList.add(detail.getGroupNo().toString());

            for (StudentTaskInfo taskInfo : detail.getStudentTaskInfos()) {
                if (!flag) {
                    columnList.add(String.format("%s(%s%%)", taskInfo.getTaskName(),
                            taskInfo.getTaskWeight().toString()));
                }
                arrayList.add(taskInfo.getScore().toString());
            }

            arrayList.add(detail.getTotalScore().toString());
            dataList.add(arrayList);
        }
        columnList.add("加权总分");
        Course course = courseMapper.selectById(Integer.valueOf(courseId));

        ExcelReader excelReader = new ExcelReader();
        String name = String.format("%s成绩汇总", course.getName());
        excelReader.export(name, name, columnList, dataList, name, response);
    }

    @Override
    public void exportStuGrade(String courseId, String studentNo,
                               HttpServletResponse response) throws Exception {
        GradeDetail detail = getStuGradeInfo(courseId, studentNo);

        if (detail == null) {
            throw new Exception("学生成绩信息为空");
        }

        // 获取列
        List<String> columnList = new ArrayList<>();
        columnList.add("姓名");
        columnList.add("学号");
        columnList.add("所在小组");

        List<ArrayList<String>> dataList = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(detail.getStudentName());
        arrayList.add(detail.getStudentNo());
        arrayList.add(detail.getGroupNo().toString());
        for (StudentTaskInfo taskInfo : detail.getStudentTaskInfos()) {
            columnList.add(String.format("%s(%s%%)", taskInfo.getTaskName(),
                    taskInfo.getTaskWeight().toString()));
            arrayList.add(taskInfo.getScore().toString());
        }
        arrayList.add(detail.getTotalScore().toString());
        dataList.add(arrayList);
        columnList.add("加权总分");

        Course course = courseMapper.selectById(Integer.valueOf(courseId));
        User user = userMapper.selectBySerialNo(studentNo);

        String name = String.format("%s%s成绩信息", user.getName(), course.getName());
        ExcelReader excelReader = new ExcelReader();
        excelReader.export(name, name, columnList, dataList, name, response);
    }


}
