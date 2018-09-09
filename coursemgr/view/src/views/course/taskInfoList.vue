<template>
    <div>
      <h4>课程名:{{courseName}}</h4>
      <el-table :data="tasks" style="width: 100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="name" label="任务名称"> </el-table-column>
        <el-table-column prop="publishTime" label="发布日期" :formatter="dateFormat"> </el-table-column>
        <el-table-column prop="startTime" label="开始日期" :formatter="dateFormat"> </el-table-column>
        <el-table-column prop="deadline" label="截止日期" :formatter="dateFormat"> </el-table-column>
        <el-table-column prop="finishState" label="完成状态"> </el-table-column>
        <el-table-column align="center"
                         label="操作"
                         width="140">
          <template slot-scope="scope">
            <el-button @click="handleDetailClick(scope.row)" type="text" size="small">详情</el-button>
            <el-button v-if="isStudent" @click="handleAnswerClick(scope.row)" type="text" size="small">答题</el-button>
            <el-button v-if="!isStudent" @click="handleModifyClick(scope.row)" type="text" size="small">修改</el-button>
            <el-button v-if="!isStudent" @click="handleDeleteClick(scope.row)" type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog :visible.sync="showTaskInfoDialog" width="1240px">
        <task-detail-comp v-if="showTaskInfoDialog" ref="taskInfoComp" :taskId="selectTaskId" :operateType="operate"></task-detail-comp>
      </el-dialog>
    </div>
</template>

<script>
  import TaskInfo from '../../components/taskInfo'
  import TaskDetailComp from '../../components/taskDetailComp'
  import {getCourseTasksByCourseId, deleteTask} from '@/api/course'
  import {TASK_OPERATOR_TYPE} from "../../utils/statusUtil";

  export default {
      name: "taskInfoList",
      components:{TaskDetailComp, TaskInfo, },
      data(){
          return {
            operate:'',
            isStudent:false,
            showTaskInfoDialog:false,
            selectTaskId:'',
            courseName:'xxx课程',
            tasks:[],
          }
      },
      created() {
        if(this.$store.state.user.roles.in_array('student')){
          this.isStudent = true;
        }
        else if(this.$store.state.user.roles.in_array('teacher')){
          this.isStudent = false;
        }
        let self = this;
        let cId = this.$route.params.courseId;
        if (!cId || cId === '') {
          this.$message.warning('无效的课程id');
        }
        this.getTaskList(cId);
      },
      methods: {

      getTaskList(courseId) {
        let self = this;
        getCourseTasksByCourseId({courseId: courseId}).then(response => {
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
          this.operate = TASK_OPERATOR_TYPE.TEACHER_VIEW_DETAIL;
        },
        /**
         * 修改任务
         * */
        handleModifyClick(row) {
          this.selectTaskId = row.id;
          this.showTaskInfoDialog = true;
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
        }
      },
    }
</script>

<style scoped>

</style>
