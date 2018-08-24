<template>
  <div>
    <h4>课程名:{{courseName}}</h4>
    <el-table :data="tasks" style="width: 100%">
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="name" label="姓名  "> </el-table-column>
      <el-table-column prop="no" label="学号"> </el-table-column>
      <el-table-column prop="groupName" label="所在小组"> </el-table-column>

      <el-table-column v-for="(task,index) in tttasks" :label="task.name" :key="index">
        <template slot-scope="scope">
          <span style="cursor: pointer; text-decoration: underline; font-weight: bold;" @click="handleClick">{{ task.score }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="totalScore" label="加权总分"> </el-table-column>
    </el-table>
    <div class="buttons">
      <el-button class="primary">导出为excel</el-button>
      <el-button class="primary">过程性打包</el-button>
    </div>
    <!--查看当前任务答题情况-->
    <el-dialog :visible.sync="showAnswer">
      <upload-answer></upload-answer>
    </el-dialog>
  </div>
</template>

<script>
  import UploadAnswer from '../teacherHome/uploadAnswer'

    export default {
      name: "gradeInfo",
      components:{UploadAnswer},
      data(){
        return {
          showAnswer: false,
          isStudent:false,
          courseName:'xxx课程',
          tasks:[{taskId:'1', name:'1', no:'1', groupName:'1', task1:'1', task2:'1', task3:'1', task4:'1', totalScore:'1', xxx任务1:13, xxx任务2:34, xxx任务3:78},
            {taskId:'2', name:'2', no:'2', groupName:'2', task1:'2', task2:'2', task3:'2', task4:'2', totalScore:'2', xxx任务1:13, xxx任务2:34, xxx任务3:78}],
          tttasks:[{name:'xxx任务1',score:13}, {name:'xxx任务2',score:15}, {name:'xxx任务3',score:78}],
        }
      },
      created(){
          this.isStudent = this.$store.state.user.roles.in_array('student');
          let cId = this.$store.getters.courseId;
          if(this.isStudent){
            //todo 获取该学生该课程的所有任务的成绩
            return;
          }
          //todo 获取当前课程所有学生的成绩
      },
      methods: {
        handleClick(row) {
          this.showAnswer = true;
          //todo 通过任务id和学生id，获取任务题目信息
          console.log(row);
        }
      },
    }
</script>

<style scoped>
  .buttons{
    margin-top: 10px;
  }

  .buttons > div{
    display: inline-block;
    margin: 0 5px;
  }
</style>
