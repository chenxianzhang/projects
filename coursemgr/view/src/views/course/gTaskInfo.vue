<template>
  <div class="gtask-panel">
    <div v-if="gTasks.length === 0" class="grouped-item" style="text-align: center">
      暂无数据！
    </div>
    <div class="grouped-item" v-for="gTask in gTasks">
      <!--1. 审阅XXX关于“xxxxxx”任务主观题-->
      审阅<span> {{gTask.targetSerialName}} </span>关于“<span> {{gTask.taskName}} </span>”任务主观题
      <el-button type="primary" @click="approvalTask(gTask.taskId, gTask.targetSerialNo)">审阅</el-button>
    </div>
    <el-dialog :visible.sync="showTaskInfoDialog" width="1240px" title="任务评审">
      <task-detail-comp v-if="showTaskInfoDialog" ref="taskInfoComp"
                        :taskId="selectTaskId"
                        :operateType="operateType"
                        :markUid="markUid"></task-detail-comp>
    </el-dialog>
  </div>
</template>

<script>
  import {TASK_OPERATOR_TYPE} from "../../utils/statusUtil";
  import {getMySchedule} from '@/api/gTasks'

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
            markUid:'',
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
        approvalTask(tId, markUid){
          //显示task信息，填写得分
          this.selectTaskId = tId;
          this.operateType = TASK_OPERATOR_TYPE.MARK_POINT;
          this.markUid = markUid;
          this.showTaskInfoDialog = true;
        }
      }
    }
</script>

<style scoped>
  .gtask-panel .grouped-item{
    padding: 10px;
    line-height: 30px;
    width: 90%;
    margin: 30px auto;
    background-color: rgba(84, 92, 100, 0.8);
    border-radius: 4px;
    color: #fafff8;
  }
  .gtask-panel .grouped-item > span{
    font-weight: bold;
    font-family: cursive;
    color: #e6a23c;
  }
  .gtask-panel .grouped-item:hover{
    box-shadow: 0px 0px 10px 4px #3a8ee6;
  }
  .gtask-panel .el-button--primary{
    float: right;
  }
</style>
