<template>
  <div class="main-container">
    <div class="course-info">
      <div class="course-title">
        <span class="course-name">课程名称：{{course.name}}</span>
        <span class="course-score">学分：<span>{{course.credit}}</span></span>
        <div v-if="editable" class="el-icon-edit-outline edit" @click="handleEditCourse"></div>
      </div>
      <div class="course-comments">
        <span class="course-comments-span">课程描述：</span>
        <span class="course-comments-content">{{course.description}}</span>
      </div>
    </div>
    <div style="width: calc(100% - 60px); margin-left: 30px;">
      <div class="course-student-title">
        <span>学生信息（总人数：{{studentsInCourse.length}}）</span>
        <div v-if="editable" class="add-student-btn">
          <el-button type="primary" @click="addStudents2Course">添加学生</el-button>
          <el-button type="primary" @click="uploadStudents2Course">批量导入学生名单</el-button>
        </div>
      </div>
      <el-table :data="studentsInCourse" style="width: 100%;">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="name" label="姓名" width="180"></el-table-column>
        <el-table-column prop="code" label="学号"></el-table-column>
        <el-table-column prop="school" label="学院"></el-table-column>
        <el-table-column prop="proffecial" label="专业"></el-table-column>
        <el-table-column prop="tel" label="电话"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column
          label="操作"
          width="100">
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
            <el-button type="text" size="small">编辑</el-button>
            <el-button type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  <upload-student-comp :showUploadDialog="showUploadDialog" @hideUploadDialog="hideUploadDialog"></upload-student-comp>
  </div>
</template>

<script>
  import {getCourseById, getStudentsByCourseId} from '@/api/course';
  import UploadStudentComp from "../../components/uploadStudentComp";

  export default {
    name: "basicInfo",
    components: {UploadStudentComp},
    data(){
      return{
        showUploadDialog:false,
        editable:false,
        studentsInCourse:[],
        course:null,
      }
    },
    created(){
      let cId = this.$store.getters.courseId;
      this.getCourseInfo(cId);
      this.getStudentsOfCourse(cId);

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
      },
      getCourseInfo(cId){
        let self = this;
        getCourseById({courseId: cId}).then(response => {
          if (response.status === 0) {
            self.$message({
              showClose: true,
              type: 'warning',
              message: response.msg
            });
            return;
          }
          console.log(response.data);
          self.course = response.data;
        });
      },
      getStudentsOfCourse(cId){
        let self = this;
        getStudentsByCourseId({courseId: cId}).then(response => {
          if (response.status === 0) {
            self.$message({
              showClose: true,
              type: 'warning',
              message: response.msg
            });
            return;
          }
          console.log(response.data)
          self.studentsInCourse = response.data;
        });
      },
      //添加学生
      addStudents2Course(){

      },
      //批量导入学生名单
      uploadStudents2Course(){
        this.showUploadDialog = true;
      },
      hideUploadDialog(val){
        this.showUploadDialog = val;
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
    height: 20%;
    width: calc(100% - 60px);
    box-shadow: 0px 0px 8px 5px #3276bd;
    border-radius: 3px;
    margin: 30px;
    padding: 10px;
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
  .add-student-btn{
    float: right;
  }
  .course-student-title{
    height: 44px;
    line-height: 44px;
    margin: 10px 0px;
    border-bottom: 1px solid gray;
  }
</style>
