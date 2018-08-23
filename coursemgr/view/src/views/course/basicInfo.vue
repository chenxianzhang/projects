<template>
  <div class="main-container">
    <div class="course-info">
      <div class="course-title">
        <span class="course-name">课程名称</span>
        <span class="course-score">学分：<span>{{courseScore}}</span></span>
        <div v-if="editable" class="el-icon-edit edit" @click="handleEditCourse"></div>
      </div>
      <div class="course-comments">
        <span class="course-comments-span">课程描述：</span>
        <span class="course-comments-content">{{courseCommentsContent}}</span>
      </div>
    </div>
    <el-table :data="studentsInCourse" style="width: calc(100% - 60px); margin-left: 30px;">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="name" label="姓名" width="180"></el-table-column>
      <el-table-column prop="code" label="学号"></el-table-column>
      <el-table-column prop="school" label="学院"></el-table-column>
      <el-table-column prop="proffecial" label="专业"></el-table-column>
      <el-table-column prop="tel" label="电话"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="proffecial" label="操作"></el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    name: "basicInfo",
    data(){
      return{
        editable:false,
        studentsInCourse:[],
        courseScore:3,
        courseCommentsContent:'系统地介绍了计算机的基本组成原理和内部工作机制，课程主要内容分成两个部分：第1、2章介绍了计算机的基础知识；第3～8章介绍了计算机的各子系统（包括运算器、存储器、控制器、外部设备和输入输出子系统等）的基本组成原理、设计方法、相互关系以及各子系统互相连接构成整机系统的技术。',
      }
    },
    created(){
      //todo 通过路由获取角色信息
      if(this.$store.state.user.roles.in_array('teacher')){
        this.editable = true;
      }
      else if(this.$store.state.user.roles.in_array('student')){
        this.editable = false;
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
    width: calc(100% - 60px);
    box-shadow: 0px 0px 8px 5px #3276bd;
    border-radius: 3px;
    margin: 30px;
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
