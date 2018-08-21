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
      <div class="p-info-edit">
        编辑
      </div>
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
        <el-button type="primary" @click="addCourse">添加课程</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

    export default {
      name: "teacherHome",
      data(){
        return{
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
      methods: {
        addCourse(course){
          if(course && course !== 'add'){
            return;
          }
          this.dialogFormVisible = true;
        }
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
    height: 300px;
    box-shadow: 0px 0px 0px 4px gray;
  }
  .p-info-span{
    height: 100%;
    width: 90%;
    float: left;
  }
  .p-info-edit{
    height: 100%;
    width: 10%;
    float: left;
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
    box-shadow: 0px 0px 0px 4px gray;
  }
</style>
