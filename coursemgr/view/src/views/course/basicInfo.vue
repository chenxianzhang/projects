<template>
  <div class="main-container">
    <div class="course-student-title">
      <div style="display: flex; align-items: center; justify-content: center;">
        <i class="custom-icon-course-info"></i>
        <span style="display: inline-block; margin-right: 10px; margin-left: 10px">课程信息</span>
        <span style="color: #009687; font-weight: bold;">COURSE INFORMATION</span>
      </div>
    </div>

    <div class="course-info">
      <div class="course-title">
        <span class="course-name">课程名称：{{course.name}}</span>
        <span class="course-score">学分：
          <span>{{course.credit}}</span>
        </span>
        <div v-if="editable" class="el-icon-edit-outline edit" @click="handleEditCourse"></div>
      </div>
      <div class="course-comments">
        <span class="course-comments-span">课程描述：</span>
        <span class="course-comments-content">{{course.description}}</span>
      </div>
    </div>
    <!-- <div style="width: calc(100% - 60px); margin-left: 30px;"> -->
    <div>
      <div class="course-student-title">
        <div style="display: flex; align-items: center; justify-content: center;">
          <i class="role-icon-student"></i>
          <span>学生信息（总人数：{{totalCount}}）</span>
          <span style="color: #009687; font-weight: bold;">STUDENT INFORMATION</span>
        </div>
        <div v-if="editable" class="add-student-btn">
          <el-button size="small" @click="addStudents2Course">添加学生</el-button>
          <el-button size="small" @click="uploadStudents2Course">导入学生</el-button>
          <el-button size="small" type="primary" @click="downloadStuTemplate">模板下载</el-button>
        </div>
      </div>
      <el-table :data="studentsInCourse"
                :header-cell-style="{background:'rgba(28, 77, 125, 0.8)', color:'white', fontWeight:'bold'}"
                style="width: 100%;">
        <el-table-column type="index" width="55" align="center"></el-table-column>
        <el-table-column align="center" prop="name" label="姓名" width="180"></el-table-column>
        <el-table-column align="center" prop="serialNo" label="学号"></el-table-column>
        <el-table-column align="center" prop="college" label="学院"></el-table-column>
        <el-table-column align="center" prop="profession" label="专业"></el-table-column>
        <el-table-column align="center" prop="cellphone" label="电话"></el-table-column>
        <el-table-column align="center" prop="email" label="邮箱"></el-table-column>
        <el-table-column v-if="editable" align="center" label="操作" width="180">
          <template slot-scope="scope">
            <el-tooltip content="编辑" placement="top">
              <div class="icon icon-course-edit" @click.stop="handleStudentEditClick(scope.row)"></div>
            </el-tooltip>
            <el-tooltip content="待办" placement="top">
              <div class="icon icon-course-todo" @click.stop="handleStudentTodoClick(scope.row)"></div>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <div class="icon icon-course-delete" @click.stop="handleStudentDeleteClick(scope.row)"></div>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="currPage"
                       :page-sizes="[10, 15, 20, 30]"
                       :page-size="pageSize"
                       layout="total, sizes, prev, pager, next, jumper"
                       :total="totalCount" style="float: right ">
        </el-pagination>
      </div>
    </div>
    <upload-student-comp :showUploadDialog="showUploadDialog" @hideUploadDialog="hideUploadDialog"></upload-student-comp>

    <drag-dialog :title="courseDlgTitle" width="36%" :dialogVisible="courseDlgVisible" @close="handleCourseClose" @confirm="updateCourse">
      <div class="edit-container">
        <div class="edit-row">
          <span class="label">课程名称</span>
          <el-input placeholder="请输入课程名称" v-model="course.name" clearable>
          </el-input>
        </div>
        <div class="edit-row">
          <span class="label">学分</span>
          <el-input-number v-model="course.credit" :step="1" :min="0"></el-input-number>
        </div>
        <div class="edit-row">
          <span class="label">课程描述</span>
          <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 10}" placeholder="请输入课程描述" v-model="course.description">
          </el-input>
        </div>
      </div>
    </drag-dialog>

    <student-add-comp :studentOperInfo="studentOperInfo" :showStudentAddDialog="showStudentAddDialog" @hideStudentAddDialog="handleHideStudentAddDialog">
    </student-add-comp>

    <drag-dialog title="学生待办移交" width="36%" v-if="showgTaskHandleOutDlg"
                 :dialogVisible="showgTaskHandleOutDlg" @close="handlegTaskClose" @confirm="handleOutgTask">
      <div class="handle-out-container">
        <span style="color: #4481ff;">{{curHandleUser.name}}</span>
        <span> 的待办事项</span>
        <div style="max-height: 300px; overflow: auto;">
          <el-tree ref="gtaskTree" :data="curHandleUsergTasks" show-checkbox>
            <!--<span class="custom-tree-node" slot-scope="{ node, data }">-->
              <!--审阅<span style="color: #009687"> {{data.targetSerialName}} </span>关于“<span style="color: #009687"> {{data.taskName}} </span>”任务主观题-->
            <!--</span>-->

            <div class="grouped-item" slot-scope="{ node, data }">
              <div style="margin-left: 10px">
                审阅
                <span class="span-hight-light"> {{data.targetSerialName}} </span>
                关于<span class="span-hight-light"> {{data.taskName}} </span>任务的主观题
              </div>
             </div>

          </el-tree>
        </div>
        <el-row :scutter="10" v-if="curHandleUsergTasks && curHandleUsergTasks.length !== 0" style="height: 40px; margin-top: 10px">
          <el-col :span="4" style="text-align: center">
            <span style="line-height: 40px">移交给：</span>
          </el-col>
          <el-col :span="20" style="height: 40px">
            <el-select v-model="handleOver2UserId" filterable placeholder="请选择..." style="width: 90%">
              <el-option v-for="item in canHandleOverUsers" :key="item.serialNo" :label="item.name" :value="item.serialNo">
              </el-option>
            </el-select>
          </el-col>
        </el-row>
      </div>
    </drag-dialog>
  </div>
