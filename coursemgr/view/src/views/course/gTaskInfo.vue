<template>
  <div class="gtask-panel">
    <div v-if="gTasks.length === 0" class="grouped-item" style="text-align: center">
      暂无数据！
    </div>
    <div class="grouped-item" v-for="gTask in gTasks">
      <!--1. 审阅XXX关于“xxxxxx”任务主观题-->
      <div style="margin-left: 40px">
        审阅
        <span class="span-hight-light"> {{gTask.targetSerialName}} </span>
        关于<span class="span-hight-light"> {{gTask.taskName}} </span>任务的主观题
      </div>
      <el-button style="margin-right: 20px" type="primary" @click="approvalTask(gTask.taskId, gTask.targetSerialNo)">审阅</el-button>
    </div>
    <el-dialog :visible.sync="showTaskInfoDialog" width="1240px" title="任务评审">
      <task-detail-comp v-if="showTaskInfoDialog" ref="taskInfoComp"
                        :taskId="selectTaskId"
                        :operateType="operateType"
                        :markUid="markUid" @closeDlg="closeDlg"></task-detail-comp>
    </el-dialog>
  </div>
</template>

<script>
  import {TASK_OPERATOR_TYPE} from "../../utils/statusUtil";
  import {getMySchedule, checkStuTaskFinished} from '@/api/gTasks'

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
      this.getData();
    },
    methods:{
      getData(){
        //获取我的任务待办列表
        getMySchedule({courseId:this.variables.courseId, studentNo:this.$store.state.user.token})
          .then(resp=>{
            if(resp.status === 0){
              this.$message.warn('获取我的待办任务失败！');
              return;
            }
            this.gTasks = resp.data;
          });
      },
      approvalTask(tId, markUid){

        // 判断当前学生该任务是否已经完成
        checkStuTaskFinished({ studentNo: this.$store.state.user.token, taskId:tId }).then(res => {
          if (res.status === 0) {
            this.$msg.warn(res.msg)
            return
          }
          if (res.data === true) {
            //显示task信息，填写得分
            this.selectTaskId = tId;
            this.operateType = TASK_OPERATOR_TYPE.MARK_POINT;
            this.markUid = markUid;
            this.showTaskInfoDialog = true;
          } else {
            this.$msg.warning('当前任务未完成，不能对该学生的任务进行审阅操作')
          }
        }).catch (err => {
          this.$msg.error(err)
        })
      },
      closeDlg(){
        this.showTaskInfoDialog = false;
        this.getData();
      }
    }
  }
</script>

<style scoped>
  .span-hight-light{
    background-color: #FE9226;
    color: white;
    padding: 5px 20px;
    border-radius: 10px;
    margin: 0 10px;
    font-weight: bold;
  }
  .gtask-panel .grouped-item{
    height: 101px;
    border: 6px solid #E8EBF0;
    margin: 17px auto;
    background-color: white;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .gtask-panel .grouped-item > span{
    font-weight: bold;
    font-family: cursive;
    color: #e6a23c;
  }
  .gtask-panel .grouped-item:hover{
    /*box-shadow: 0px 0px 10px 4px #3a8ee6;*/
    border-color: #9AEDE5;
  }
  .gtask-panel .el-button--primary{
    float: right;
  }
  .gtask-panel {
      margin-top: -20px;
  }
  .container-scrollbar{
    background-color: #F2F2F2;
  }
</style>
