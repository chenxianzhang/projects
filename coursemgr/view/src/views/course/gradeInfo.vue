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
          <span>{{ task.score }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="task1" label="任务1"></el-table-column>
      <el-table-column prop="task2" label="任务2"> </el-table-column>
      <el-table-column prop="task3" label="任务3"> </el-table-column>
      <el-table-column prop="task4" label="任务4"> </el-table-column>
      <el-table-column prop="totalScore" label="加权总分"> </el-table-column>
    </el-table>
    <div class="buttons">
      <el-button class="primary">导出为excel</el-button>
      <el-button class="primary">过程性打包</el-button>
    </div>
  </div>
</template>

<script>
    export default {
        name: "gradeInfo",
      data(){
        return {
          courseName:'xxx课程',
          tasks:[{taskId:'1', name:'1', no:'1', groupName:'1', task1:'1', task2:'1', task3:'1', task4:'1', totalScore:'1', xxx任务1:13, xxx任务2:34, xxx任务3:78},
            {taskId:'2', name:'2', no:'2', groupName:'2', task1:'2', task2:'2', task3:'2', task4:'2', totalScore:'2', xxx任务1:13, xxx任务2:34, xxx任务3:78}],
          tttasks:[{name:'xxx任务1',score:13}, {name:'xxx任务2',score:15}, {name:'xxx任务3',score:78}],
        }
      },
      methods: {
        handleClick(row) {
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
