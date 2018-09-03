<template>
  <div class="main-container">
      <div class="step">{{currentStep}}</div>
      <task-info :taskId="taskId" :operate="TASK_OPERATOR_TYPE.TEACHER_STATEMENT"></task-info>
  </div>
</template>

<script>
  import TaskInfo from '../../components/taskInfo'
  import {TASK_OPERATOR_TYPE} from "../../utils/statusUtil";

  export default {
      name: "newTask",
      components:{TaskInfo},
      data(){
          return {
            TASK_OPERATOR_TYPE:TASK_OPERATOR_TYPE,
            currentStep: '第一步：编辑题目',
            taskId:''
          }
      },
      created(){

      },
      methods:{

      },
    }
</script>

<style scoped>
  .main-container .step{
    height: 40px;
    line-height: 40px;
    width: 150px;
    background: #456;
    color: #fff;
    text-align: center;
    box-shadow: 0 0 5px 4px #35506b;
    margin-bottom: 10px;
  }
</style>
