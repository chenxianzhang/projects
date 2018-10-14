package edu.coursemgr.service.impl;

import com.sun.org.apache.xml.internal.utils.Hashtree2Node;
import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.dao.*;
import edu.coursemgr.model.CourseTasks;
import edu.coursemgr.model.StudentPaper;
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

    @Autowired
    private StudentPaperMapper studentPaperMapper;

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

        List<CourseTasks> taskList = courseTasksMapper.selectSomeByCourseId(Integer.valueOf(courseId));
        if (taskList == null) {
            return null;
        }
        final String stuNo = studentNo;
        return CollectionUtils.arrayListCast(taskList, task -> {
            StudentTaskScoreStat stat = new StudentTaskScoreStat();
            stat.setTaskId(task.getId());
            stat.setTaskName(task.getName());
            stat.setTotalScore(task.getTotalScore());

            Map params = new HashMap();
            params.put("taskId", task.getId());
            params.put("studentNo", stuNo);
            StudentTasks tasks = studentTasksMapper.selectByStudent(params);
            if (tasks != null) {
                stat.setScore(tasks.getScore());
            }
            return stat;
        });
    }

    @Override
    public StuCourseSynthesizeStat statStuSynthesizeInfo(String courseId, String studentNo) {

        List<StudentTaskScore> taskScoreList = studentTasksMapper.selectSomeByCourse(
                Integer.valueOf(courseId));

        StuCourseSynthesizeStat result = new StuCourseSynthesizeStat();

        Map params = new HashMap();
        params.put("courseId", courseId);
        params.put("studentNo", studentNo);
        List<StudentPaper> paperList = studentPaperMapper.selectSomeByCourseStu(params);

        Float subjective = taskQuestionsMapper.statSubjectiveScore(Integer.valueOf(courseId));
        Float objective = taskQuestionsMapper.statObjectiveScore(Integer.valueOf(courseId));
        // 名次及总分
        Map<String, Float> stuScoreMap = new HashMap<>();
        if (taskScoreList != null) {
            for (StudentTaskScore studentTaskScore : taskScoreList) {
                if (studentTaskScore.getScore() == null) {
                    continue;
                }
                if (!stuScoreMap.containsKey(studentTaskScore.getStudentNo())) {
                    stuScoreMap.put(studentTaskScore.getStudentNo(),
                            studentTaskScore.getScore() * studentTaskScore.getTaskWeight() / 100);
                    continue;
                }
                stuScoreMap.put(studentTaskScore.getStudentNo(),
                        stuScoreMap.get(studentTaskScore.getStudentNo()) +
                                studentTaskScore.getScore() * studentTaskScore.getTaskWeight() / 100);
            }
            Integer rank = getRank(stuScoreMap, studentNo);
            if (stuScoreMap.containsKey(studentNo)) {
                result.setTotalScore(stuScoreMap.get(studentNo));
                result.setRank(rank);
            }
        }

        // 主观题和客观题得分率
        if (paperList != null) {
            Float subjectiveScore = 0f;
            Float objectiveScore = 0f;
            for (StudentPaper paper : paperList) {

                if (paper.getQuestionType().equals(CommonEnum.QuestionType.SUBJECTIVE_ITEM.getValue())) {
                    Float score = paper.getScore();
                    if (paper.getTeacherScore() != null && !paper.getTeacherScore().isNaN()) {
                        score = paper.getTeacherScore();
                    }
                    subjectiveScore += score;
                } else {
                    objectiveScore += paper.getScore();
                }
            }

            result.setSubjectScoreRate(subjectiveScore / subjective * 100);
            result.setObjectiveItemScoreRate(objectiveScore / objective * 100);
        }

        int totalCount = userMapper.selectTotalCntByCourseId(Integer.valueOf(courseId));
        result.setTotalStudentCnt(totalCount);

        return result;
    }

    private Integer getRank(Map<String, Float> stuScoreMap, String studentNo) {

        List<Map.Entry<String, Float>> entryList = new ArrayList<Map.Entry<String, Float>>(
                stuScoreMap.entrySet());

        Collections.sort(entryList, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });


        Integer rank = 0;
        Float preScore = -1f;
        Iterator<Map.Entry<String, Float>> iter = entryList.iterator();
        Map.Entry<String, Float> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            if (preScore != tmpEntry.getValue()) {
                rank++;
            }

            if (tmpEntry.getKey().equals(studentNo)) {
                break;
            }
        }

        return rank;

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