</template>

<script>
import {
  getCourseById,
  getStudentsByCourseId,
  updateCourse,
  deleteStudent,
  dodwnloadStuTemp,
  getExceptStudentList
} from '@/api/course'
import { getScheduleByStudent, handOverSchedule } from '@/api/gTasks'
import UploadStudentComp from '../../components/uploadStudentComp'
import dragDialog from '@/components/dragDialog'
import studentAddComp from '@/components/studentAddComp'

export default {
  name: 'basicInfo',
  components: { UploadStudentComp, dragDialog, studentAddComp },
  data() {
    return {
      defaultProps:{

      },
      showgTaskHandleOutDlg: false, //待办移交弹窗
      curHandleUser: '',
      curHandleUsergTasks: [], //当前用户的待办事项
      gTaskCheckList: [], //已选待办事项列表
      handleOver2UserId: '', //移交给
      canHandleOverUsers: [], //可选择移交用户列表

      courseDlgTitle: '课程编辑',
      courseDlgVisible: false,

      showStudentAddDialog: false, //是否显示 手动添加学生弹出框
      showUploadDialog: false,
      editable: false,
      studentsInCourse: [],
      //学生操作  add、detail、edit
      studentOperInfo: {
        operType: '',
        student: {
          roles: 3,
          name: '',
          serialNo: '',
          password: '',
          college: '',
          profession: '',
          cellphone: '',
          sex: '',
          email: ''
        }
      },
      course: {
        name: '',
        credit: 0,
        description: '',
        userNo: this.$store.state.user.token
      },
      pageSize: 10,
      currPage: 1,
      totalCount: 0
    }
  },
  created() {
    let cId = this.variables.courseId
    this.getCourseInfo(cId)
    this.getStudentsOfCourse(cId)

    if (this.$store.state.user.roles.in_array('teacher')) {
      this.editable = true
    } else if (this.$store.state.user.roles.in_array('student')) {
      this.editable = false
    }
  },
  methods: {
    calcTableHeight() {
      setTimeout(() => {
        let totalHeight = document.body.getBoundingClientRect().height
        let courseElHeight = document
          .getElementsByClassName('course-info')[0]
          .getBoundingClientRect().height
        let subHeight = 350
        document.getElementsByClassName(
          'el-table__body-wrapper'
        )[0].style.height =
          totalHeight - courseElHeight - subHeight + 'px'
        document.getElementsByClassName(
          'el-table__body-wrapper'
        )[0].style.overflowY =
          'auto'
      }, 50)
    },
    downloadStuTemplate() {
      dodwnloadStuTemp()
    },
    handleSizeChange(val) {
      this.pageSize = val
      let cId = this.variables.courseId
      this.getStudentsOfCourse(cId)
    },
    handleCurrentChange(val) {
      this.currPage = val
      let cId = this.variables.courseId
      this.getStudentsOfCourse(cId)
    },
    getCourseInfo(cId) {
      let self = this
      getCourseById({ courseId: cId }).then(response => {
        if (response.status === 0) {
          self.$message({
            showClose: true,
            type: 'warning',
            message: response.msg
          })
          return
        }
        console.log(response.data)
        self.course = response.data
      })
    },
    getStudentsOfCourse(cId) {
      let self = this
      getStudentsByCourseId({
        courseId: cId,
        pageSize: this.pageSize,
        currPage: this.currPage
      }).then(response => {
        if (response.status === 0) {
          self.$message({
            showClose: true,
            type: 'warning',
            message: response.msg
          })
          return
        }
        self.totalCount = response.data.totalCount
        self.studentsInCourse = response.data.pageData
        this.calcTableHeight()
      })
    },
    //添加学生
    addStudents2Course() {
      this.studentOperInfo.operType = 'add'
      this.studentOperInfo.student = {
        roles: 3,
        name: '',
        serialNo: '',
        password: '',
        college: '',
        profession: '',
        cellphone: '',
        sex: '',
        email: ''
      }
      this.showStudentAddDialog = true
    },
    handleHideStudentAddDialog(val) {
      this.showStudentAddDialog = val
      let cId = this.variables.courseId
      // this.getCourseInfo(cId);
      this.getStudentsOfCourse(cId)
    },
    //批量导入学生名单
    uploadStudents2Course() {
      this.showUploadDialog = true
    },
    hideUploadDialog(val) {
      this.showUploadDialog = val
      this.getStudentsOfCourse(this.variables.courseId)
    },
    /**
     * 查看学生详情信息
     * */
    handleStudentDetailClick(data) {
      this.studentOperInfo.operType = 'detail'
      this.studentOperInfo.student = data
      this.showStudentAddDialog = true
    },
    /**
     * 编辑学生详情信息
     * */
    handleStudentEditClick(data) {
      this.studentOperInfo.operType = 'edit'
      this.studentOperInfo.student = data
      this.showStudentAddDialog = true
    },
    /**
     * 删除学生详情信息
     * */
    handleStudentDeleteClick(data) {
      let self = this

      this.$confirm('此操作将删除该学生, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          deleteStudent({
            courseId: this.variables.courseId,
            studentNo: data.serialNo
          })
            .then(response => {
              if (response.status === 1) {
                self.getStudentsOfCourse(self.variables.courseId)
                self.$message({
                  showClose: true,
                  type: 'success',
                  message: '删除成功'
                })
              } else {
                self.$message({
                  showClose: true,
                  type: 'warning',
                  message: response.msg
                })
              }
            })
            .catch(err => {
              console.log(err)
            })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },

    /**
     * handleStudentTodoClick  学生待办查看
     * */
    handleStudentTodoClick(data) {
      this.curHandleUser = data
      this.showgTaskHandleOutDlg = true
      //todo 当前用户的获取待办事项
      getScheduleByStudent({
        studentNo: data.serialNo,
        courseId: this.variables.courseId
      }).then(resp => {
        if (resp.status === 0) {
          this.$message.warning('获取待办事项失败')
          console.log(resp.msg)
          return
        }
        this.curHandleUsergTasks = resp.data
        console.log(this.curHandleUsergTasks)

        getExceptStudentList({
          courseId: this.variables.courseId,
          studentNo: data.serialNo
        }).then(res => {
          if (res.status === 0) {
            this.$msg.warning('获取移交人信息失败')
            console.log(res.msg)
            return
          }
          this.canHandleOverUsers = res.data
        })
      })
    },
    /**
     * 确认移交待办事项
     * */
    handleOutgTask() {
      let scheduleList = this.$refs.gtaskTree.getCheckedNodes();
      handOverSchedule({
        courseId: this.variables.courseId,
        originStudentNo: this.curHandleUser.serialNo,
        dstStudentNo: this.handleOver2UserId,
        scheduleList:scheduleList
      }).then(resp => {
        if (resp.status === 0) {
          this.$message.warning('移交待办事项失败！')
          console.log(resp.msg)
          return
        }
        this.$message.success('移交待办事项成功！')
        this.showgTaskHandleOutDlg = false
        this.getStudentsOfCourse(this.variables.courseId)
      })
    },
    /**
     * 关闭移交待办事项弹窗
     * */
    handlegTaskClose() {
      this.showgTaskHandleOutDlg = false
    },
    handleEditCourse: function() {
      this.courseDlgVisible = true
    },
    handleCourseClose() {
      this.courseDlgVisible = false
    },
    updateCourse() {
      if (this.course.name.trim() === '') {
        this.$message({
          showClose: true,
          type: 'warning',
          message: '名称不能为空'
        })
        return
      }
      if (this.course.credit === 0) {
        this.$message({
          showClose: true,
          type: 'warning',
          message: '学分不能为0'
        })
        return
      }
      let self = this
      updateCourse(this.course)
        .then(response => {
          if (response.status === 0) {
            self.$message({
              showClose: true,
              type: 'error',
              message: response.msg
            })
            return
          }
          self.$message.success('修改成功！')
          self.courseDlgVisible = false
          self.getTeacherCourseList()
        })
        .catch(err => {
          console.log(err)
        })
    }
  }
}
</script>

