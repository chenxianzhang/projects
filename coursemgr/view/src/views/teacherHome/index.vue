<template>
  <div class="main">
    <div class="p-info">
      <div class="p-info-span">
        <div>姓名：{{teacher.name}}</div>
        <div>教工号：{{teacher.tNo}}</div>
        <div>学院：{{teacher.school}}</div>
        <div>电话：{{teacher.tel}}</div>
        <div>邮箱：{{teacher.email}}</div>
      </div>
      <div class="p-info-edit el-icon-edit" @click="modifyBasicInfo()"></div>
    </div>
    <div class="course-container">
      <div class="course" v-for="course in teacher.courses" @click="addCourse(course)">
        <span :class="course==='add'?'el-icon-plus':''">{{course==='add'?'':course}}</span>
      </div>
    </div>
    <el-dialog title="课程添加" :visible.sync="dialogFormVisible">
      <el-form :label-position="labelPosition" label-width="80px" :model="course">
        <el-form-item label="课程名称">
          <el-input v-model="course.name"></el-input>
        </el-form-item>
        <el-form-item label="学分">
          <el-input v-model="course.score"></el-input>
        </el-form-item>
        <el-form-item label="课程描述">
          <el-input type="textarea" v-model="course.comments"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addCourseSubmit">添加课程</el-button>
      </div>
    </el-dialog>

    <el-dialog title="信息修改" :visible.sync="modifyFormVisible">
      <el-form :label-position="labelPosition" label-width="80px" :model="teacher">
        <el-form-item label="课程名称">
          <el-input v-model="teacher.name"></el-input>
        </el-form-item>
        <el-form-item label="教工号">
          <el-input v-model="teacher.tNo"></el-input>
        </el-form-item>
        <el-form-item label="学院">
          <el-input v-model="teacher.school"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="teacher.tel"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="teacher.email"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="modifyConfirm">确定</el-button>
        <el-button type="primary" @click="modifyCancel">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

    export default {
      name: "teacherHome",
      data(){
        return{
          modifyFormVisible:false,
          teacher:{
            name:'xxx',
            tNo:'123567',
            school:'wwww',
            tel:'324342',
            email:'23435@qq.com',
            courses:["english","math","add"]
          },
          dialogFormVisible:false,
          labelPosition: 'right',
          course: {
            name: '',
            score: '',
            comments: ''
          }
        }
      },
      created(){
        //todo 获取当前教师所有课程列表
      },
      methods: {
        addCourse(course){
          if(course && course !== 'add'){
            return;
          }
          this.dialogFormVisible = true;
        },
        addCourseSubmit(){
          //todo 保存课程到数据库
          //todo 添加课程信息到循环显示列表
          this.teacher.courses.splice(0, 0, this.course.name);
          this.modifyFormVisible = false;
        },
        modifyBasicInfo(){
          this.modifyFormVisible = true;
        },
        modifyConfirm(){
          //todo 保存修改后的结果
        },
        modifyCancel(){
          this.modifyFormVisible = false;
        },
      }
    }
</script>

<style scoped>
  .main{
    height: 100%;
    width: 100%;
  }
  .p-info{
    width: 100%;
    height: 200px;
    box-shadow: 0px 0px 9px 4px #a09595;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .p-info-span{
    height: 80%;
    width: 80%;
    float: left;
    display: grid;
    padding: 10px;
    background: yellowgreen;
    box-shadow: 0px 0px 9px 4px #a09595;
  }
  .p-info-edit{
    position: relative;
    right: 25px;
    top: -65px;
  }
  .course-container{
    display:flex;
    align-items: center;
  }
  .course{
    height: 200px;
    width: 200px;
    margin: 10px 0 10px 10px;
    background: #ffb02c;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
  }
  .course:hover{
    box-shadow: 0px 0px 9px 4px #a09595;
  }
</style>
