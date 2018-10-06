<template>
    <div class="user-container">

        <user-edit-dlg 
            v-if="uEditDlgVisible" 
            :visible="uEditDlgVisible" 
            :role="role" 
            :title="uEditDlgTitle"
            @close="handleClose"
            @confirm="handleConfirm" 
            :user='user'></user-edit-dlg>

        <drag-dialog :title="courseTitle" width="36%" :dialogVisible="courseVisible" :hiddenOperator="true" @close="handleCourseClose">
            <el-card class="box-card">
                <div v-for="course in courseList" :key="course.id">
                    {{ course.name }}
                </div>
            </el-card>
        </drag-dialog>

        <drag-dialog :title="delDlgTitle" width="36%" :dialogVisible="delDlgVisible" @close="handleDelDlgClose" @confirm="handleDelDlgConfirm">
            <div class="del-container">
                <div class="tipinfo">
                    <span>删除用户的同时会删除相关的所有课程信息，请输入二级密码进行确认</span>
                </div>
                <div class="level-pwd-input">
                    <el-input v-model="levelPwd" type="password" placeholder="请输入二级密码" :style="{width: '60%'}"></el-input>
                </div>
            </div>
        </drag-dialog>

        <div class="operation">
            <div class="left">
                <el-button type="primary" @click="addUser">添加</el-button>
                <el-button type="primary">批量导入</el-button>
            </div>
            <div class="right">
                <el-input :placeholder="placeholder" v-model="nameOfNo" @input="queryChange">
                    <el-button slot="append" icon="el-icon-search"></el-button>
                </el-input>
            </div>
        </div>
        <router-view ref="child" @handleCommond="handleCommond" />
    </div>
</template>

<script>
import userEditDlg from '../components/userEditDlg'
import { addUser, updateUser, getCourseByUser, resetPwd, deleteUser } from '@/api/manager'

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
      currUserSerialNo: this.$store.state.user.token
    }
  },
  components: {
    userEditDlg
  },
  mounted() {
    if (this.$route.path.indexOf('student') >= 0) {
      this.placeholder = '输入姓名或者学号'
      this.role = 'student'
    } else {
      this.placeholder = '输入姓名或者教工号'
      this.role = 'teacher'
    }
  },
  methods: {
    queryChange(value) {
      this.$refs.child.$emit('query', this.nameOfNo)
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

<style lang="scss" scoped>
.user-container {
  .operation {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid gray;
    padding-bottom: 5px;
  }
}

.del-container {
    .tipinfo {
        text-align: center;
    }
    .level-pwd-input {
        margin-top: 20px;
        display: flex;
        justify-content: center;
    }
}
</style>

