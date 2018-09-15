<template>
    <div>
      <h4>当前课程： {{courseName}}</h4>
      <el-table :data="tasks"
                style="width: 100%"
                border
                :header-cell-style="{background:'rgba(28, 77, 125, 0.8)', color:'white', fontWeight:'bold'}">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="name" label="任务名称" align="center"> </el-table-column>
        <el-table-column prop="publishTime" label="发布日期" align="center" :formatter="dateFormat"> </el-table-column>
        <el-table-column prop="startTime" label="开始日期" align="center" :formatter="dateFormat"> </el-table-column>
        <el-table-column prop="deadline" label="截止日期" align="center" :formatter="dateFormat"> </el-table-column>
        <el-table-column prop="finishStatus" label="完成状态"
                         align="center">
          <template slot-scope="scope">
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium" v-if="isStudent&&scope.row.finishStatus==='FINISHED'" type="success">已完成</el-tag>
                <el-tag size="medium" v-if="isStudent&&scope.row.finishStatus==='UNCOMMITTED'" type="danger">待答题</el-tag>
                <el-tag size="medium" v-if="isStudent&&scope.row.finishStatus==='TO_REVIEW'" type="warning">待审批</el-tag>
                <div v-if="!isStudent">
                  <span style="font-weight: bold;">{{scope.row.finishPersonCnt}}</span>/<span>{{scope.row.totalPersonCnt}}</span>
                </div>
              </div>
          </template>
        </el-table-column>
        <el-table-column align="center"
                         label="操作"
                         width="140">
          <template slot-scope="scope">
            <el-button @click="handleDetailClick(scope.row)" type="text" size="small">详情</el-button>
            <el-button v-if="isStudent" :disabled="scope.row.finishStatus!='UNCOMMITTED' || !scope.row.canAnswer" @click="handleAnswerClick(scope.row)" type="text" size="small">答题</el-button>
            <el-button v-if="!isStudent" :disabled="scope.row.startTime < new Date()" @click="handleModifyClick(scope.row)" type="text" size="small">修改</el-button>
            <el-button v-if="!isStudent" @click="handleDeleteClick(scope.row)" type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog :visible.sync="showTaskInfoDialog" width="1240px">
        <task-detail-comp v-if="showTaskInfoDialog" ref="taskInfoComp" :taskId="selectTaskId" :operateType="operate"></task-detail-comp>
      </el-dialog>
      <el-dialog :visible.sync="showTaskStatement" width="1240px">
        <task-info-new v-if="showTaskStatement" ref="taskStatement" :taskId="selectTaskId" :operateType="operate"></task-info-new>

        <div slot="footer" class="dialog-footer">
          <el-button @click="showTaskStatement = false">取 消</el-button>
          <el-button type="primary" @click="handleSaveTask">确 定</el-button>
        </div>
      </el-dialog>
    </div>
</template>

