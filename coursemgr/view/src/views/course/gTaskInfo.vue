<template>
  <div>
    <div class="grouped-item" v-for="gTask in gTasks">
      <!--1. 审阅XXX关于“xxxxxx”任务主观题-->
      审阅<span>{{gTask.from}}</span>关于“<span>{{gTask.taskName}}</span>”任务主观题
      <el-button type="primary" @click="approvalTask(gTask.taskId)">审阅</el-button>
    </div>
  </div>
</template>

<script>
    export default {
        name: "gTaskInfo",
      data(){
          return{
            gTasks:[],
          }
      },
      created(){
          //获取我的任务待办列表
      },
      methods:{
        approvalTask(tId){
          //todo 显示task信息，填写得分
        }
      }
    }
</script>

<style scoped>
  .grouped-item{
    padding: 10px;
    line-height: 30px;
    width: 90%;
    margin: 30px auto;
    display: grid;
    background-color: #FFD04B;
  }
  .grouped-item:hover{
    box-shadow: 0px 0px 10px 4px #3a8ee6;
  }
</style>
