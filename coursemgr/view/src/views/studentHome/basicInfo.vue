<template>
  <div class="main-container">
    <div class="course-info">
      <div class="course-title">
        <span class="course-name">课程名称</span>
        <span class="course-score">学分：<span>{{courseScore}}</span></span>
        <div class="el-icon-edit edit" @click="handleEditCourse"></div>
      </div>
      <div class="course-comments">
        <span class="course-comments-span">课程描述：</span>
        <span class="course-comments-content">{{courseCommentsContent}}</span>
      </div>
    </div>
    <div></div>
  </div>
</template>

<script>
    export default {
        name: "basicInfo",
      data(){
          return{
            courseScore:3,
            courseCommentsContent:'系统地介绍了计算机的基本组成原理和内部工作机制，课程主要内容分成两个部分：第1、2章介绍了计算机的基础知识；第3～8章介绍了计算机的各子系统（包括运算器、存储器、控制器、外部设备和输入输出子系统等）的基本组成原理、设计方法、相互关系以及各子系统互相连接构成整机系统的技术。',
          }
      },
      methods:{
        handleEditCourse: function () {
          this.$message('编辑课程');
        }
      }
    }
</script>

<style scoped>
  .main-container{
    height: 100%;
    width: 100%;
    padding: 10px;
  }

  .main-container .course-info{
    height: 30%;
    width: 100%;
    background: aqua;
  }

  .main-container .course-info .course-title{
    height: 30px;
    width: 100%;
  }

  .main-container .course-info .course-title .course-score{
    font-size: 12px;
    display: inline-block;
    margin-left: 10px;
  }

  .main-container .course-info .course-title .edit{
    float: right;
    margin-right: 10px;
    cursor: pointer;
    display: inline-block;
  }
  .course-comments{
    word-wrap: break-spaces;
    height: calc(100% - 70px);
    margin-top: 10px;
  }

  .course-comments .course-comments-content{
    font-size: 12px;
  }
</style>