<script>
  import TaskInfo from '../../components/taskInfo'
  import TaskDetailComp from '../../components/taskDetailComp'
  import {getCourseTasksByCourseId, deleteTask, getCourseById} from '@/api/course'
  import {getMyTaskSituation, getCourseTaskSituation} from '@/api/task'
  import {SUBJECT_TYPE, TASK_OPERATOR_TYPE} from "../../utils/statusUtil";
  import TaskInfoNew from "../../components/taskInfo-new";
  import {saveTask} from "../../api/task";

  export default {
      name: "taskInfoList",
      components:{TaskInfoNew, TaskDetailComp, TaskInfo, },
      data(){
          return {
            operate:'',
            isStudent:false,
            showTaskInfoDialog:false,
            showTaskStatement:false,
            selectTaskId:'',
            courseName:'',
            tasks:[],
          }
      },
      created() {
        let self = this;
        let cId = this.$route.params.courseId;
        if (!cId || cId === '') {
          this.$message.warning('无效的课程id');
        }

        getCourseById({courseId:cId})
          .then(resp=>{
            if(resp.status === 0){
              this.$message.warn('获取任务信息失败！');
              return;
            }
            this.courseName = resp.data.name;
          });

        if(this.$store.state.user.roles.in_array('student')){
          this.isStudent = true;
          this.getStuTaskList(cId);
        }
        else if(this.$store.state.user.roles.in_array('teacher')){
          this.isStudent = false;
          this.getTaskList(cId);
        }
      },
      methods: {
        /**
         * handleSaveTask 保存任务
         * */
        handleSaveTask(){
          if(!this.$refs.taskStatement.taskVerify()){
            return;
          }
          this.task = this.$refs.taskStatement.task;
          let saveData = this.getSaveData();
          debugger
          saveTask(saveData).then(response=>{
            debugger
            if(response.status === 0){
              this.$message({
                showClose: true,
                type: 'warning',
                message: response.msg
              });
              return;
            }
            this.$message({
              showClose: true,
              type: 'success',
              message: "保存成功！"
            });
          });
        },
        /**
         * getSaveData  构造需要保存或者更新的task数据
         * params null
         * return {task:task, questionList:questions} 保存的数据结构
         * */
        getSaveData(){
          let task = {
            id:this.task.id,
            name:this.task.name,
            courseId:this.$route.params.courseId,
            weight: this.task.weight,
            publishTime:new Date(),
            startTime:this.task.startDate,
            deadline:this.task.inspireDate,
            totalScore: this.task.totalScore,
            markType:this.task.markType
          };
          let questionList = [];
          for(let item of this.task.subjects){
            let optionList = [];
            if(item.questionType === SUBJECT_TYPE.CHOOSE){
              for(let opItem of item.selections){
                optionList.push(opItem);
              }
            }
            questionList.push({
              taskQuestions:{
                id:item.id,
                stems:item.stem,
                questionType:item.questionType,
                score:item.score,
                answers:item.answer,
              },
              optionList:optionList
            });
          }
          return {task:task, questionList:questionList};
        },
        getStuTaskList(cId){
          getMyTaskSituation({courseId:cId, studentNo:this.$store.state.user.token})
            .then(resp=>{
              if(resp.status === 0){
                this.$message.warn('获取我的任务失败！');
                return;
              }
              for(let item of resp.data){
                this.tasks.push({
                  id:item.taskId,
                  name:item.taskName,
                  publishTime:item.publishTime,
                  startTime:item.startTime,
                  deadline:item.deadline,
                  finishStatus:item.finishStatus
                });
              }
            });
        },
        getTaskList(courseId) {
          let self = this;
          getCourseTaskSituation({courseId: courseId}).then(response => {
            if (response.status === 0) {
              self.$message({
                showClose: true,
                type: 'warning',
                message: response.msg
              });
              return;
            }
            self.tasks = response.data;
          });
        },

        /**
         * 学生答题
         * */
        handleAnswerClick(row) {
          this.selectTaskId = row.id;
          this.showTaskInfoDialog = true;
          this.operate = TASK_OPERATOR_TYPE.STUDENT_ANSWER;
        },
        /**
         * 查看详情
         * */
        handleDetailClick(row) {
          this.selectTaskId = row.id;
          this.showTaskInfoDialog = true;
          if(this.isStudent){
            this.operate = TASK_OPERATOR_TYPE.STUDENT_VIEW_DETAIL;
            return;
          }
          this.operate = TASK_OPERATOR_TYPE.TEACHER_VIEW_DETAIL;
        },
        /**
         * 修改任务
         * */
        handleModifyClick(row) {
          this.selectTaskId = row.id;
          this.showTaskStatement = true;
          this.operate = TASK_OPERATOR_TYPE.TEACHER_STATEMENT;
        },
        /**
         * 删除任务
         * */
        handleDeleteClick(row) {
          let self = this;
        deleteTask({courseId: this.$route.params.courseId,
         taskId: row.id}).then(response => {
            debugger
            if (response.status === 1) {
            self.getTaskList(self.$route.params.courseId);
              self.$message({
                showClose: true,
                type: 'success',
                message: "删除成功"
              });
            } else {
              self.$message({
                showClose: true,
                type: 'warning',
                message: "删除失败"
              });
            }

         }).catch(err => {
            console.log(err);
         })
        },
        /**
         * 时间格式转换
         * */
        dateFormat:function(row, column) {
          let date = row[column.property];
          if (date == undefined || date === '') {
            return "-";
          }
          return new Date(date).toLocaleDateString();
        },
        /**
         * 状态转换Function(row, column, cellValue, index)
         * */
        stateFormat:function(row, column, cellValue, index) {
          if(this.isStudent){
            let status = '<span style="background: #2d7620; color: #ffffff;">已完成</span>';
            if(cellValue === 'UNCOMMITTED'){
              status = '<span style="background: #9b9b9b; color: #ffffff;">未答题</span>'
            }
            else if(cellValue === 'TO_REVIEW'){
              status = '<span style="background: #ee9900; color: #ffffff;">已提交</span>'
            }
            return status;
          }
          else{
            return row.finishPersonCnt + '/' + row.totalPersonCnt
          }
        }
      },
    }
</script>

<style scoped>
.ddd{
  background-color: #ee9900;
}
</style>