<style scoped>
  .grouped-item{
    background-color: white;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .span-hight-light{
    background-color: #FE9226;
    color: white;
    padding: 2px 10px;
    border-radius: 10px;
    margin: 0 10px;
    font-weight: bold;
  }

.main-container {
  height: 100%;
  width: 100%;
  /* padding: 10px; */
}

.main-container .course-info {
  height: 20%;
  /* width: calc(100% - 60px); */
  box-shadow: 0px 0px 4px 2px rgb(84, 92, 100);
  border-radius: 3px;
  /* margin: 30px; */
  padding: 10px;
}

.main-container .course-info .course-title {
  height: 30px;
  width: 100%;
}

.main-container .course-info .course-title .course-score {
  font-size: 12px;
  display: inline-block;
  margin-left: 10px;
}

.main-container .course-info .course-title .edit {
  float: right;
  margin-right: 10px;
  cursor: pointer;
  display: inline-block;
}
.course-comments {
  word-wrap: break-spaces;
  height: calc(100% - 70px);
  margin-top: 10px;
}

.course-comments .course-comments-content {
  font-size: 12px;
}
.add-student-btn {
  float: right;
}
.course-student-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 44px;
  line-height: 44px;
  margin: 10px 0px;
}

.role-icon-student {
  margin-right: 5px;
  width: 22px;
  height: 20px;
  display: inline-block;
  background: url('../../../static/img/login/role-icon-student.png') no-repeat;
  background-size: 100% 100%;
}

