<template>
    <div class="main">
      <div class="left-nav">
        <el-menu
          router
          @select="handleSelect"
          default-active="/studentHome/basicInfo"
          class="el-menu-vertical-demo main"
          @open="handleOpen"
          @close="handleClose"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b">
          <el-menu-item index="/studentHome/basicInfo">
            <i class="el-icon-location"></i>
            <span slot="title">基本信息</span>
          </el-menu-item>
          <el-menu-item index="/studentHome/groupInfo">
            <i class="el-icon-menu"></i>
            <span slot="title">我的分组</span>
          </el-menu-item>
          <el-menu-item index="3">
            <i class="el-icon-document"></i>
            <span slot="title">我的任务</span>
          </el-menu-item>
          <el-menu-item index="4">
            <i class="el-icon-setting"></i>
            <span slot="title">我的课程</span>
          </el-menu-item>
          <el-menu-item index="5">
            <i class="el-icon-setting"></i>
            <span slot="title">我的待办</span>
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
      name: "studentHome",
      data(){
        return{

        }
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
    height: 100%;
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
    background: aqua;
  }
</style>
