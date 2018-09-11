package edu.coursemgr.service.impl;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.dao.*;
import edu.coursemgr.model.*;
import edu.coursemgr.pojo.Schedule;
import edu.coursemgr.pojo.SubjectGradeModel;
import edu.coursemgr.service.interfaces.GradeMgrService;
import edu.coursemgr.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenxianzhang on 2018/9/1 0001 下午 9:17
 */
@Service
@Transactional
public class GradeMgrServiceImpl implements GradeMgrService {

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GradeRelateMapper gradeRelateMapper;

    @Autowired
    private CourseTasksMapper courseTasksMapper;

    @Autowired
    private StudentPaperMapper studentPaperMapper;

    @Autowired
    private StudentTasksMapper studentTasksMapper;

    @Override
    public boolean updateGroupMemberGradeObj(Integer courseId, String markType, CourseTasks task) {
        if (markType.equals(CommonEnum.GradeType.SELF_EVA.getValue())) {
            return true;
        }
        List<GradeRelate> resultList = updateMemberGradeObj(
                courseId, markType);
        if (resultList != null) {
            for (GradeRelate relate : resultList) {
                User user = userMapper.selectBySerialNo(relate.getGradeObjNo());
                if (user != null) {
                    relate.setGradeObjName(user.getName());
                }
                relate.setTaskId(task.getId());
                relate.setTaskName(task.getName());
            }


            // 删除存在的评分关系
            gradeRelateMapper.deleteByTaskId(task.getId());

            //插入新的数据
            insertBatch(resultList);
        }
        return true;
    }

    @Override
    public List<Schedule> getMySchedule(String courseId, String studentNo) {
        User user = userMapper.selectBySerialNo(studentNo);
        if (user == null) {
            return null;
        }
        // 获取所有主观题为自评模式的任务
        Map<String, Object> params = new HashMap<>();
        params.put("courseId", courseId);
        params.put("studentNo", studentNo);
        params.put("markType", CommonEnum.GradeType.SELF_EVA.getValue());
        List<CourseTasks> taskList = courseTasksMapper.selectSomeByMarkType(params);

        List<Schedule> resultList = null;
        final String studentName = user.getName();
        if (taskList != null) {
            resultList = CollectionUtils.arrayListCast(taskList, task -> {

                Schedule schedule = new Schedule();
                schedule.setMarkPersonName(studentName);
                schedule.setMarkPersonSerialNo(studentNo);

                schedule.setTargetSerialName(studentName);
                schedule.setTargetSerialNo(studentNo);
                schedule.setTaskId(task.getId());
                schedule.setTaskName(task.getName());
                return schedule;
            });
        }

        params = new HashMap<>();
        params.put("courseId", courseId);
        params.put("studentNo", studentNo);

        List<GradeRelate> tmpList = gradeRelateMapper.selectSomeByStudent(params);
        if (resultList == null) {
            resultList = new ArrayList<>();
        }

        for (GradeRelate relate : tmpList) {
            String[] tmpStuNos = relate.getGradeObjNo().split(",");
            if (tmpStuNos == null) {
                continue;
            }
            for (String stuNo : tmpStuNos) {

                // 判断当前学生任务是否已提交
                params = new HashMap<>();
                params.put("taskId", relate.getTaskId());
                params.put("studentNo", stuNo);
                StudentTasks studentTasks = studentTasksMapper.selectByStudent(params);
                if (studentTasks == null || !studentTasks.getStatus().equals(CommonEnum.StudentTaskStatus.TO_REVIEW.getValue())) {
                    continue;
                }
                user = userMapper.selectBySerialNo(stuNo);
                if (user == null) {
                    continue;
                }
                Schedule schedule = new Schedule();
                schedule.setMarkPersonName(studentName);
                schedule.setMarkPersonSerialNo(studentNo);

                schedule.setTargetSerialName(user.getName());
                schedule.setTargetSerialNo(user.getSerialNo());
                schedule.setTaskId(relate.getTaskId());
                schedule.setTaskName(relate.getTaskName());

                resultList.add(schedule);
            }
        }
        return resultList;
    }

    @Override
    public int updateSubjectScore(SubjectGradeModel subjectGradeModel) {
        if (subjectGradeModel.getSubjectList() == null) {
            return 0;
        }
        // 先更新学员主观题分数
        subjectGradeModel.getSubjectList().forEach(sub -> {
            StudentPaper paper = new StudentPaper();
            paper.setScore(sub.getScore());
            paper.setQuestionId(sub.getQuestionId());
            paper.setStudentNo(sub.getStudentNo());
            studentPaperMapper.updateByStudent(paper);
        });

        // 更新学生任务总分，及状态
        Map<String, Object> params = new HashMap<>();
        params.put("studentNo", subjectGradeModel.getStudentNo());
        params.put("taskId", subjectGradeModel.getTaskId());
        List<StudentPaper> papers = studentPaperMapper.getPaperByStudent(params);
        if (papers == null) {
            return 0;
        }
        Float totalScore = 0f;
        for (StudentPaper paper : papers) {
            if (!paper.getScore().isNaN()) {
                totalScore += paper.getScore();
            }
        }

        // 更新学生任务信息
        StudentTasks studentTasks = new StudentTasks();
        studentTasks.setTaskId(subjectGradeModel.getTaskId().toString());
        studentTasks.setStudentNo(subjectGradeModel.getStudentNo());
        studentTasks.setStatus(CommonEnum.StudentTaskStatus.FINISHED.getValue());
        studentTasks.setScore(totalScore);
        studentTasksMapper.updateByStudentTask(studentTasks);

        return 1;
    }

