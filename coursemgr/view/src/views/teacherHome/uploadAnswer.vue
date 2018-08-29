<template xmlns="http://www.w3.org/1999/html">
  <div>
    <div class="step">{{currentStep}}</div>
    <task-info :taskId="taskId"></task-info>
  </div>
</template>

<script>
  import TaskInfo from '../../components/taskInfo'
  export default {
    name: "uploadAnswer",
    components:{TaskInfo},
    data(){
      return {
        currentStep:'第二步：上传答案',
        taskId:'',
      }
    },
    created(){
      //截取路由传过来的数据
      this.taskId = this.$route.params.taskId;
    },
    methods:{

    },
  }
</script>

<style scoped>
  .step{
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
