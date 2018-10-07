package edu.coursemgr.service.interfaces;

import edu.coursemgr.pojo.StatAllGradeModel;

import java.util.List;

/**
 * Created by chenxianzhang on 2018/10/7 0007 下午 9:21
 */
public interface StatMgrService {


    /**
     * 统计所有成绩
     * @param courseId
     * @param sort
     * @return
     */
    StatAllGradeModel statAllGrade(String courseId, String sort);


}
