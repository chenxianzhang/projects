<template>
    <div>
      <!--<div class="title">-->
        <!--<i class="custom-icon-info"></i>-->
        <!--<span class="zh">当前课程：{{courseName}}</span>-->
        <!--<span class="en">THE CURRENT COURSE</span>-->
      <!--</div>-->
      <el-table :data="tasks"
                style="width: 100%"
                :header-cell-style="{background:'rgba(28, 77, 125, 0.8)', color:'white', fontWeight:'bold'}">
        <el-table-column type="index" width="55" align="center"></el-table-column>
        <el-table-column prop="name" label="任务名称" align="center"> </el-table-column>
        <el-table-column prop="publishTime" label="发布日期" align="center" :formatter="dateFormat"> </el-table-column>
        <el-table-column prop="startTime" label="开始日期" align="center" :formatter="dateFormat"> </el-table-column>
        <el-table-column prop="deadline" label="截止日期" align="center" :formatter="dateFormat"> </el-table-column>
        <el-table-column prop="finishStatus" label="完成状态" align="center">
          <template slot-scope="scope">
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium" v-if="isStudent&&scope.row.finishStatus==='FINISHED'" type="success">已完成</el-tag>
                <el-tag size="medium" v-if="isStudent&&scope.row.finishStatus==='UNCOMMITTED'" type="danger">待答题</el-tag>
                <el-tag size="medium" v-if="isStudent&&scope.row.finishStatus==='TO_REVIEW'" type="warning">待审批</el-tag>
                <el-tag v-if="!isStudent" size="medium" type="success">
                  <div style="cursor: pointer;" @click="handleViewFinishState(scope.row)">
                    <span style="font-weight: bold;">{{scope.row.finishPersonCnt}}</span>/<span>{{scope.row.totalPersonCnt}}</span>
                  </div>
                </el-tag>
              </div>
          </template>
        </el-table-column>
        <el-table-column align="center"
                         label="操作"
                         width="140">
          <template slot-scope="scope">
            <!-- <div class="operator">
              <i class="operator-icon-delete" @click="handleDeleteClick(scope.row)"></i>
            </div> -->
            <el-tooltip content="详情" placement="top">
              <el-button @click="handleDetailClick(scope.row)" type="text" size="small">
                <i class="operator-icon-detail"></i>
              </el-button>
            </el-tooltip>

            <el-tooltip content="答题" placement="top">
              <el-button v-if="isStudent"
                         :disabled="scope.row.finishStatus!=='UNCOMMITTED' || !scope.row.canAnswer"
                         @click="handleAnswerClick(scope.row)" type="text" size="small">
                <i :class="scope.row.finishStatus!=='UNCOMMITTED' || !scope.row.canAnswer ? 'operator-icon-answer-not' : 'operator-icon-answer'"></i>
              </el-button>
            </el-tooltip>

            <el-tooltip content="编辑" placement="top">
              <el-button v-if="!isStudent" :disabled="scope.row.startTime < new Date()"
                         @click="handleModifyClick(scope.row)" type="text" size="small">
                <i :class="scope.row.startTime < new Date() ? 'operator-icon-edit-not' : 'operator-icon-edit'"></i>
              </el-button>
            </el-tooltip>

            <el-tooltip content="删除" placement="top">
              <el-button v-if="!isStudent" @click="handleDeleteClick(scope.row)" type="text" size="small">
                <i class="operator-icon-delete"></i>
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog :visible.sync="showTaskInfoDialog" width="1240px" title="任务信息">
        <task-detail-comp v-if="showTaskInfoDialog"
                          ref="taskInfoComp"
                          :taskId="selectTaskId"
                          :operateType="operate" @answerEmit="handleAnswerEmit"></task-detail-comp>
      </el-dialog>
      <el-dialog :visible.sync="showTaskStatement" width="1240px" title="修改任务">
        <task-info-new v-if="showTaskStatement"
                       ref="taskStatement"
                       :taskId="selectTaskId"
                       :operateType="operate" ></task-info-new>

        <div slot="footer" class="dialog-footer">
          <el-button @click="showTaskStatement = false">取 消</el-button>
          <el-button type="primary" @click="handleSaveTask">确 定</el-button>
        </div>
      </el-dialog>
      <el-dialog v-if="showTaskFinishStateDlg" :visible.sync="showTaskFinishStateDlg" width="1240px" title="任务完成情况">
        <el-row>
          <span style="margin-right: 20px">课程名：{{curTaskRowData.name}}</span><span>截止日期：{{new Date(curTaskRowData.deadline).toLocaleDateString()}}</span>
        </el-row>
        <el-row style="margin-top: 10px">
          <el-table :data="taskFinishStateList" style="width: 100%"
                    :header-cell-style="{background:'rgba(28, 77, 125, 0.8)', color:'white', fontWeight:'bold'}">
            <el-table-column type="index" label="序号" width="55" align="center"></el-table-column>
            <el-table-column prop="studentName" label="姓名" align="center"> </el-table-column>
            <el-table-column prop="studentNo" label="学号" align="center"> </el-table-column>
            <el-table-column prop="statusText" label="状态" align="center"></el-table-column>
            <el-table-column prop="reviewer" label="评阅人" align="center"> </el-table-column>
          </el-table>
        </el-row>
      </el-dialog>
    </div>
