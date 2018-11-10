<template>
  <div class="main-frame">
    <div class="task-name-label">
      任务名称：<span>{{task.task.name}}</span>
    </div>
    <div class="subjectStatic">总题数 {{task.questionList.length}} 道，总分值 {{task.task.totalScore}} 分。
      <span v-if="hasSubject" style="margin-left: 30px; display: inline-block;">评分方式：
        <span style="color: #ee5134;">{{task.task.markType === 'GROUP_INNER_EVA' ? '组内互评' : (task.task.markType === 'SELF_EVA' ? '自主评分' : '组间互评')}}</span>
      </span>
    </div>
    <div class="subject-container">
      <div class="subject-item" v-for="(item, index) in task.questionList"
           style="padding-bottom: 10px; border-bottom: 1px solid #dff0d8">
        <!--题干设置区域-->
        <div style="margin-bottom:10px; line-height: 32px; display: flex">
          <span style="display: inline-block; width: 20px">{{index + 1}}.</span>
          <el-input v-html="item.taskQuestions.stems" style="width: calc(100% - 20px)"></el-input>
        </div>
        <!--单选题 选项设置区域-->
        <div v-if="item.taskQuestions.questionType === SUBJECT_TYPE.CHOOSE">
          <el-radio-group v-model="item.taskQuestions.answers"
                          style="display: grid"
                          :disabled="true">
            <el-radio v-for="(cItem, cIndex) in item.optionList"
                      :label="cItem.optionTag"
                      :key="cIndex"
                      style="margin: 5px;">
              <span>{{cItem.optionTag}}. </span>
              <span v-html="cItem.optionDes"></span>
            </el-radio>
          </el-radio-group>
          <div class="score-label">得分：<el-input v-html="item.taskQuestions.score" style="width: 20px; height: 30px;"/>分</div>
        </div>
        <!--判断题 选项设置区域-->
        <div v-if="item.taskQuestions.questionType === SUBJECT_TYPE.JUDGE">
          <el-radio-group v-model="item.taskQuestions.answers" style="display: grid" disabled>
            <el-radio label="是" style="margin: 5px;">
            </el-radio>
            <el-radio label="否" style="margin: 5px;">
            </el-radio>
          </el-radio-group>
          <div class="score-label">得分：<el-input v-html="item.taskQuestions.score" style="width: 20px; height: 30px;"/>分</div>
        </div>
        <!--主观题 答题 设置区域-->
        <div v-if="item.taskQuestions.questionType === SUBJECT_TYPE.SUBJECTIVE">
          <!--主观题 答题-->
          <el-input v-html="item.taskQuestions.answers"
                    style="min-height: 78px; overflow-y: auto; border: 1px solid #DFDFDF; background-color: #F6F6F6;"></el-input>
          <!--<div>得分：{{item.taskQuestions.score}} <span style="color: orangered;" v-if="item.taskQuestions.teacherScore !== ''">作废</span></div>-->
          <!--<div>评阅人：{{task.markUser && task.markUser.name}}</div>-->
          <!--<div style="margin-top: 10px" v-if="showCxdfInput || item.taskQuestions.teacherScore !== ''">-->
          <!--得分：<input v-model="item.taskQuestions.teacherScore" :disabled="!showCxdfInput"-->
          <!--type="number" min="0" :max="item.taskQuestions.questionScore" style="height: 30px; width: 30px"/>-->
          <!--</div>-->
          <div class="score-label">
            <span v-if="item.taskQuestions.questionType === SUBJECT_TYPE.SUBJECTIVE">满分：{{item.taskQuestions.questionScore}} 分</span>
            <span>得分：<el-input v-html="item.taskQuestions.score" style="width: 20px; height: 30px;"/>分</span>
            <span style="color: #FE9226;" v-if="item.taskQuestions.teacherScore !== ''">作废</span>
            <span>评阅人：{{task.markUser && task.markUser.name}}</span>
          </div>
          <div style="margin-top: 10px" v-if="showCxdfInput || item.taskQuestions.teacherScore !== ''">
            得分：<input v-model="item.taskQuestions.teacherScore" :disabled="!showCxdfInput"
                      type="number" min="0" :max="item.taskQuestions.questionScore"
                      @blur="handleBlur($event, item.taskQuestions.questionScore)" style="height: 30px; width: 40px"/>
          </div>
        </div>
      </div>
    </div>
    <el-row :gutter="20" v-if="!isStudent && hasSubject && task.status === 'FINISHED'">
      <el-col>
        <el-button type="primary" @click="cxdf">重新评阅</el-button>
        <el-button type="primary" @click="cxdfSubmit" :disabled="canSubmit">提 交</el-button>
      </el-col>
      <el-col></el-col>
    </el-row>
  </div>
