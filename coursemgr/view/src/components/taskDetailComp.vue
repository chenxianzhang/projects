<template>
  <div class="main-frame">
    <div class="task-name-label">
      任务名称：{{task.name}}
    </div>
    <div class="subjectStatic">总题数 {{task.subjects.length}} 道，总分值 {{task.totalScore}} 分，权重：{{task.weight}} %。</div>
    <div class="subject-container">
      <div class="subject-item" v-for="(item, index) in task.subjects"
           style="padding-bottom: 10px; border-bottom: 1px solid #dff0d8">
        <!--题干设置区域-->
        <div style="margin-bottom:10px">
          <span>{{index + 1}}.</span>
          <el-input v-html="item.stem" style="width: calc(100% - 240px)"></el-input>
          分数：<el-input v-html="item.score" style="width: 40px; height: 30px;" />分
        </div>
        <!--单选题、判断题 选项设置区域-->
        <div v-if="item.questionType === SUBJECT_TYPE.CHOOSE">
            <el-radio-group  v-model="item.answer" style="display: grid" disabled>
              <el-radio v-for="(cItem, cIndex) in item.selections" :label="cItem.optionDes" :key="cIndex" style="margin: 5px;">
                <span v-html="cItem.optionDes"></span>
              </el-radio>
            </el-radio-group>
        </div>
        <!--单选题、判断题 选项设置区域-->
        <div v-if="item.questionType === SUBJECT_TYPE.JUDGE">
          <el-radio-group  v-model="item.answer" style="display: grid" disabled>
            <el-radio label="是" style="margin: 5px;">
            </el-radio>
            <el-radio label="否" style="margin: 5px;">
            </el-radio>
          </el-radio-group>
        </div>
        <!--主观题 答题 设置区域-->
        <div v-if="item.questionType === SUBJECT_TYPE.SUBJECTIVE">
          <!--主观题 答题-->
          <el-input type="textarea" v-if="operateType===TASK_OPERATOR_TYPE.STUDENT_ANSWER"
                    v-model="item.answer" style="width: calc(100% - 100px)"></el-input>

          <!--主观题 查看-->
          <el-input v-if="operateType!==TASK_OPERATOR_TYPE.STUDENT_VIEW_DETAIL"
                    v-html="item.answer" style="width: calc(100% - 100px)"></el-input>

          <div v-if="operateType===TASK_OPERATOR_TYPE.STUDENT_VIEW_DETAIL" style="width: 100px">
            评分：<input style="width: 40px; height: 30px;" min="0" :max="item.score" v-model="item.score" disabled />
          </div>
          <div v-if="operateType===TASK_OPERATOR_TYPE.MARK_POINT" style="width: 100px">
            评分：<input style="width: 40px; height: 30px;" min="0" :max="item.score" v-model="item.score" />
          </div>
        </div>
        <!--编辑和完成编辑按钮-->
        <el-button type="primary" v-if="operateType === TASK_OPERATOR_TYPE.STUDENT_ANSWER"
                   @click="handleSubjectSubmit">提交</el-button>
      </div>
    </div>
  </div>
</template>

<script>
  import {SUBJECT_TYPE, TASK_OPERATOR_TYPE} from '../utils/statusUtil'
  import {Subject, Task, Selection} from "../models/task-model";
  import {getTaskDetailByTaskId} from '@/api/task'

    export default {
      name: "taskDetailComp",
      props:['operateType', 'taskId'],
      data(){
        return{
          SUBJECT_TYPE:SUBJECT_TYPE,
          TASK_OPERATOR_TYPE:TASK_OPERATOR_TYPE,
          task: new Task(),
          taskStudent: new Task()
        }
      },
      created(){
        //获取任务详情
        if(!this.taskId || this.taskId === ''){
          return;
        }
        //获取任务信息
        getTaskDetailByTaskId({taskId: this.taskId})
          .then(resp=>{
            if(resp.status === 0){
              this.$message.warning('获取任务信息失败');
              return;
            }
            //根据结果设置subject值
            console.log('resp.data' + resp.data)
            this.setSubjectByTaskDetailInfo(resp.data);
          });
        //学生查看任务详情
        if(this.operateType === TASK_OPERATOR_TYPE.STUDENT_VIEW_DETAIL){
          //todo 获取学生答题情况
        }
      },
      methods:{
        /**
         * 根据接口返回的任务信息结果，构造subject对象的值
         * params taskDetailInfo
         * return null
         **/
        setSubjectByTaskDetailInfo(taskDetailInfo){
          if(!taskDetailInfo){
            return;
          }
          this.task.name = taskDetailInfo.task.name;
          this.task.weight = taskDetailInfo.task.weight;
          this.task.totalScore = taskDetailInfo.task.totalScore;
          this.task.inspireDate = taskDetailInfo.task.deadline;
          this.task.markType = taskDetailInfo.task.markType;
          for(let item of taskDetailInfo.questionList){
            // switch (item.taskQuestions.questionType) {
            //   case SUBJECT_TYPE.CHOOSE://单选题
                let subject_c = new Subject();
                subject_c.id = item.taskQuestions.id;
                subject_c.edit = false;
                subject_c.selections = item.optionList;
                subject_c.answer = item.taskQuestions.answers;
                subject_c.score = item.taskQuestions.score;
                console.log(item.taskQuestions.score)
                subject_c.stem = item.taskQuestions.stems;
                subject_c.questionType = item.taskQuestions.questionType;
                this.task.subjects.push(subject_c);
            //     break;
            //   case SUBJECT_TYPE.SUBJECTIVE://主观题
            //     // this.subject.subjectForJudge.push({id:item.id, stem:item.stems, chooseItem:["是","否"], answer:item.answers});
            //     // this.subject.subjectForJudgeScore = item.score;
            //     break;
            //   case SUBJECT_TYPE.JUDGE://判断题
            //     // this.subject.subjectForSubjective.push({id:item.id, stem:item.stems, chooseItem:[], answer:item.answers, score: item.score});
            //     break;
            // }
          }
        },
        /**
         * 学生答题  提交答案
         * */
        handleSubjectSubmit(){
          //学生答题
          if(this.operateType === TASK_OPERATOR_TYPE.STUDENT_ANSWER){

          }
        },
      },
    }
</script>

<style scoped>
  .main-frame{
    width: 1200px;
    margin: 0 auto;
    border: 1px solid #ee9900;
    padding: 5px;
  }

  .task-name-label{
    line-height: 40px;
  }
</style>
