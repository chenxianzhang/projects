<template>
  <div>
    <task-info-new ref="taskInfo"></task-info-new>
    <el-button type="primary" class="save-btn" @click="handleSaveTask">保  存</el-button>
  </div>
</template>

<script>
  import {SUBJECT_TYPE} from "../../utils/statusUtil";
  import {saveTask} from '@/api/task'
  import TaskInfoNew from '../../components/taskInfo-new'
    export default {
      name: "newTask1",
      components:{TaskInfoNew},
      data(){
        return {
          SUBJECT_TYPE:SUBJECT_TYPE,
          task:{}
        }
      },
      methods:{
        /**
         * handleSaveTask 保存任务
         * */
        handleSaveTask(){
          if(!this.$refs.taskInfo.taskVerify()){
            return;
          }
          this.task = this.$refs.taskInfo.task;
          let saveData = this.getSaveData();
          debugger
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
          });
        },
        /**
         * getSaveData  构造需要保存或者更新的task数据
         * params null
         * return {task:task, questionList:questions} 保存的数据结构
         * */
        getSaveData(){
          let task = {
            name:this.task.name,
            courseId:this.$route.params.courseId,
            weight: this.task.weight,
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
      }
    }
</script>

<style scoped>
  .save-btn{
    margin-left: calc(50% - 40px);
    margin-top: 10px;
    margin-bottom: 10px;
    width: 80px;
  }
</style>
