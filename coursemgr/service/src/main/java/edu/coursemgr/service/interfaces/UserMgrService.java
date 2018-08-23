package edu.coursemgr.service.interfaces;

import edu.coursemgr.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/23 0023 下午 10:56
 */
public interface UserMgrService {

    /**
     * 通过课程ID获取当前课程下所有学生信息
     * @param courseId
     * @return
     */
    List<User> getStudentsByCourseId(String courseId);

    /**
     * 添加学生信息
     * @param user
     * @return
     */
    int addStudent(User user);

//    boolean importStudents(MultipartFile file);
}
