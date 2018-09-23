<template>
  <div class="main">

    <div style="height: 50px; width: 100%; background-color: #456; line-height: 50px; color: white; padding-left: 20px;">
        <span>课程管理系统</span>
        <div style="float: right; margin-right: 50px;font-size:14px">
          当前用户：
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              {{userInfo.name}}<i class="el-icon-arrow-down el-icon--right white-color"></i>
            </span>
            <el-dropdown-menu slot="dropdown" class="el-menu-vertical-demo">
              <el-dropdown-item command="backHome">返回首页</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
        </div>
      </div>
    <div class="container">
      <div class="flex-center">
        <div class="container-left">
          <div class="module-title">
            <span>课程列表</span>
          </div>
          <div class="course-list">
            <div class="card-panel" v-for="(item, index) in courseList" :key="index" @click="handleCourseClick(item.id)">
              <span>{{ item.name }}</span>
            </div>
            <div class="card-panel" @click.stop="addCourse" v-if="canAddCourse">
              <i class="el-icon-plus"></i>
            </div>
          </div>
        </div>
        <div class="container-right">
            <div class="module-title">
              <span>用户信息</span>
              <!--<svg-icon icon-class="edit"></svg-icon>-->
              <i class="el-icon-edit-outline" @click="handleEdit"></i>
            </div>
            <div class="content">
              <div class="info-row">
                <span class="label">姓名</span>
                <span class="value" v-if="!editUserInfo"> {{ userInfo.name }} </span>
                <el-input v-model="userInfo.name" v-if="editUserInfo"/>
              </div>
              <div class="info-row">
                <span class="label">{{ getSearialLabel() }} </span>
                <span class="value">{{ userInfo.serialNo }}</span>
              </div>
              <div class="info-row">
                <span class="label">学院</span>
                <span class="value" v-if="!editUserInfo">{{ userInfo.college }}</span>
                <el-input v-model="userInfo.college" v-if="editUserInfo"/>
              </div>
              <div class="info-row">
                <span class="label">电话</span>
                <span class="value" v-if="!editUserInfo">{{ userInfo.cellphone }}</span>
                <el-input v-model="userInfo.cellphone" v-if="editUserInfo"/>
              </div>
              <div class="info-row">
                <span class="label">邮箱</span>
                <span class="value" v-if="!editUserInfo">{{ userInfo.email }}</span>
                <el-input v-model="userInfo.email" v-if="editUserInfo"/>
              </div>
              <div class="edit-oper" v-if="editUserInfo">
                <el-button @click="cancel" >取消</el-button>
                <el-button type="primary" @click="save">修改</el-button>
              </div>
            </div>
        </div>
      </div>
    </div>
    <drag-dialog :title="courseDlgTitle" width="36%" :dialogVisible="courseDlgVisible"
                 @close="handleCourseClose" @confirm="saveCourse">
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
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 10}"
            placeholder="请输入课程描述"
            v-model="editCourse.description">
          </el-input>
        </div>
      </div>

    </drag-dialog>
  </div>
</template>
<script>

import { getTeacherCourseList, saveCourse, getStuCourseList } from '@/api/home';
import { findUser,update } from '@/api/login';
import { validateEmail, validatePhone } from '@/utils/validate';
import dragDialog from '@/components/dragDialog';

