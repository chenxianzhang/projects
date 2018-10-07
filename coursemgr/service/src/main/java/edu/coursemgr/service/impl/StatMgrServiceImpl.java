package edu.coursemgr.service.impl;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.dao.CourseTasksMapper;
import edu.coursemgr.dao.StudentTasksMapper;
import edu.coursemgr.dao.UserMapper;
import edu.coursemgr.model.StudentTasks;
import edu.coursemgr.model.User;
import edu.coursemgr.pojo.StatAllGradeModel;
import edu.coursemgr.pojo.StudentScore;
import edu.coursemgr.pojo.StudentTaskInfo;
import edu.coursemgr.service.interfaces.StatMgrService;
import edu.coursemgr.utils.CollectionUtils;
import edu.coursemgr.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by chenxianzhang on 2018/10/7 0007 下午 9:52
 */
@Service
@Transactional
public class StatMgrServiceImpl implements StatMgrService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentTasksMapper studentTasksMapper;

    @Autowired
    private CourseTasksMapper courseTasksMapper;

    @Override
    public StatAllGradeModel statAllGrade(String courseId, String sort) {

        List<User> userList = userMapper.selectSomeByCourseId(Integer.valueOf(courseId));
        if (userList == null || userList.size() == 0) {
            return null;
        }

        Float minScore = null;
        Float maxScore = null;
        Float sumScore = 0f;
        List<StudentScore> studentScoreList = new ArrayList<>();
        for (User user : userList) {
            Map params = new HashMap();
            params.put("courseId", courseId);
            params.put("studentNo", user.getSerialNo());
            List<StudentTaskInfo> studentTaskList = courseTasksMapper.selectStuTaskInfo(params);

            StudentScore studentScore = new StudentScore();
            studentScore.setStudentNo(user.getSerialNo());
            studentScore.setStudentName(user.getName());

            Float totalScore = 0f;

            if (studentTaskList != null) {
                for (StudentTaskInfo taskInfo : studentTaskList) {
                    if (taskInfo.getStatus() == null) {
                        continue;
                    }
                    if (taskInfo.getScore() == null) {
                        continue;
                    }
                    totalScore += taskInfo.getScore() * taskInfo.getTaskWeight() / 100;
                }
            }
            if (minScore == null || minScore > totalScore) {
                minScore = totalScore;
            }
            if (maxScore == null || maxScore < totalScore) {
                maxScore = totalScore;
            }
            sumScore += totalScore;
            studentScore.setTotalScore(totalScore);
            studentScoreList.add(studentScore);
        }

        if (!CommonUtils.isEmpty(sort)) {
            sortStudentScore(studentScoreList, sort);
        }

        StatAllGradeModel model = new StatAllGradeModel();
        model.setStudentScoreList(studentScoreList);
        model.setMinScore(minScore);
        model.setMaxScore(maxScore);
        model.setAvgScore(sumScore / userList.size());

        return model;
    }


    private void sortStudentScore(List<StudentScore> studentScoreList, final String sort) {

        Collections.sort(studentScoreList, new Comparator<StudentScore>() {
            @Override
            public int compare(StudentScore o1, StudentScore o2) {

                if ("desc".equals(sort)) {
                    return o2.getTotalScore().compareTo(o1.getTotalScore());
                }

                return o1.getTotalScore().compareTo(o2.getTotalScore());
            }
        });
    }
}
