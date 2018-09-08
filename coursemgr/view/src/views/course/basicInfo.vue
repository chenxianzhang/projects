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
        <el-table-column v-if="editable" type="selection" width="55"></el-table-column>
        <el-table-column align="center" prop="name" label="姓名" width="180"></el-table-column>
        <el-table-column align="center" prop="serialNo" label="学号"></el-table-column>
        <el-table-column align="center" prop="college" label="学院"></el-table-column>
        <el-table-column align="center" prop="profession" label="专业"></el-table-column>
        <el-table-column align="center" prop="cellphone" label="电话"></el-table-column>
        <el-table-column align="center" prop="email" label="邮箱"></el-table-column>
        <el-table-column v-if="editable" align="center"
          label="操作"
          width="140">
          <template slot-scope="scope">
            <el-button @click="handleStudentDetailClick(scope.row)" type="text" size="small">查看</el-button>
            <el-button @click="handleStudentEditClick(scope.row)" type="text" size="small">编辑</el-button>
            <el-button @click="handleStudentDeleteClick(scope.row)" type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  <upload-student-comp :showUploadDialog="showUploadDialog" @hideUploadDialog="hideUploadDialog"></upload-student-comp>

    <drag-dialog :title="courseDlgTitle" width="36%" :dialogVisible="courseDlgVisible"
                 @close="handleCourseClose" @confirm="updateCourse">
      <div class="edit-container">
        <div class="edit-row">
          <span class="label">课程名称</span>
          <el-input placeholder="请输入课程名称" v-model="course.name" clearable>
          </el-input>
        </div>
        <div class="edit-row">
          <span class="label">学分</span>
          <el-input-number v-model="course.credit" :step="1" :min="0"></el-input-number>
        </div>
        <div class="edit-row">
          <span class="label">课程描述</span>
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 10}"
            placeholder="请输入课程描述"
            v-model="course.description">
          </el-input>
        </div>
      </div>
    </drag-dialog>

    <student-add-comp :studentOperInfo="studentOperInfo" :showStudentAddDialog="showStudentAddDialog" @hideStudentAddDialog="handleHideStudentAddDialog">
    </student-add-comp>
  </div>
</template>

<script>
  import {getCourseById, getStudentsByCourseId,updateCourse, deleteStudent} from '@/api/course';
  import UploadStudentComp from "../../components/uploadStudentComp";
  import dragDialog from '@/components/dragDialog';
  import studentAddComp from '@/components/studentAddComp'

  export default {
    name: "basicInfo",
    components: {UploadStudentComp, dragDialog, studentAddComp},
    data(){
      return{
        courseDlgTitle:'课程编辑',
        courseDlgVisible: false,

        showStudentAddDialog:false,//是否显示 手动添加学生弹出框
        showUploadDialog:false,
        editable:false,
        studentsInCourse:[],
        //学生操作  add、detail、edit
        studentOperInfo:{
          operType:'',
          student:{
            roles:3,
            name:'',
            serialNo:'',
            password:'',
            college:'',
            profession:'',
            cellphone:'',
            sex:'',
            email:''
          }
        },
        course:{
          name: '',
          credit: 0,
          description: '',
          userNo: this.$store.state.user.token
        },
      }
    },
    created(){
      //let cId = this.$store.getters.courseId;
      let cId = this.$route.params.courseId;
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
        this.studentOperInfo.operType = 'add';
        this.studentOperInfo.student = {
          roles:3,
          name:'',
          serialNo:'',
          password:'',
          college:'',
          profession:'',
          cellphone:'',
          sex:'',
          email:''
        };
        this.showStudentAddDialog = true;
      },
      handleHideStudentAddDialog(val){
        this.showStudentAddDialog = val;
        let cId = this.$store.getters.courseId;
        // this.getCourseInfo(cId);
        this.getStudentsOfCourse(cId);
      },
      //批量导入学生名单
      uploadStudents2Course(){
        this.showUploadDialog = true;
      },
      hideUploadDialog(val){
        this.showUploadDialog = val;
      },
      /**
       * 查看学生详情信息
       * */
      handleStudentDetailClick(data){
        this.studentOperInfo.operType = 'detail';
        this.studentOperInfo.student = data;
        this.showStudentAddDialog = true;
      },
      /**
       * 编辑学生详情信息
       * */
      handleStudentEditClick(data){
        this.studentOperInfo.operType = 'edit';
        this.studentOperInfo.student = data;
        this.showStudentAddDialog = true;
      },
      /**
       * 删除学生详情信息
       * */
      handleStudentDeleteClick(data){

        let self = this;
        deleteStudent({courseId: this.$route.params.courseId,
         studentNo: data.serialNo}).then(response => {

            if (response.status === 1) {
            self.getStudentsOfCourse(self.$route.params.courseId);
              self.$message({
                showClose: true,
                type: 'success',
                message: "删除成功"
              });
            } else {
              self.$message({
                showClose: true,
                type: 'warning',
                message: "删除失败"
              });
            }

         }).catch(err => {
            console.log(err);
         })
      },


      handleEditCourse: function () {
        this.courseDlgVisible = true;
      },
      handleCourseClose () {
        this.courseDlgVisible = false;
      },
      updateCourse() {
        if (this.course.name.trim() === '' ) {
          this.$message({
            showClose: true,
            type: 'warning',
            message: "名称不能为空"
          });
          return;
        }
        if (this.course.credit === 0) {
          this.$message({
            showClose: true,
            type: 'warning',
            message: "学分不能为0"
          });
          return;
        }
        let self = this;
        updateCourse(this.course).then(response => {
          if (response.status === 0) {
            self.$message({
              showClose: true,
              type: 'error',
              message: response.msg
            });
            return;
          }
          self.$message.success('修改成功！');
          self.courseDlgVisible = false;
          self.getTeacherCourseList();
        }).catch(err => {
          console.log(err);
        });
      },
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
<style rel="stylesheet/scss" lang="scss" scoped>
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
</style>