.course-icon-info {
  margin-right: 5px;
  width: 22px;
  height: 20px;
  display: inline-block;
  background: url('../../../static/img/login/role-icon-student.png') no-repeat;
  background-size: 100% 100%;
}

  .icon {
    width: 22px;
    height: 22px;
    left: 2px;
    color: white;
    display: inline-block;
    margin: 0 5px;
    cursor: pointer;
  }

  .icon-course-edit {
    background: url("../../../static/img/basicInfo/edit-normal.png") no-repeat left;
  }
  .icon-course-edit:hover {
    background: url("../../../static/img/basicInfo/edit-hover.png") no-repeat left;
  }

  .icon-course-delete{
    background: url("../../../static/img/basicInfo/del-normal.png") no-repeat left;
  }
  .icon-course-delete:hover{
    background: url("../../../static/img/basicInfo/del-hover.png") no-repeat left;
  }

  .icon-course-todo{
    background: url("../../../static/img/basicInfo/gtask-normal.png") no-repeat left;
  }
  .icon-course-todo:hover{
    background: url("../../../static/img/basicInfo/gtask-hover.png") no-repeat left;
  }
</style>
<style rel="stylesheet/scss" lang="scss" scoped>
.edit-container {
  padding: 0 20px;
  .edit-row {
    display: flex;
    align-items: center;
    padding: 10px 0;
    .label {
      width: 70px;
      text-align: right;
      margin-right: 20px;
    }
    .el-input {
      // width: calc(100% - 90px);
      width: 180px;
    }
    .el-textarea {
      width: calc(100% - 90px);
    }
  }
}

.pagination {
  margin-top: 10px;
}
</style>
<style>
  .handle-out-container .el-input__inner{
    height: 40px !important;
  }
</style>