export default {
  name: "home",
  data() {
    return {
      canAddCourse: false,
      courseList: [],
      userInfo: {},
      courseDlgTitle: "增加课程",
      courseDlgVisible: false,
      editCourse: {
        name: '',
        credit: 0,
        description: '',
        userNo: this.$store.state.user.token
      },
      editUserInfo: false,
      userInfoBak: {}
    };
  },
  components:{
    dragDialog
  },
  created () {
    if (!this.$store.state.user.roles) {
      // TODO 弹出提示，并调回登录页面
    }
    if (this.$store.state.user.roles.in_array('teacher')) {
      this.canAddCourse = true;
    }
    else {
      this.canAddCourse = false;
    }

    if (this.$store.state.user.roles.in_array('student')) {
      this.getStuCourseList();
    } else {
      this.getTeacherCourseList();
    }
    this.initUserInfo();
  },
  mounted () {

  },
  methods: {
    cancel() {
      this.editUserInfo = false;
      this.userInfo = JSON.parse(JSON.stringify(this.userInfoBak));
    },
    save() {
      if (this.userInfo.college.trim() === "") {
        this.$message({
                showClose: true,
                type: 'warning',
                message: "学院信息不能为空"
             });
             return;
      }
      if (!validatePhone(this.userInfo.cellphone)) {
        this.$message({
                showClose: true,
                type: 'warning',
                message: "请输入有效的手机号码"
             });
           return;
      }
      /*if (!validateEmail(this.userInfo.email)) {
         this.$message({
              showClose: true,
              type: 'warning',
              message: "请输入有效的邮箱地址"
           });
           return;
      }*/
      let self = this;
      update(this.userInfo).then(response => {
        if (response.status === 0) {
          self.$message({
              showClose: true,
              type: 'warning',
              message: response.msg
           });
           return;
        }
        self.userInfoBak = JSON.parse(JSON.stringify(self.userInfo));
        this.editUserInfo = false;
        self.$message({
              showClose: true,
              type: 'success',
              message: "修改成功"
           });
      }).catch(err => {
        console.log(err);
        self.$message({
              showClose: true,
              type: 'error',
              message: "保存个人信息异常"
           });
      });
    },
    handleEdit() {
      this.editUserInfo = !this.editUserInfo;
      if (!this.editUserInfo) {
        this.userInfo = JSON.parse(JSON.stringify(this.userInfoBak));
      }
    },
    getTeacherCourseList() {
      let self = this;
      getTeacherCourseList({teacherNo: this.$store.state.user.token}).then(response => {
        if (response.status === 0) {
          self.$message({
              showClose: true,
              type: 'error',
              message: response.msg
           });
          return;
        }
        self.courseList = response.data;
      }).catch (err => {
        console.log(err);
      });
    },
    getStuCourseList() {
        let self = this;
        getStuCourseList({studentNo: this.$store.state.user.token}).then(response => {
          if (response.status === 0) {
            self.$message({
                showClose: true,
                type: 'error',
                message: response.msg
             });
            return;
          }
          self.courseList = response.data;
        }).catch (err => {
          console.log(err);
        });
    },
    initUserInfo () {
      let self = this;
      findUser(this.$store.state.user.token).then(response => {
        if (response.status === 0) {
          self.$message({
              showClose: true,
              type: 'error',
              message: response.msg
           });
        }
        self.userInfo = response.data;
        self.userInfoBak = JSON.parse(JSON.stringify(self.userInfo));
      })
    },
    addCourse() {
      this.editCourse.name = "";
      this.editCourse.credit = 0;
      this.editCourse.description = "";
      this.courseDlgVisible = true;
    },
    handleCourseClose () {
       this.courseDlgVisible = false;
    },
    getSearialLabel() {
      if (!this.$store.state.user.roles) {
        return "";
      }
      return this.$store.state.user.roles.in_array('teacher') ? "教工号" : "学号";
    },
    saveCourse() {
       if (this.editCourse.name.trim() === '' ) {
          this.$message({
              showClose: true,
              type: 'warning',
              message: "名称不能为空"
           });
           return;
       }
       if (this.editCourse.credit === 0) {
          this.$message({
              showClose: true,
              type: 'warning',
              message: "学分不能为0"
           });
           return;
       }
       let self = this;
       saveCourse(this.editCourse).then(response => {
          if (response.status === 0) {
            self.$message({
              showClose: true,
              type: 'error',
              message: response.msg
           });
           return;
          }
          self.courseDlgVisible = false;
          self.getTeacherCourseList();
       }).catch(err => {
          console.log(err);
       });
    },
    handleCourseClick(cId){
      this.$store.commit('SET_COURSE_ID', cId);
      this.$router.push({name:'basicInfo', params:{courseId: cId}});
    },
  }
}


</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.main{
  background-color: #567;
  width: 100%;
  height: 100%;
  .el-dropdown-link{
    color:#fff;
  }
.container {
  background-color: #567;
  width: 100%;
  height: calc(100% - 55px);
  display: flex;
  justify-content: center;
  padding: 10px 0px;
  .flex-center {
    width: 84%;
    height: 100%;
    display: flex;
     justify-content: center;
    .container-left {
      border-radius: 4px;
      height: 100%;
      width: calc(100% - 400px);
      background-color: white;
      .course-list {
        width: 100%;
        height: calc(100% - 44px);
        display: flex;
        flex-wrap: wrap;
        justify-content: flex-start;
        //justify-content: space-around;
        align-content: flex-start;
        overflow: auto;
        .card-panel {
          display: flex;
          justify-content: center;
          flex-wrap: wrap;
          align-items: center;
          width: 100px;
          height: 100px;
          background-color: #87a035;
          border-radius: 4px;
          margin-top: 10px;
          margin-left: 5px;
          cursor: pointer;
          span {
            text-align: center;
            font-size: 18px;
            font-weight: 800;
            color: #ccc;
          }
          .el-icon-plus {
            color: #ccc;
          }
        }
        .card-panel:hover {
          background-color: rgba(135, 160, 53, 0.8);
          span {
            color: #fff;
          }
        }
      }
    }
    .container-right {
      width: 400px;
      height: 400px;
      border-radius: 4px;
      background-color: #fff;
      margin-left: 10px;
      .content {
        width: 100%;
        height: calc(100% - 44px);
        .info-row {
          border-bottom: 1px solid #ccc;
          padding: 10px;
          font-size:  13px;
          display: flex;
          align-items: center;
          span {
            display: inline-block;
          }
          .label {
            width: 70px;
            text-align: right;
            color: #555;
            font-weight: 600;
          }
          .value {
            padding-left: 15px;
            color: #888;
          }
          .el-input {
            width: calc(80% - 60px);
            margin-left: 10px;
          }
        }
        .edit-oper {
          margin-top: 15px;
          display: flex;
          justify-content: center;
        }
      }
    }
    .module-title {
      color: #303133;
      font-weight: 800;
      transition: color .2s cubic-bezier(.645,.045,.355,1);
      border-bottom: 1px solid #ccc;
      box-shadow: 0 1px 0 #ccc;
      display: flex;
      justify-content: space-between;
      align-items: center;
      span {
        padding: 10px;
        display: inline-block;
      }
      .el-icon-edit-outline {
        cursor: pointer;
        margin-right: 10px;
      }
      .el-icon-edit-outline:hover {
        opacity: 0.8;
      }
    }
  }
  .edit-container {
    padding: 0 20px;
    .edit-row {
      display:flex;
      align-items: center;
      padding: 10px 0;
      .label {
        width: 70px;
        text-align: right;
        margin-right: 20px;
      }
      .el-input{
         // width: calc(100% - 90px);
         width: 180px;
      }
      .el-textarea {
        width: calc(100% - 90px);
      }
    }
  }
}
}


</style>


