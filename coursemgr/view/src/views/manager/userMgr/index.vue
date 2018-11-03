<template>
  <div class="user-container">

    <user-edit-dlg v-if="uEditDlgVisible" :visible="uEditDlgVisible" :role="role" :title="uEditDlgTitle" :operator="userOperStatus" @close="handleClose" @confirm="handleConfirm" :user='user'></user-edit-dlg>

    <drag-dialog :title="courseTitle" width="36%" :dialogVisible="courseVisible" :hiddenOperator="true" @close="handleCourseClose">
      <el-card class="box-card" v-if="courseList && courseList.length > 0">
        <div class="item" v-for="course in courseList" :key="course.id">
          {{ course.name }}
        </div>
      </el-card>
      <div v-else>
        <span>{{ courseTipInfo }}</span>
      </div>
    </drag-dialog>

    <drag-dialog :title="delDlgTitle" width="36%" :dialogVisible="delDlgVisible" @close="handleDelDlgClose" @confirm="handleDelDlgConfirm">
      <div class="del-container">
        <div class="tipinfo">
          <span>删除用户的同时会删除相关的所有课程信息，请输入二级密码进行确认</span>
        </div>
        <div class="level-pwd-input">
          <el-input v-model="levelPwd" type="password" placeholder="请输入二级密码"></el-input>
        </div>
      </div>
    </drag-dialog>

    <upload-student-comp :showUploadDialog="showUploadDialog" @hideUploadDialog="hideUploadDialog" :uploadAction="uploadAction"></upload-student-comp>

    <div class="operation">
      <div class="left">
        <el-button size="small" @click="addUser">添加</el-button>
        <el-button size="small" @click="handleImport">批量导入</el-button>
        <el-button size="small" type="primary" @click="handleDownload">模板下载</el-button>
      </div>
      <div class="right">
        <el-input :placeholder="placeholder" v-model="nameOfNo" @input="queryChange">
          <el-button type="primary" slot="append" icon="el-icon-search"></el-button>
        </el-input>
      </div>
    </div>
    <router-view ref="child" @handleCommond="handleCommond" />
  </div>
</template>

<script>
import userEditDlg from '../components/userEditDlg'
import uploadStudentComp from '@/components/uploadStudentComp'

import {
  addUser,
  updateUser,
  getCourseByUser,
  resetPwd,
  deleteUser,
  downloadTemplate
} from '@/api/manager'
import { dodwnloadStuTemp } from '@/api/course'

