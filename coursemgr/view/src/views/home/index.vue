<template>
  <div class="home-main">
    <page-title></page-title>

    <div class="container">
      <el-card class="course-list">
        <div slot="header">
          <span>课程列表</span>
        </div>
        <div class="content">
          <i class="custom-icon item" v-for="(item, index) in courseList" :key="index" @click="handleCourseClick(item.id)">{{ item.name }}</i>
          <div class="custom-icon item" @click.stop="addCourse" v-if="canAddCourse">
            <i class="custom-icon plus"></i>
          </div>
        </div>
      </el-card>

      <el-card class="user-detail">
        <div slot="header" class="header">
          <span>用户信息</span>
          <div class="user-info-edit-icon">
            <i class="el-icon-edit-outline" @click="handleEdit"></i>
          </div>
        </div>
        <div class="detail-content">
          <div class="info-row">
            <span class="label">姓名</span>
            <span class="value" v-if="!editUserInfo"> {{ userInfo.name }} </span>
            <el-input v-model="userInfo.name" v-if="editUserInfo" />
          </div>
          <div class="info-row">
            <span class="label">{{ getSearialLabel() }} </span>
            <span class="value">{{ userInfo.serialNo }}</span>
          </div>
          <div class="info-row">
            <span class="label">学院</span>
            <span class="value" v-if="!editUserInfo">{{ userInfo.college }}</span>
            <el-input v-model="userInfo.college" v-if="editUserInfo" />
          </div>
          <div class="info-row">
            <span class="label">电话</span>
            <span class="value" v-if="!editUserInfo">{{ userInfo.cellphone }}</span>
            <el-input v-model="userInfo.cellphone" v-if="editUserInfo" />
          </div>
          <div class="info-row">
            <span class="label">邮箱</span>
            <span class="value" v-if="!editUserInfo">{{ userInfo.email }}</span>
            <el-input v-model="userInfo.email" v-if="editUserInfo" />
          </div>
          <div class="info-row" v-if="editUserInfo">
            <span class="label">密码</span>
            <span class="value" v-if="!editUserInfo">{{ userInfo.password }}</span>
            <el-input v-model="userInfo.password" />
          </div>
          <div class="edit-oper" v-if="editUserInfo">
            <el-button @click="cancel">取消</el-button>
            <el-button type="primary" @click="save">修改</el-button>
          </div>
        </div>
      </el-card>

    </div>
    <drag-dialog :title="courseDlgTitle" width="30%" :dialogVisible="courseDlgVisible" @close="handleCourseClose" @confirm="saveCourse">
      <div class="edit-container">
        <div class="edit-row">
          <span class="label">课程名称</span>
          <el-input placeholder="请输入课程名称" v-model="editCourse.name" clearable>
          </el-input>
        </div>
        <div class="edit-row">
          <span class="label">学分</span>
          <el-input-number v-model="editCourse.credit" :step="1" :min="0"></el-input-number>
        </div>
        <div class="edit-row">
          <span class="label">课程描述</span>
          <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 10}" placeholder="请输入课程描述" v-model="editCourse.description">
          </el-input>
        </div>
      </div>

    </drag-dialog>
  </div>
</template>
<script>
import { getTeacherCourseList, saveCourse, getStuCourseList } from '@/api/home'
import { findUser, update } from '@/api/login'
import { validateEmail, validatePhone, validatePwd } from '@/utils/validate'
import { setCourseId } from '@/utils/auth'
import dragDialog from '@/components/dragDialog'
import pageTitle from '../layout/components/pageTitle'

