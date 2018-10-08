package edu.coursemgr.service.impl;

import com.sun.org.apache.xml.internal.utils.Hashtree2Node;
import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.dao.CourseTasksMapper;
import edu.coursemgr.dao.StudentTasksMapper;
import edu.coursemgr.dao.TaskQuestionsMapper;
import edu.coursemgr.dao.UserMapper;
import edu.coursemgr.model.CourseTasks;
import edu.coursemgr.model.StudentTasks;
import edu.coursemgr.model.User;
import edu.coursemgr.pojo.*;
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

    @Autowired
    private TaskQuestionsMapper taskQuestionsMapper;

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

    @Override
    public List<CourseTaskStatModel> statTaskSubmitReview(String courseId) {

        List<CourseTasks> taskList = courseTasksMapper.selectSomeByCourseId(Integer.valueOf(courseId));

        if (taskList == null) {
            return null;
        }


        return CollectionUtils.arrayListCast(taskList, task -> {
            CourseTaskStatModel model = new CourseTaskStatModel();

            model.setTaskId(task.getId());
            model.setTaskName(task.getName());
            model.setSubmitCnt(studentTasksMapper.statTastSubmitCnt(task.getId().toString()));
            model.setFinishedCnt(studentTasksMapper.selectTaskFinshedCnt(task.getId().toString()));
            return model;
        });
    }

    @Override
    public List<QuestionTypeStatModel> statQuestionTypeCnt(String courseId) {

        List<QuestionTypeStatModel> resultList = new ArrayList<>();

        Map params = new HashMap();
        params.put("courseId", courseId);
        params.put("questionType", CommonEnum.QuestionType.JUDGE.getValue());
        int judgeCnt = taskQuestionsMapper.statQuestionType(params);
        QuestionTypeStatModel model = new QuestionTypeStatModel();
        model.setCount(judgeCnt);
        model.setGetQuestionTypeText(CommonEnum.QuestionType.JUDGE.getText());
        model.setQuestionTypeValue(CommonEnum.QuestionType.JUDGE.getValue());
        resultList.add(model);

        params.put("questionType", CommonEnum.QuestionType.SINGLE_CHOICE.getValue());
        int singleCnt = taskQuestionsMapper.statQuestionType(params);
        model = new QuestionTypeStatModel();
        model.setCount(singleCnt);
        model.setGetQuestionTypeText(CommonEnum.QuestionType.SINGLE_CHOICE.getText());
        model.setQuestionTypeValue(CommonEnum.QuestionType.SINGLE_CHOICE.getValue());
        resultList.add(model);

        params.put("questionType", CommonEnum.QuestionType.SUBJECTIVE_ITEM.getValue());
        int subjectiveCnt = taskQuestionsMapper.statQuestionType(params);
        model = new QuestionTypeStatModel();
        model.setCount(subjectiveCnt);
        model.setGetQuestionTypeText(CommonEnum.QuestionType.SUBJECTIVE_ITEM.getText());
        model.setQuestionTypeValue(CommonEnum.QuestionType.SUBJECTIVE_ITEM.getValue());
        resultList.add(model);

        return resultList;
    }

    @Override
    public List<StudentTaskScoreStat> statStudentTaskScore(String courseId, String studentNo) {
        return null;
    }

    @Override
    public StuCourseSynthesizeStat statStuSynthesizeInfo(String courseId, String studentNo) {
        return null;
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