export default {
  name: 'index',
  data() {
    return {
      placeholder: '输入姓名或者教工号',
      nameOfNo: '',
      uEditDlgVisible: false,
      uEditDlgTitle: '',
      role: '',
      userOperStatus: 'add',
      user: {},
      courseVisible: false,
      courseTitle: '开设课程',
      courseList: [],
      delDlgTitle: '删除确认',
      delDlgVisible: false,
      levelPwd: '',
      delSerialNo: '',
      currUserSerialNo: this.$store.state.user.token,
      showUploadDialog: false,
      uploadAction: '',
      courseTipInfo: ''
    }
  },
  components: {
    userEditDlg,
    uploadStudentComp
  },
  mounted() {
    if (this.$route.path.indexOf('student') >= 0) {
      this.placeholder = '输入姓名或者学号'
      this.courseTipInfo = '没有关联的课程信息'
      this.courseTitle = '参与课程'
      this.role = 'student'
      this.uploadAction =
        window.global.BASE_API + '/userMgr/batchUploadStudents'
    } else {
      this.placeholder = '输入姓名或者教工号'
      this.role = 'teacher'
      this.courseTipInfo = '该教师暂未开设课程'
      this.courseTitle = '开设课程'
      this.uploadAction =
        window.global.BASE_API + '/userMgr/batchUploadTeachers'
    }
  },
  methods: {
    queryChange() {
      this.$refs.child.$emit('query', this.nameOfNo)
    },
    handleDownload() {
      if (this.role === 'student') {
        dodwnloadStuTemp()
        return
      }
      downloadTemplate()
    },
    handleImport() {
      this.showUploadDialog = true
    },
    hideUploadDialog(val) {
      this.showUploadDialog = false
      this.queryChange()
    },
    handleCommond(data) {
      switch (data.commond) {
        case 'edit':
          this.uEditDlgTitle = '编辑用户信息'
          this.user = data.value
          this.uEditDlgVisible = true
          this.userOperStatus = 'edit'
          break
        case 'course':
          this.getCourseByUser(data.value)
          this.courseVisible = true
          break
        case 'reset':
          this.resetPwd(data.value)
          break
        case 'delete':
          this.delDlgVisible = true
          this.delSerialNo = data.value.serialNo
          break
      }
    },
    deleteUser() {
      let params = {
        serialNo: this.delSerialNo,
        role: this.role,
        levelPwd: this.levelPwd,
        currUserSerialNo: this.currUserSerialNo
      }
      deleteUser(params)
        .then(response => {
          if (response.status === 0) {
            this.$msg.error(response.msg)
            return
          }
          this.delSerialNo = ''
          this.levelPwd = ''
          this.handleDelDlgClose()
          this.queryChange()
          this.$msg.success('删除用户成功')
        })
        .catch(err => {
          this.$msg.error(err)
        })
    },
    resetPwd(value) {
      let params = {
        serialNo: value.serialNo
      }
      resetPwd(params)
        .then(response => {
          if (response.status === 0) {
            this.$msg.error(response.msg)
            return
          }
          let msg =
            this.role === 'teacher'
              ? '重置成功，当前密码为教工号'
              : '重置成功，当前密码为学号'
          this.$msg.success(msg)
        })
        .catch(err => {
          this.$msg.error(err)
        })
    },
    getCourseByUser(value) {
      let params = {
        serialNo: value.serialNo,
        role: this.role
      }
      getCourseByUser(params)
        .then(response => {
          if (response.status === 0) {
            this.$msg.error(response.msg)
            return
          }
          debugger
          this.courseList = response.data
        })
        .catch(err => {
          this.$msg.error(err)
        })
    },
    addUser() {
      this.uEditDlgTitle = '增加用户信息'
      this.uEditDlgVisible = true
      this.userOperStatus = 'add'
      this.user = {}
    },
    handleClose() {
      this.uEditDlgVisible = false
    },
    handleConfirm(user) {
      user.roles = this.role
      if (this.userOperStatus === 'edit') {
        updateUser(user)
          .then(response => {
            if (response.status === 0) {
              this.$msg.warning(response.msg)
              return
            }
            this.handleClose()
            this.queryChange()
            this.user = {}
            this.$msg.success('编辑用户信息成功')
          })
          .catch(err => {
            this.$msg.error(err)
          })
        return
      }
      addUser(user)
        .then(response => {
          if (response.status === 0) {
            this.$msg.warning(response.msg)
            return
          }
          this.handleClose()
          this.queryChange()
          this.user = {}
          this.$msg.success('保存用户信息成功')
        })
        .catch(err => {
          this.$msg.error(err)
        })
    },
    handleCourseClose() {
      this.courseVisible = false
    },
    handleDelDlgClose() {
      this.delDlgVisible = false
    },
    handleDelDlgConfirm() {
      if (!this.levelPwd || this.levelPwd.trim() === '') {
        this.$msg.warning('二级密码不能为空')
        return
      }
      this.deleteUser()
    }
  }
}
</script>

<style lang="scss">
.user-container {
  .right {
    .el-input {
      .el-input__inner {
        border: 1px solid #009788;
        &:focus {
          border: 1px solid #ccc;
        }
      }
      .el-input-group__append {
        border-radius: unset;
        background-color: #009788;
        color: #fff;
        border: 1px solid #009788;
      }
    }
  }
}
</style>


<style lang="scss" scoped>
.user-container {
  .operation {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid gray;
    padding-bottom: 5px;
  }
  .el-card {
      border: 6px solid rgb(232, 235, 242);
      box-shadow: unset;
      .item {
        font-size: 14px;
        font-weight: bolder;
      }
  }
}

.del-container {
  .tipinfo {
    text-align: center;
    border: 1px dashed #009788;
    padding: 5px;
    background-color: rgb(229, 243, 243);
    font-size: 12px;
    color: #009788;
  }
  .level-pwd-input {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}
</style>