</template>

<script>
  import { updateSubjectScore } from '@/api/grade'
  import {getStuTaskDetail} from '@/api/task'
  import {SUBJECT_TYPE} from '../utils/statusUtil'

  export default {
    name: "gradeDetail",
    props:['taskId', 'uId'],
    data(){
      return{
        isStudent:false,
        hasSubject:false,
        canSubmit:true,
        showCxdfInput:false,//是否重新打分
        task:{
          task:{
            name:'',
            totalScore:'',
            // weight:''
          },
          questionList:[],

        },
        SUBJECT_TYPE:SUBJECT_TYPE
      }
    },
    created(){
      this.isStudent = this.$store.state.user.roles.in_array('student');

      //获取任务详情
      if(!this.taskId || this.taskId === ''){
        return;
      }
      //学生查看任务详情
      getStuTaskDetail({taskId: this.taskId, studentNo: this.uId})
        .then(resp => {
          if (resp.status === 0) {
            this.$message.warning('获取学生任务信息失败');
            return;
          }
          //根据结果设置subject值
          this.task = resp.data;
          for (let item of this.task.questionList) {
            if (item.taskQuestions.questionType === SUBJECT_TYPE.SUBJECTIVE) {
              this.hasSubject = true;
              break;
            }
          }
        });
    },
    mounted() {

    },
    methods:{
      /**
       * cxdf 重新评阅
       *
       * */
      cxdf(){

        this.canSubmit = false;
        this.showCxdfInput = true;
      },
      /**
       * cxdfSubmit 重新评阅提交
       *
       * */
      cxdfSubmit(){
        // let totalScore = 0;
        // for(let item of this.task.questionList){
        //   totalScore += item.taskQuestions.teacherScore !== '' ? +item.taskQuestions.teacherScore : item.taskQuestions.score;
        // }
        // this.task.studentTotalScore = totalScore;

        updateSubjectScore(this.setStudentPaperByTask(this.task))
          .then(resp=>{
            if(resp.status === 0){
              this.$message.warn('评分失败')
              return
            }
            this.$message.success('评分成功')
            this.$emit('remarkSuccess');
          });
      },
      /**
       * handleBlur 打分 分值 校验
       * @params e
       * @params maxScore
       * */
      handleBlur(e, maxScore){
        if(+e.currentTarget.value > maxScore){
          this.canSubmit = true;
          this.$message.warning('当前设置分值超过最大分值，请重新设置！');
        }
        else {
          this.canSubmit = false;
        }
      },

      setStudentPaperByTask(task){
        debugger
        let taskTmp = {};
        taskTmp.courseId = this.variables.courseId;
        taskTmp.studentNo = this.uId;
        taskTmp.teacherNo = this.$store.state.user.token;
        taskTmp.taskId = task.task.id;
        taskTmp.subjectList = [];
        for(let q of task.questionList){
          taskTmp.subjectList.push({
            taskId:task.task.id,
            studentNo : this.uId,
            questionId: q.taskQuestions.id,
            questionType: q.taskQuestions.questionType,
            standardAnswers: q.taskQuestions.standardAnswer,//标准答案
            answers:q.taskQuestions.answers,
            score:q.taskQuestions.score,
            teacherScore: q.taskQuestions.teacherScore
          });
        }
        return taskTmp;
      },
    },
  }
</script>

<style scoped>
  .main-frame{
    width: 1200px;
    margin: 0 auto;
    border: 1px solid #EBEBEB;
  }

  .subjectStatic{
    height: 45px;
    width: 100%;
    line-height: 45px;
    background-color: #F6F6F6;
    padding: 0 20px;
  }

  .task-name-label{
    height: 45px;
    line-height: 45px;
    font-size: 16px;
    padding: 0 20px;
  }

  .subject-container{
    padding: 20px;
  }

  span .is-checked{
    color: #009687;
  }

  .save-btn{
    margin-left: calc(50% - 40px);
    margin-top: 10px;
    margin-bottom: 10px;
  }
  .task-name-label > span{
    font-family: cursive;
    color: #e6a23c;
  }

  .score-label{
    height: 35px;
    line-height: 35px;
    padding-left: 10px;
    margin-top: 10px;
    background-color: #FEF4EA;
    border: 1px solid #FE9226;
  }

  .score-label > span{
    display: inline-block;
    margin-right: 20px;
  }
</style>

