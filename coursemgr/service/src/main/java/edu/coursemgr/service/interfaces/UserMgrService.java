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
     * 获取课程下所有学生信息
     * @param courseId
     * @return
     */
    List<User> getStudentList(String courseId);

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


    /**
     * 通过不同角色获取不同用户
     * @param role
     * @param currPage
     * @param pageSize
     * @param nameOrNo
     * @return
     */
    PageModel getUserByRole(String role, String pageSize, String currPage, String nameOrNo);

    /**
     * 添加用户信息
     * @param user
     * @return
     */
    int addUser(User user) throws Exception;

    /**
     * 编辑用户信息
     * @param user
     * @return
     */
    int updateUser(User user) throws Exception;

    /**
     * 根据二级密码删除指定用户
     * @param user
     * @return
     */
    int deleteUser(User user);

    /**
     * 清除该学生与之关联的所有课程信息
     * @param studentNo
     * @return
     * @throws Exception
     */
    int deleteStudent(String studentNo) throws Exception;

    /**
     * 判断二级密码是否正确
     * @param serialNo
     * @param levelPwd
     * @return
     */
    User checkLevelPwd(String serialNo, String levelPwd) throws Exception;
}