export default {
  name: 'home',
  data() {
    return {
      canAddCourse: false,
      courseList: [],
      userInfo: {},
      courseDlgTitle: '增加课程',
      courseDlgVisible: false,
      editCourse: {
        name: '',
        credit: 0,
        description: '',
        userNo: this.$store.state.user.token
      },
      editUserInfo: false,
      userInfoBak: {}
    }
  },
  components: {
    dragDialog,
    pageTitle
  },
  created() {
    if (!this.$store.state.user.roles) {
      // TODO 弹出提示，并调回登录页面
    }
    if (this.$store.state.user.roles.in_array('teacher')) {
      this.canAddCourse = true
    } else {
      this.canAddCourse = false
    }

    if (this.$store.state.user.roles.in_array('student')) {
      this.getStuCourseList()
    } else {
      this.getTeacherCourseList()
    }
    this.initUserInfo()
  },
  mounted() {},
  methods: {
    cancel() {
      this.editUserInfo = false
      this.userInfo = JSON.parse(JSON.stringify(this.userInfoBak))
    },
    save(mes) {
      if (!validatePwd(this.userInfo.password)) {
        this.$message({
          showClose: true,
          type: 'warning',
          message: '密码不能为空，且长度不能小于6！'
        })
        return
      }
      if (!validatePhone(this.userInfo.cellphone)) {
        this.$message({
                showClose: true,
                type: 'warning',
                message: "请输入有效的手机号码"
             });
           return;
      }
      if (!validateEmail(this.userInfo.email)) {
        this.$message({
          showClose: true,
          type: 'warning',
          message: "请输入有效的邮箱地址"
        });
        return;
      }
      let self = this
      update(this.userInfo)
        .then(response => {
          if (response.status === 0) {
            self.$message({
              showClose: true,
              type: 'warning',
              message: response.msg
            })
            return
          }
          self.userInfoBak = JSON.parse(JSON.stringify(self.userInfo))
          this.editUserInfo = false
          self.$message({
            showClose: true,
            type: 'success',
            message: '修改成功'
          })
        })
        .catch(err => {
          console.log(err)
          self.$message({
            showClose: true,
            type: 'error',
            message: '保存个人信息异常'
          })
        })
    },
    handleEdit() {
      this.editUserInfo = !this.editUserInfo
      if (!this.editUserInfo) {
        this.userInfo = JSON.parse(JSON.stringify(this.userInfoBak))
      }
    },
    getTeacherCourseList() {
      let self = this
      getTeacherCourseList({ teacherNo: this.$store.state.user.token })
        .then(response => {
          if (response.status === 0) {
            self.$message({
              showClose: true,
              type: 'error',
              message: response.msg
            })
            return
          }
          self.courseList = response.data
        })
        .catch(err => {
          console.log(err)
        })
    },
    getStuCourseList() {
      let self = this
      getStuCourseList({ studentNo: this.$store.state.user.token })
        .then(response => {
          if (response.status === 0) {
            self.$message({
              showClose: true,
              type: 'error',
              message: response.msg
            })
            return
          }
          self.courseList = response.data
        })
        .catch(err => {
          console.log(err)
        })
    },
    initUserInfo() {
      let self = this
      findUser(this.$store.state.user.token).then(response => {
        if (response.status === 0) {
          self.$message({
            showClose: true,
            type: 'error',
            message: response.msg
          })
        }
        self.userInfo = response.data
        self.userInfoBak = JSON.parse(JSON.stringify(self.userInfo))

        if (!response.data.hasLogin || response.data.hasLogin === 0) {
          self.$router.push('/login/resetpwd')
        }
      })
    },
    addCourse() {
      this.editCourse.name = ''
      this.editCourse.credit = 0
      this.editCourse.description = ''
      this.courseDlgVisible = true
    },
    handleCourseClose() {
      this.courseDlgVisible = false
    },
    getSearialLabel() {
      if (!this.$store.state.user.roles) {
        return ''
      }
      return this.$store.state.user.roles.in_array('teacher')
        ? '教工号'
        : '学号'
    },
    saveCourse() {
      if (this.editCourse.name.trim() === '') {
        this.$message({
          showClose: true,
          type: 'warning',
          message: '名称不能为空'
        })
        return
      }
      if (this.editCourse.credit === 0) {
        this.$message({
          showClose: true,
          type: 'warning',
          message: '学分不能为0'
        })
        return
      }
      let self = this
      saveCourse(this.editCourse)
        .then(response => {
          if (response.status === 0) {
            self.$message({
              showClose: true,
              type: 'error',
              message: response.msg
            })
            return
          }
          self.courseDlgVisible = false
          self.getTeacherCourseList()
        })
        .catch(err => {
          console.log(err)
        })
    },
    handleCourseClick(cId) {
      setCourseId(cId)
      this.variables.courseId = cId
      this.$router.push({ name: 'basicInfo' })
    },
    logout() {
      this.$store.dispatch('logOut').then(() => {
        // 清空tagsview
        this.$store.dispatch('delAllViews')
        //   this.$router.push('/login')
        location.reload()
      })
    },
    handleCommand(cmd) {
      switch (cmd) {
        case 'logout':
          this.logout()
          break
        case 'backHome':
          this.$router.push('/home')
          break
      }
    }
  }
}
</script>
<style lang="scss">
.home-main {
  .el-card {
    min-height: 100px;
    height: 378px;
    border-radius: 0;
    margin-top: 10px;
    .el-card__header {
      color: #fff;
      padding: 10px 20px;
      background: url('../../../static/img/card-title-bg.png');
    }
    .el-card__body {
      height: calc(100% - 42px);
      overflow: auto;
    }
  }
  .el-input-number {
    line-height: 30px !important;
  }
}
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
.home-main {
  width: 100%;
  height: 100%;
  .el-dropdown-link {
    color: #fff;
  }
  .container {
    width: 100%;
    height: calc(100% - 70px);
    display: flex;
    justify-content: center;
    background: url('../../../static/img/home-bg.png') no-repeat;
    background-size: 100% 100%;
    padding: 10px 0px;
    .course-list {
      height: 90%;
      width: 50%;
      margin-right: 20px;
      padding-bottom: 10px;
      .content {
        height: 100%;
        display: flex;
        flex-wrap: wrap;
        justify-content: flex-start;
        //justify-content: space-around;
        align-content: flex-start;
        .item {
          background: url('../../../static/img/course-item-bg.png') no-repeat;
          width: 101px;
          height: 101px;
          word-break: break-all;
          word-wrap: break-word;
          margin-top: 10px;
          margin-left: 5px;
          cursor: pointer;
          display: flex;
          justify-content: center;
          text-align: center;
          align-items: center;
          padding: 5px;
          font-size: 15px;
          font-weight: bolder;
          color: rgb(0, 150, 136);
          &:hover {
            background: url('../../../static/img/course-item-bg-hover.png')
              no-repeat;
            color: #fff;
          }
        }
        .plus {
          background: url('../../../static/img/plus.png') no-repeat;
          width: 30px;
          height: 30px;
        }
      }
    }
    .user-detail {
      width: 30%;
      .header {
        display: flex;
        justify-content: space-between;
        i {
          cursor: pointer;
          &:hover {
            opacity: 0.8;
          }
        }
      }
      .detail-content {
        border: 1px solid #ccc;
        border-bottom: 0;
        .info-row {
          border-bottom: 1px solid #ccc;
          // padding: 10px;
          font-size: 13px;
          display: flex;
          align-items: center;
          span {
            display: inline-block;
          }
          .label {
            border-right: 1px solid #ccc;
            padding: 10px;
            width: 70px;
            text-align: right;
            color: rgb(27, 44, 54);
            font-weight: bolder;
            background-color: rgb(246, 248, 247);
            height: 100%;
          }
          .value {
            padding-left: 15px;
            color: rgb(27, 44, 54);
          }
          .el-input {
            width: calc(80% - 60px);
            margin-left: 10px;
          }
        }
        .edit-oper {
          padding: 5px 0;
          display: flex;
          justify-content: center;
          border-bottom: 1px solid #ccc;
        }
      }
    }
  }
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
          width: calc(100% - 90px);
          // width: 180px;
        }
        .el-input-number {
            width: calc(100% - 90px);
        }
        .el-textarea {
          width: calc(100% - 90px);
        }
      }
    }
}

.user-info-edit-icon{
  height: 20px;
  width: 20px ;
  display: flex;
  align-items: center;
  justify-content: center;
}

  .user-info-edit-icon:hover{
    background-color: rgb(0, 87, 79);
  }
</style>
<style>
.change-pass .el-form-item__label {
  color: #ffffff !important;
}
</style>


