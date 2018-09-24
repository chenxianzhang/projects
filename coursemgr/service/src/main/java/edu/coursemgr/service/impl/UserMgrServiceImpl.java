package edu.coursemgr.service.impl;

import edu.coursemgr.common.CommonEnum;
import edu.coursemgr.common.Constant;
import edu.coursemgr.dao.CourseStudentsMapper;
import edu.coursemgr.dao.GroupMapper;
import edu.coursemgr.dao.GroupMemberMapper;
import edu.coursemgr.dao.UserMapper;
import edu.coursemgr.excel.ExcelReader;
import edu.coursemgr.excel.ExcelUtil;
import edu.coursemgr.model.CourseStudents;
import edu.coursemgr.model.Group;
import edu.coursemgr.model.GroupMember;
import edu.coursemgr.model.User;
import edu.coursemgr.pojo.PageModel;
import edu.coursemgr.pojo.UserEditModel;
import edu.coursemgr.service.interfaces.UserMgrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by chenxianzhang on 2018/8/23 0023 下午 10:58
 */
@Service
@Transactional
public class UserMgrServiceImpl implements UserMgrService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseStudentsMapper courseStudentsMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Override
    public List<User> getStudentList(String courseId) {

        return userMapper.selectSomeByCourseId(Integer.valueOf(courseId));
    }

    @Override
    public PageModel getStudentsByCourseId(String courseId,
                                           String pageSize, String currPage) {
        Integer totalCount = userMapper.selectTotalCntByCourseId(Integer.valueOf(courseId));

        Map<String, Object> params = new HashMap<>();
        params.put("courseId", courseId);
        params.put("pageSize", pageSize);

        int currSize = (Integer.valueOf(currPage) - 1) * Integer.valueOf(pageSize);
        params.put("currSize", currSize);

        List<User> data = userMapper.selectSomeByPage(params);
        PageModel pageModel = new PageModel();
        pageModel.setPageData(data);
        pageModel.setTotalCount(totalCount);
        return pageModel;
    }

    @Override
    public int addStudent(UserEditModel userEditModel) throws Exception {
        // 检查数据库中是否存在当前用户
        User user = userMapper.selectBySerialNo(userEditModel.getUser().getSerialNo());
        if (null != user) {
            throw new Exception("当前用户已存在，不可重复添加");
        }
        userEditModel.getUser().setRoles(CommonEnum.Role.STUDENT.getValue());
        userEditModel.getUser().setHasLogin(0);
        if (userMapper.insert(userEditModel.getUser()) > 0) {
            // 保存课程与学员关系
            CourseStudents cs = new CourseStudents();
            cs.setCourseId(Integer.valueOf(userEditModel.getCourseId()));
            cs.setStudentNo(userEditModel.getUser().getSerialNo());
            if (this.courseStudentsMapper.insert(cs) == 0) {
                throw new Exception("将学员加入到当前课程中发生异常");
            }
        }
        return 1;
    }

    @Override
    public void importStudents(MultipartFile file, String courseId)
            throws Exception {
        //判断文件是否为空
        if(file == null){
            throw new Exception(Constant.ExceptionMessage.PARAM_EMPTY);
        }
        String name = file.getOriginalFilename();
        long size = file.getSize();
        boolean illegal = name == null || ExcelUtil.EMPTY.equals(name) && size == 0;
        if(illegal){
            throw new Exception(Constant.ExceptionMessage.EXCEL_EMPTY);
        }
        //读取Excel数据到List中
        // 默认第一行为表头
        List<ArrayList<String>> list = new ExcelReader().readExcel(file);
        // 读取表头更新StuExcelHeader枚举的index值，代表其所在excel表中的列
        illegal = null == list || list.size() == 0;
        if (illegal) {
            throw new Exception(Constant.ExceptionMessage.EXCEL_EMPTY);
        }
//        udpateHeaderIndex(list.get(0));

        // 读取表格中数据,并插入数据库
        readData2DB(list, courseId);
    }

    @Override
    public List<User> getNoGroupStuList(String courseId) {

        return userMapper.selectSomeNoGroup(Integer.valueOf(courseId));
    }

    @Override
    public int deleteStudent(String courseId, String studentNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("courseId", courseId);
        params.put("studentNo", studentNo);

        // 确认当前学生是否为组长
        Group group = groupMapper.selectByLeader(params);
        if (group != null) {
            Map tmpParams = new HashMap();
            params.put("groupId", group.getId());
            params.put("studentNo", studentNo);
            List<GroupMember> members = groupMemberMapper.selectOtherMember(tmpParams);
            if (members != null && members.size() > 0) {
                group.setGroupLeaderNo(members.get(0).getStudentNo());
                group.setLeaderName(members.get(0).getStudentName());
                // 更新group
                groupMapper.updateByIdSelective(group);

            } else {
                groupMapper.deleteById(group.getId());
            }
        }

        // 删除组成员表中的信息
        groupMemberMapper.deleteByStudent(params);

        return courseStudentsMapper.deleteByStudent(params);
    }

    private void udpateHeaderIndex(ArrayList<String> headers) {
        if (headers == null) {
            return;
        }

        for (int i = 0; i < headers.size(); i++) {
            for (CommonEnum.StuExcelHeader excelHeader : CommonEnum.StuExcelHeader.values()) {
                if (excelHeader.getText().equals(headers.get(i).trim())) {
                    excelHeader.setIndex(i);
                }
            }
        }
    }

    private User list2Obj(ArrayList<String> row) {

        if (row == null) {
            return null;
        }
        User user = new User();
        user.setName(row.get(CommonEnum.StuExcelHeader.NAME.getIndex()));
        user.setSerialNo(row.get(CommonEnum.StuExcelHeader.STUDENT_ID.getIndex()));
//        user.setCollege(row.get(CommonEnum.StuExcelHeader.COLLEGE.getIndex()));
        user.setProfession(row.get(CommonEnum.StuExcelHeader.PROFESSIONAL.getIndex()));
//        user.setCellphone(row.get(CommonEnum.StuExcelHeader.CELLPHONE.getIndex()));
//        user.setEmail(row.get(CommonEnum.StuExcelHeader.EMAIL.getIndex()));
//        user.setSex(row.get(CommonEnum.StuExcelHeader.SEX.getIndex()));
        user.setRoles(CommonEnum.Role.STUDENT.getValue());
        user.setHasLogin(0);
        user.setCreateDate(new Date());
        boolean illegal = user.getSerialNo().isEmpty() ||
                user.getSerialNo().length() < Constant.Common.PASSWORD_MIN_LENGTH;
        if (illegal) {
            return null;
        }
        int len = Constant.Common.PASSWORD_MIN_LENGTH;
        String stuNo = user.getSerialNo();
        user.setPassword(stuNo.substring(stuNo.length() - len, stuNo.length()));
        return user;
    }

    private boolean isHeader(ArrayList<String> row) {
        if (row == null || row.size() == 0) {
            return false;
        }
        String rowStr = String.join(",", row);
        if (rowStr.contains(CommonEnum.StuExcelHeader.NAME.getText()) &&
                rowStr.contains(CommonEnum.StuExcelHeader.STUDENT_ID.getText()) &&
                rowStr.contains(CommonEnum.StuExcelHeader.PROFESSIONAL.getText())) {
            return true;
        }
        return false;
    }

    private void readData2DB(List<ArrayList<String>> list, String courseId) throws Exception {
        boolean header = false;
        for (int i = 0; i < list.size(); i++) {

            // 判断当前是否是列头所在行
            if (isHeader(list.get(i))) {
                header = true;
                udpateHeaderIndex(list.get(i));
                continue;
            }
            if (!header) {
                continue;
            }
            User user = list2Obj(list.get(i));
            if (user == null) {
                continue;
            }
            User tmpUser = userMapper.selectBySerialNo(user.getSerialNo());
            // 如果用户不存在数据表中，则插入
            if (tmpUser == null) {
                if (userMapper.insert(user) == 0) {
                    throw new Exception(Constant.ExceptionMessage.DATA_SAVE_EXCEPTION);
                }
            }

            // 并将数据关联到课程学生表中
            CourseStudents courseStudents = new CourseStudents();
            courseStudents.setCourseId(Integer.valueOf(courseId));
            courseStudents.setStudentNo(user.getSerialNo());
            Map<String, Object> params = new HashMap<>();
            params.put("courseId", courseId);
            params.put("studentNo", user.getSerialNo());
            CourseStudents existCS = courseStudentsMapper.selectByStudent(params);
            if (existCS != null) {
                return;
            }
            if (courseStudentsMapper.insert(courseStudents) == 0) {
                throw new Exception(Constant.ExceptionMessage.DATA_SAVE_EXCEPTION);
            }
        }
    }

}
