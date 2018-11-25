<template>
  <div class="new-task-container">
    <task-info-new ref="taskInfo"></task-info-new>
    <el-row :gutter="60">
      <el-col :span="8">
        <el-button type="primary" v-if="saveDisabled" class="save-btn" disabled>保 存</el-button>
        <el-button type="primary" v-else class="save-btn" @click="handleSaveTask">保 存</el-button>
      </el-col>
      <el-col :span="8">
        <el-button type="primary" class="save-btn" @click="handleViewTask">预 览</el-button>
      </el-col>
      <el-col :span="8">
        <el-button type="primary" class="save-btn" @click="handleReset">重置</el-button>
      </el-col>
    </el-row>

    <el-dialog :visible.sync="showTaskInfoDialog" width="1240px" title="任务信息" @close="handleCloseDlg">
      <task-detail-comp v-if="showTaskInfoDialog" ref="taskInfoComp" :operateType="operate" :preViewTask="task"></task-detail-comp>
    </el-dialog>
  </div>
</template>

<script>
import { SUBJECT_TYPE, TASK_OPERATOR_TYPE } from '../../utils/statusUtil'
import { saveTask } from '@/api/task'
import TaskInfoNew from '../../components/taskInfo-new'
import taskDetailComp from '@/components/taskDetailComp'

export default {
  name: 'newTask1',
  components: { TaskInfoNew, taskDetailComp },
  data() {
    return {
      operate: TASK_OPERATOR_TYPE.PRE_VIEW,
      showTaskInfoDialog: false,
      SUBJECT_TYPE: SUBJECT_TYPE,
      task: {},
      saveDisabled: false
    }
  },
  // beforeRouteLeave(to, from, next) {

  //   this.$store.getters.visitedViews
  //   debugger
  //   this.handleReset()
  //   next()
  // },
  methods: {
    handleReset() {
      this.$refs.taskInfo.$emit('reset')
    },
    /**
     * handleSaveTask 保存任务
     * */
    handleSaveTask() {
      if (!this.$refs.taskInfo.taskVerify()) {
        return
      }
      this.saveDisabled = true;
      this.task = this.$refs.taskInfo.task;
      let saveData = this.getSaveData();
      saveTask(saveData).then(response => {
        this.saveDisabled = false
        if (response.status === 0) {
          this.$message({
            showClose: true,
            type: 'warning',
            message: response.msg
          })
          return
        }
        // this.task.id = response.data.taskId;
        this.handleReset()
        this.$message({
          showClose: true,
          type: 'success',
          message: '保存成功！'
        })
      })
    },
    /**
     * getSaveData  构造需要保存或者更新的task数据
     * params null
     * return {task:task, questionList:questions} 保存的数据结构
     * */
    getSaveData() {
      let task = {
        id: this.task.id,
        name: this.task.name,
        courseId: this.variables.courseId,
        weight: this.task.weight,
        publishTime: new Date(),
        startTime: this.task.startDate,
        deadline: this.task.inspireDate,
        totalScore: this.task.totalScore,
        markType: this.task.markType
      }
      let questionList = []
      for (let item of this.task.subjects) {
        let optionList = []
        if (item.questionType === SUBJECT_TYPE.CHOOSE) {
          for (let opItem of item.selections) {
            optionList.push(opItem)
          }
        }
        questionList.push({
          taskQuestions: {
            id: item.id,
            stems: item.stem,
            questionType: item.questionType,
            score: item.score,
            answers: item.answer
          },
          optionList: optionList
        })
      }
      return { task: task, questionList: questionList }
    },
    /**
     * handleViewTask 新建预览任务
     * */
    handleViewTask() {
      if (!this.$refs.taskInfo.taskVerify()) {
        return
      }
      this.task = this.$refs.taskInfo.task
      this.showTaskInfoDialog = true
    },
    /**
     * handleCloseDlg 关闭弹窗
     * */
    handleCloseDlg() {
      this.showTaskInfoDialog = false
    }
  }
}
</script>

<style scoped>
.save-btn {
  margin-left: calc(50% - 40px);
  margin-top: 10px;
  margin-bottom: 10px;
}
</style>
<style>
  .new-task-container .el-dialog__header{
    background-color: #009687;
    color: white;
  }
  .el-dialog__close{
   color: white !important;
  }
  .new-task-container {
    margin-top: -25px;
  }
</style>
