package edu.coursemgr.service.interfaces;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.model.Course;
import edu.coursemgr.pojo.GradeDetail;
import edu.coursemgr.pojo.PageModel;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/16 0016 下午 10:43
 */
public interface CourseMgrService {

    List<Course> getTeacherCourseList(String tearcherNo);

    int saveCourse(String name, String credit, String description,
                       String userNo);

    List<Course> getStuCourseList(String stuNo);

    /**
     * 根据课程id获取课程信息
     * @param courseId
     * @return
     */
    Course getCourseById(String courseId);

    /**
     * 更新课程信息
     * @param course
     * @param isSelective  为true则选择性的更新，及如果对象属性值为空，则不更新，
     *                     反之则不管是否为空都更新
     * @return
     */
    int updateCourse(Course course, boolean isSelective);

    /**
     * 获取课程下所有学员的成绩信息
     * @param courseId
     * @param pageSize
     * @param currPage
     * @return
     */
    PageModel getAllGradeInfo(String courseId, String pageSize, String currPage);

    /**
     * 根据课程id更新，课程分组方式
     * @param course
     * @return
     */
    int updateCourseGroupType(Course course, CommonEnum.GroupedType type);

    /**
     * 获取学员课程信息
     * @param courseId
     * @param studentNo
     * @return
     */
    GradeDetail getStuGradeInfo(String courseId, String studentNo);

    /**
     * 导出当前课程所有学生成绩信息
     * @param courseId
     */
    void exportCourseGrade(String courseId, HttpServletResponse response) throws Exception;

    /**
     * 导出学生成绩信息
     * @param courseId
     * @param studentNo
     * @param response
     * @throws Exception
     */
    void exportStuGrade(String courseId, String studentNo,
                        HttpServletResponse response) throws Exception;

    /**
     * 根据教工号，删除与之关联的课程信息
     * @param teacherNo
     * @return
     */
    boolean deleteCourseByTeacherNo(String teacherNo);

    /**
     * 根据用户获取与之关联的课程信息列表
     * @param role
     * @param serialNo
     * @return
     */
    List<Course> getCourseByUser(String role, String serialNo);

}
