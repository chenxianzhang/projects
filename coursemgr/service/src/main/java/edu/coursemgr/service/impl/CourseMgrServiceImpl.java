package edu.coursemgr.service.impl;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.common.Constant;
import edu.coursemgr.dao.*;
import edu.coursemgr.excel.ExcelReader;
import edu.coursemgr.model.Course;
import edu.coursemgr.model.GroupMember;
import edu.coursemgr.model.User;
import edu.coursemgr.pojo.GradeDetail;
import edu.coursemgr.pojo.PageModel;
import edu.coursemgr.pojo.StudentTaskInfo;
import edu.coursemgr.pojo.UserGroup;
import edu.coursemgr.service.interfaces.CourseMgrService;
import edu.coursemgr.utils.CollectionUtils;
import edu.coursemgr.utils.CommonUtils;
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

    @Autowired
    private QuestionOptionsMapper questionOptionsMapper;

    @Autowired
    private StudentPaperMapper studentPaperMapper;

    @Autowired
    private StudentTasksMapper studentTasksMapper;

    @Autowired
    private TaskQuestionsMapper taskQuestionsMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GradeRelateMapper gradeRelateMapper;

    @Autowired
    private CourseStudentsMapper courseStudentsMapper;

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
    public PageModel getAllGradeInfo(String courseId, String pageSize,
                                     String currPage) {

        int totalCount = userMapper.selectTotalCntByCourseId(Integer.valueOf(courseId));

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("courseId", courseId);
        paramMap.put("pageSize", pageSize);
        int offset = (Integer.valueOf(currPage) - 1) * Integer.valueOf(pageSize);
        paramMap.put("currSize", offset);
        // 获取学员与其组信息
//        List<User> userList = userMapper.selectFinishedTaskStu(paramMap);
        // 获取课程下所有学员
        List<User> userList = userMapper.selectSomeByPage(paramMap);

        // 在根据学员信息获取其所有任务信息
        List<GradeDetail> gradeDetails = CollectionUtils.arrayListCast(userList,
                user -> {

                    //获取组信息
                    Map<String, Object> params = new HashMap<>(2);
                    params.put("studentNo", user.getSerialNo());
                    params.put("courseId", courseId);
                    GroupMember groupMember = groupMemberMapper.selectByMember(params);

                    GradeDetail detail = new GradeDetail();
                    if (groupMember != null) {
                        detail.setGroupNo(groupMember.getGroupNo());
                    }

                    detail.setStudentName(user.getName());
                    detail.setStudentNo(user.getSerialNo());

                    List<StudentTaskInfo> taskInfos = courseTasksMapper.selectStuTaskInfo(params);
                    if (taskInfos == null || taskInfos.size() == 0) {
                        return null;
                    }
                    detail.setStudentTaskInfos(taskInfos);
                    // 计算加权总分
                    CommonEnum.StudentTaskStatus status = null;
                    Float totalScore = 0f;
                    if (taskInfos != null) {
                        for (StudentTaskInfo taskInfo : taskInfos) {
                            if (taskInfo.getStatus() == null) {
                                taskInfo.setStatus(CommonEnum.StudentTaskStatus.UNCOMMITTED.getName());
                                continue;
                            }
                            status = CommonEnum.StudentTaskStatus.valueOf(taskInfo.getStatus());
                            taskInfo.setStatus(status.getName());
                            if (taskInfo.getScore() == null) {
                                continue;
                            }
                            totalScore += taskInfo.getScore() * taskInfo.getTaskWeight() / 100;
                        }
                    }
                    detail.setTotalScore(totalScore);
                    return detail;
                });

        PageModel pageModel = new PageModel();
        pageModel.setTotalCount(totalCount);
        pageModel.setPageData(gradeDetails);

        return pageModel;
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
            CommonEnum.StudentTaskStatus status = null;
            if (taskInfos != null) {
                for (StudentTaskInfo taskInfo : taskInfos) {
                    if (taskInfo.getStatus() == null) {
                        taskInfo.setStatus(CommonEnum.StudentTaskStatus.UNCOMMITTED.getName());
                        continue;
                    }
                    status = CommonEnum.StudentTaskStatus.valueOf(taskInfo.getStatus());
                    taskInfo.setStatus(status.getName());
                    if (taskInfo.getScore() == null) {
                        continue;
                    }
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
        List<GradeDetail> gradeDetailList = getGradeDetailList(courseId);

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
            if (detail.getStudentTaskInfos() == null ||
                    detail.getStudentTaskInfos().size() == 0) {
                continue;
            }
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
            flag = true;
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

    @Override
    public boolean deleteCourseByTeacherNo(String teacherNo) {
        // 获取所有的课程信息
        List<Course> courseList = courseMapper.selectTeacherCourse(teacherNo);
        if (courseList == null || courseList.size() == 0) {
            return true;
        }
        List<Integer> courseIdList = CollectionUtils.arrayListCast(courseList,
                course -> course.getId());

        return deleteCourseList(courseIdList);
    }

    @Override
    public List<Course> getCourseByUser(String role, String serialNo) {

        if (CommonEnum.Role.STUDENT.getValue().equals(role)) {
            return courseMapper.selectTeacherCourse(serialNo);
        } else {
            return courseMapper.selectStuCourse(serialNo);
        }
    }

    private boolean deleteCourseList(List<Integer> courseIdList) {

        String courseIds = CommonUtils.join(courseIdList, Constant.Common.SEPARATE_COMMA);
        // question_options
        questionOptionsMapper.deleteByCourseIds(courseIds);

        // student_paper
        studentPaperMapper.deleteByCourseIds(courseIds);

        // student_tasks
        studentTasksMapper.deleteByCourseIds(courseIds);

        // task_questions
        taskQuestionsMapper.deleteByCourseIds(courseIds);

        // group_member
        groupMemberMapper.deleteByCourseIds(courseIds);

        // group
        groupMapper.deleteByCourseIds(courseIds);

        // grade_relate
        gradeRelateMapper.deleteByCourseIds(courseIds);

        // course_tasks
        courseTasksMapper.deleteByCourseIds(courseIds);

        // course_students
        courseStudentsMapper.deleteByCourseIds(courseIds);

        // course
        courseMapper.deleteByCourseIds(courseIds);

        return true;
    }

    private List<GradeDetail> getGradeDetailList(String courseId) {
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
