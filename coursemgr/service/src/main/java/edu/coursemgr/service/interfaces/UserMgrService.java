package edu.coursemgr.service.interfaces;

import edu.coursemgr.model.User;
import edu.coursemgr.pojo.PageModel;
import edu.coursemgr.pojo.UserEditModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/23 0023 下午 10:56
 */
public interface UserMgrService {

    /**
     * 通过课程ID获取当前课程下所有学生信息
     * @param courseId
     * @param currPage
     * @param pageSize
     * @return
     */
    PageModel getStudentsByCourseId(String courseId, String pageSize, String currPage);

    /**
     * 添加学生信息
     * @param user
     * @return
     */
    int addStudent(UserEditModel userEditModel) throws Exception;

    /**
     * 导入数据到数据库
     * @param file
     * @param courseId
     * @return
     */
    void importStudents(MultipartFile file, String courseId) throws Exception;

    /**
     * 获取课程下所有未分组的学生信息
     * @param courseId
     * @return
     */
    List<User> getNoGroupStuList(String courseId);

    /**
     * 删除指定课程下的学生关联关系
     * @param courseId
     * @param studentNo
     * @return
     */
    int deleteStudent(String courseId, String studentNo);
}
