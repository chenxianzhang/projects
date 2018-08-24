<template>
    <div class="main">
      <div style="height: 50px; width: 100%; background-color: #456; line-height: 50px; color: white; padding-left: 20px;">
        <span>课程管理系统</span>
        <div style="float: right; margin-right: 20px;">当前用户：xxx</div>
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
            <i class="el-icon-location"></i>
            <span slot="title">基本信息</span>
          </el-menu-item>
          <el-menu-item v-if="isStudent" index="/course/groupInfo">
            <i class="el-icon-menu"></i>
            <span slot="title">我的分组</span>
          </el-menu-item>
          <el-menu-item v-if="isStudent" index="/course/taskInfo">
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

          <el-menu-item v-if="!isStudent" index="/teacherHome/groupInfo">
            <i class="el-icon-setting"></i>
            <span slot="title">分组情况</span>
          </el-menu-item>
          <el-menu-item v-if="!isStudent" index="/course/taskInfo">
            <i class="el-icon-setting"></i>
            <span slot="title">课程任务</span>
          </el-menu-item>
          <el-menu-item v-if="!isStudent" index="/course/gradeInfo">
            <i class="el-icon-setting"></i>
            <span slot="title">所有成绩</span>
          </el-menu-item>
          <el-menu-item v-if="!isStudent" index="/teacherHome/newTask">
            <i class="el-icon-setting"></i>
            <span slot="title">新建任务</span>
          </el-menu-item>
        </el-menu>
      </div>
        <div class="right-container">
          <router-view></router-view>
        </div>
    </div>
</template>

<script>

    export default {
      name: "course",
      data(){
        return{
          isStudent:false,
        }
      },
      created(){
        this.isStudent = this.$store.state.user.roles.in_array('student');
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
  }
</style>