</template>

<script>
  import TaskDetailComp from '../../components/taskDetailComp'
  import {getCourseTasksByCourseId, deleteTask, getCourseById} from '@/api/course'
  import {getMyTaskSituation, getCourseTaskSituation} from '@/api/task'
  import {SUBJECT_TYPE, TASK_OPERATOR_TYPE} from "../../utils/statusUtil";
  import TaskInfoNew from "../../components/taskInfo-new";
  import { saveTask, getTaskSituationList } from "../../api/task";

  export default {
      name: "taskInfoList",
      components:{TaskInfoNew, TaskDetailComp },
      data(){
          return {
            operate:'',
            isStudent:false,
            showTaskInfoDialog:false,
            showTaskStatement:false,
            showTaskFinishStateDlg:false,//是否显示【任务完成情况】弹窗
            selectTaskId:'',
            courseName:'',
            tasks:[],
            curTaskRowData:null,//当前选择的任务 行数据
            taskFinishStateList:[],//当前任务完成情况列表
          }
      },
      created() {
        let self = this;
        let cId = this.variables.courseId;
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
        handleViewFinishState(rowData){
          this.taskFinishStateList = [];
          this.curTaskRowData = rowData;
          let params = {taskId:rowData.id, courseId:rowData.courseId, pageSize: 200, currPage: 1};
          getTaskSituationList(params).then(resp=>{
            if(resp.status === 0){
              this.$message.warning('获取任务完成情况失败！');
              console.log(resp.msg);
              return;
            }
            this.taskFinishStateList = resp.data.pageData;
          });
          this.showTaskFinishStateDlg = true;
          console.log(rowData)
          console.log('查看完成情况')
        },
        /**
         * handleSaveTask 保存任务
         * */
        handleSaveTask(){
          if(!this.$refs.taskStatement.taskVerify()){
            return;
          }
          this.task = this.$refs.taskStatement.task;
          let saveData = this.getSaveData();
          saveTask(saveData).then(response=>{
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
            this.getTaskList(this.variables.courseId);
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
            courseId:this.variables.courseId,
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
              this.tasks = [];
              for(let item of resp.data){
                this.tasks.push({
                  id:item.taskId,
                  name:item.taskName,
                  publishTime:item.publishTime,
                  startTime:item.startTime,
                  deadline:item.deadline,
                  finishStatus:item.finishStatus,
                  canAnswer: item.canAnswer
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
         * 学生答题 成功提交
         * */
        handleAnswerEmit() {
          this.showTaskInfoDialog = false;
          this.getStuTaskList(this.variables.courseId);
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
          this.$confirm('确认删除此任务?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(()=>{
            deleteTask({courseId: this.variables.courseId, taskId: row.id})
              .then(response => {
                if (response.status === 1) {
                  self.getTaskList(self.variables.courseId);
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
              })
              .catch(err => {
                console.log(err);
              })
          }).catch(err => {
            console.log(err);
          });

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

<style lang="scss" scoped>
.title{
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  i {
    margin-right: 10px;
  }
  .zh {
    margin-right: 20px;
    color: #000;
  }
  .en {
    color: #009788;
    margin-top: 2px;
  }
}
</style>

