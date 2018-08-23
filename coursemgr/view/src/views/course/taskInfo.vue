<template>
    <div>
      <h4>课程名:{{courseName}}</h4>
      <el-table :data="tasks" style="width: 100%">
        <el-table-column prop="taskName" label="任务名称"> </el-table-column>
        <el-table-column prop="publishDate" label="发布日期"> </el-table-column>
        <el-table-column prop="expiryDate" label="截止日期"> </el-table-column>
        <el-table-column prop="finishState" label="完成状态"> </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
</template>

<script>
    export default {
        name: "taskInfo",
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
