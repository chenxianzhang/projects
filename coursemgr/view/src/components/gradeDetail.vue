<template>
  <div class="main-frame">
    <div class="task-name-label">
      任务名称：<span>{{task.task.name}}</span>
    </div>
    <div class="subjectStatic">总题数 {{task.questionList.length}} 道，总分值 {{task.task.totalScore}} 分，权重：{{task.task.weight}} %。</div>
    <div class="subject-container">
      <div class="subject-item" v-for="(item, index) in task.questionList"
           style="padding-bottom: 10px; border-bottom: 1px solid #dff0d8">
        <!--题干设置区域-->
        <div style="margin-bottom:10px; line-height: 32px">
          <span>{{index + 1}}.</span>
          <el-input v-html="item.taskQuestions.stems" style="width: calc(100% - 240px)"></el-input>
        </div>
        <!--单选题 选项设置区域-->
        <div v-if="item.taskQuestions.questionType === SUBJECT_TYPE.CHOOSE">
          <el-radio-group v-model="item.taskQuestions.answers"
                          style="display: grid"
                          :disabled="true">
            <el-radio v-for="(cItem, cIndex) in item.optionList"
                      :label="cItem.optionDes"
                      :key="cIndex"
                      style="margin: 5px;">
              <span v-html="cItem.optionDes"></span>
            </el-radio>
          </el-radio-group>
          <span>得分：<el-input v-html="item.taskQuestions.score" style="width: 40px; height: 30px;"/>分</span>
        </div>
        <!--判断题 选项设置区域-->
        <div v-if="item.taskQuestions.questionType === SUBJECT_TYPE.JUDGE">
          <el-radio-group v-model="item.taskQuestions.answers" style="display: grid" disabled>
            <el-radio label="是" style="margin: 5px;">
            </el-radio>
            <el-radio label="否" style="margin: 5px;">
            </el-radio>
          </el-radio-group>
          <span>得分：<el-input v-html="item.taskQuestions.score" style="width: 40px; height: 30px;"/>分</span>
        </div>
        <!--主观题 答题 设置区域-->
        <div v-if="item.taskQuestions.questionType === SUBJECT_TYPE.SUBJECTIVE">
          <el-row :gutter="10">
            <el-col :span="22">
              <!--主观题 答题-->
              <el-input type="textarea"
                        v-model="item.taskQuestions.answers"
                        placeholder="请填写答案"
                        disabled>
              </el-input>
            </el-col>
            <el-col :span="2">
              <div>得分：{{item.taskQuestions.score}}</div>
              <div>评阅人：张三</div>
              <div style="margin-top: 10px" v-if="showCxdfInput">
                得分：<input v-model="item.teacherScore" type="number" min="0" max="100" style="height: 30px; width: 30px"/>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    <el-row :gutter="20">
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
        canSubmit:true,
        showCxdfInput:false,//是否重新打分
        task:{
          task:{
            name:'',
            totalScore:'',
            weight:''
          },
          questionList:[],

        },
        SUBJECT_TYPE:SUBJECT_TYPE
      }
    },
    created(){
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
        });
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
        updateSubjectScore(this.task)
          .then(resp=>{
            if(resp.status === 0){
              this.$message.warn('评分失败')
              return
            }
            this.$message.success('评分成功')
          });
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
    font-size: 16px;
  }

  .save-btn{
    margin-left: calc(50% - 40px);
    margin-top: 10px;
    margin-bottom: 10px;
    width: 80px;
  }
  .task-name-label > span{
    font-family: cursive;
    color: #e6a23c;
  }
</style>

