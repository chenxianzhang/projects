<template>
  <div>
    <h4>课程名:{{courseName}}</h4>
    <el-table :data="tasks" style="width: 100%">
      <el-table-column prop="taskName" label="姓名  "> </el-table-column>
      <el-table-column prop="publishDate" label="学号"> </el-table-column>
      <el-table-column prop="groupName" label="所在小组"> </el-table-column>
      <el-table-column prop="task1" label="任务1"> </el-table-column>
      <el-table-column prop="task2" label="任务2"> </el-table-column>
      <el-table-column prop="task3" label="任务3"> </el-table-column>
      <el-table-column prop="task4" label="任务4"> </el-table-column>
      <el-table-column prop="totalScore" label="加权总分"> </el-table-column>
    </el-table>
  </div>
</template>

<script>
    export default {
        name: "gradeInfo",
      data(){
        return {
          courseName:'xxx课程',
          tasks:[{taskId:'1', taskName:'1', publishDate:'1', expiryDate:'1', finishState:'1'},
            {taskId:'2', taskName:'2', publishDate:'2', expiryDate:'2', finishState:'2'}],
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

</style>