    @Override
    public User getSubjectGradePerson(String taskId, String studentNo) {

        Map<String, Object> params = new HashMap<>();
        params.put("taskId", taskId);
        params.put("studentNo", studentNo);
        GradeRelate gradeRelate = gradeRelateMapper.selectByStudent(params);

        if (gradeRelate == null) {
            return userMapper.selectBySerialNo(studentNo);
        }

        return userMapper.selectBySerialNo(gradeRelate.getStudentNo());
    }


    private List<GradeRelate> updateMemberGradeObj(Integer courseId, String markType) {
        // 根据课程id获取所有组成员信息
        List<GroupMember> groupMembers = groupMemberMapper.selectByCourseId(courseId);
        if (groupMembers == null) {
            return null;
        }
        // 根据组分配信息
        Map<Integer, List<GroupMember>> memberMap = new HashMap<>();
        for (GroupMember member : groupMembers) {
            if (!memberMap.containsKey(member.getGroupId())) {
                memberMap.put(member.getGroupId(), new ArrayList<>());
            }
            memberMap.get(member.getGroupId()).add(member);
        }
        List<GradeRelate> gradeRelateList = new ArrayList<>();
        // 判断当前评分类型，根据不同的类型进行数据更新
        if (markType.equals(CommonEnum.GradeType.GROUP_INNER_EVA.getValue())) {
            for (Map.Entry<Integer, List<GroupMember>> entry : memberMap.entrySet()) {
                gradeRelateList.addAll(groupInnerEva(entry.getValue()));
            }
        } else {
            List<Integer> keyList = new ArrayList<>();
            keyList.addAll(memberMap.keySet());
            for (int i = 0; i < keyList.size(); i++) {
                if (i == (keyList.size() - 1)) {
                    gradeRelateList.addAll(groupInterBlockEva(
                            memberMap.get(keyList.get(i)),
                            memberMap.get(keyList.get(0))));
                    break;
                }
                gradeRelateList.addAll(groupInterBlockEva(
                        memberMap.get(keyList.get(i)),
                        memberMap.get(keyList.get(i + 1))));
            }
        }

        return gradeRelateList;
    }

    private List<GradeRelate> groupInnerEva(List<GroupMember> groupMembers) {
        List<GradeRelate> resultList = new ArrayList<>();

        for (int i = 0; i < groupMembers.size(); i++) {
            GradeRelate relate = new GradeRelate();
            relate.setCourseId(groupMembers.get(i).getCourseId());
            relate.setStudentNo(groupMembers.get(i).getStudentNo());
            if (i == (groupMembers.size() - 1)) {
                relate.setGradeObjNo(groupMembers.get(0).getStudentNo());
                resultList.add(relate);
                break;
            }
            relate.setGradeObjNo(groupMembers.get(i + 1).getStudentNo());
            resultList.add(relate);
        }
        return resultList;
    }

    /**
     * @param prevList
     * @param lastList
     */
    private List<GradeRelate> groupInterBlockEva(List<GroupMember> prevList, List<GroupMember> lastList) {

        List<GradeRelate> resultList = new ArrayList<>();

        for (int i = 0; i < prevList.size(); i++) {
            if (i == lastList.size()) {
                break;
            }
            GradeRelate relate = new GradeRelate();
            relate.setStudentNo(prevList.get(i).getStudentNo());
            relate.setCourseId(prevList.get(i).getCourseId());
            relate.setGradeObjNo(lastList.get(i).getStudentNo());
            resultList.add(relate);
        }
        if (lastList.size() > prevList.size()) {
            for (int i = prevList.size(); i < lastList.size(); i++) {
                int index = i % prevList.size();
                resultList.get(index).setGradeObjNo(getGradeObjNo(
                        resultList.get(index).getGradeObjNo(), lastList.get(i).getStudentNo()));
            }
        }
        return resultList;
    }

    private String getGradeObjNo(String origin, String dst) {
        if (origin.isEmpty()) {
            return dst;
        }
        return String.format("%s,%s", origin, dst);
    }

    private void insertBatch(List<GradeRelate> relateList) {
        if (relateList == null) {
            return;
        }
        relateList.forEach(relate -> gradeRelateMapper.insertSelective(relate));
    }

}
