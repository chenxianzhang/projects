<template>
    <div class="main">
      <div style="height: 50px; width: 100%; background-color: #456; line-height: 50px; color: white; padding-left: 20px;">
        <span>课程管理系统</span>
        <div style="float: right; margin-right: 50px;">
          当前用户：{{userInfo.name}}
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <i class="el-icon-arrow-down el-icon--right white-color"></i>
            </span>
            <el-dropdown-menu slot="dropdown" class="el-menu-vertical-demo">
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
        </div>
      </div>
      <div class="left-nav">
        <el-menu
          router
          @select="handleSelect"
          default-active="/course/basicInfo"
          class="el-menu-vertical-demo main"
          @open="handleOpen"
          @close="handleClose"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b">
          <el-menu-item index="/course/basicInfo">
            <i class="el-icon-info"></i>
            <span slot="title">基本信息</span>
          </el-menu-item>
          <el-menu-item v-if="isStudent" index="/course/groupInfo">
            <i class="el-icon-menu"></i>
            <span slot="title">我的分组</span>
          </el-menu-item>
          <el-menu-item v-if="isStudent" index="/course/taskInfoList">
            <i class="el-icon-document"></i>
            <span slot="title">我的任务</span>
          </el-menu-item>
          <el-menu-item v-if="isStudent" index="/course/gradeInfo">
            <i class="el-icon-setting"></i>
            <span slot="title">我的成绩</span>
          </el-menu-item>
          <el-menu-item v-if="isStudent" index="/course/gTaskInfo">
            <i class="el-icon-setting"></i>
            <span slot="title">我的待办</span>
          </el-menu-item>

          <el-submenu v-if="!isStudent" index="2">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>分组情况</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/teacherHome/grouped">已分组</el-menu-item>
              <el-menu-item index="/teacherHome/NotGrouped">未分组</el-menu-item>
            </el-menu-item-group>
          </el-submenu>

          <el-submenu index="3" v-if="!isStudent">
            <template slot="title">
              <i class="el-icon-document"></i>
              <span>课程任务</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/course/taskInfoList">任务列表</el-menu-item>
              <el-menu-item index="/teacherHome/newTask">新建任务</el-menu-item>
            </el-menu-item-group>
          </el-submenu>

          <el-menu-item v-if="!isStudent" index="/course/gradeInfo">
            <i class="el-icon-tickets"></i>
            <span slot="title">所有成绩</span>
          </el-menu-item>
        </el-menu>
      </div>
        <div class="right-container">
          <router-view></router-view>
        </div>
    </div>
</template>

<script>
  import { findUser } from '@/api/login';
    export default {
      name: "course",
      data(){
        return{
          isStudent:false,
          userInfo:{},
        }
      },
      created(){
        this.isStudent = this.$store.state.user.roles.in_array('student');
        //获取用户信息
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
        })
      },
      methods: {
        handleOpen(key, keyPath) {
          console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
          console.log(key, keyPath);
        },
        handleSelect(index, indexPath){
          console.log(index, indexPath);
        },
        logout(){
          this.$router.push('/login');
        },
        handleCommand(cmd){
          switch (cmd) {
            case "logout":
              this.$router.push('/login');
              break;
          }
        }
      }
    }
</script>

<style scoped>
  .main{
    height: calc(100% - 50px);
    width: 100%;
  }
  .left-nav{
    width: 20%;
    height: 100%;
    float: left;
    background: antiquewhite;
  }

  .left-nav > ul{
    height: 100%;
  }

  .right-container{
    width: calc(80% - 2px);
    height: 100%;
    margin-left: 2px;
    float: left;
    padding: 20px;
    overflow: auto;
  }

  .white-color{
    color: #fff;
    cursor: pointer;
  }
</style>
