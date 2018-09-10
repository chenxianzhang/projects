<template>
  <div>
    <div class="grouped-item" v-for="gTask in gTasks">
      <!--1. 审阅XXX关于“xxxxxx”任务主观题-->
      审阅<span>{{gTask.targetSerialName}}</span>关于“<span>{{gTask.taskName}}</span>”任务主观题
      <el-button type="primary" @click="approvalTask(gTask.taskId)">审阅</el-button>
    </div>
    <el-dialog :visible.sync="showTaskInfoDialog" width="1240px">
      <task-detail-comp v-if="showTaskInfoDialog" ref="taskInfoComp" :taskId="selectTaskId" :operateType="operateType"></task-detail-comp>
    </el-dialog>
  </div>
</template>

<script>
  import {TASK_OPERATOR_TYPE} from "../../utils/statusUtil";
  import {getMySchedule} from '@/api/gtasks'

  import TaskDetailComp from '../../components/taskDetailComp'

    export default {
      name: "gTaskInfo",
      components:{
        TaskDetailComp
      },
      data(){
          return{
            operateType:'',
            selectTaskId:'',
            TASK_OPERATOR_TYPE:TASK_OPERATOR_TYPE,
            showTaskInfoDialog:false,
            gTasks:[],
          }
      },
      created(){
        // private Integer taskId;
        //
        // private String taskName;
        //
        // private String markPersonSerialNo;
        //
        // private String markPersonName;
        //
        // private String targetSerialNo;
        //
        // private String targetSerialName;
          //获取我的任务待办列表
          getMySchedule({courseId:this.$route.params.courseId, studentNo:this.$store.state.user.token})
            .then(resp=>{
              if(resp.status === 0){
                this.$message.warn('获取我的待办任务失败！');
                return;
              }
              this.gTasks = resp.data;
            });
      },
      methods:{
        approvalTask(tId){
          //显示task信息，填写得分
          this.selectTaskId = tId;
          this.operateType = this.TASK_OPERATOR_TYPE.MARK_POINT;
          this.showTaskInfoDialog = true;
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
