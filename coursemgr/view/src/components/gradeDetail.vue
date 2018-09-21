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
          <span>得分：<el-input v-html="item.taskQuestions.score" style="width: 40px; height: 30px;"/>分</span>
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
        </div>
        <!--判断题 选项设置区域-->
        <div v-if="item.taskQuestions.questionType === SUBJECT_TYPE.JUDGE">
          <el-radio-group v-model="item.taskQuestions.answers" style="display: grid" disabled>
            <el-radio label="是" style="margin: 5px;">
            </el-radio>
            <el-radio label="否" style="margin: 5px;">
            </el-radio>
          </el-radio-group>
        </div>
        <!--主观题 答题 设置区域-->
        <div v-if="item.taskQuestions.questionType === SUBJECT_TYPE.SUBJECTIVE">
          <!--主观题 答题-->
          <el-input type="textarea"
                    v-model="item.taskQuestions.answers"
                    style="width: calc(100% - 100px)"
                    placeholder="请填写答案"
                    disabled>
          </el-input>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {getStuTaskDetail} from '@/api/task'
  import {SUBJECT_TYPE} from '../utils/statusUtil'

  export default {
    name: "gradeDetail",
    props:['taskId', 'uId'],
    data(){
      return{
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
       * 根据task的信息  设置学生答卷信息
       * params taskInfo
       * return null
       **/
      // setStudentPaperByTask(task){
      //   this.taskStudent.courseId = this.$route.params.courseId;
      //   this.taskStudent.studentNo = this.$store.state.user.token;
      //   this.taskStudent.taskId = this.taskId;
      //   for(let q of task.subjects){
      //     this.taskStudent.questionList.push({
      //       questionId: q.id,
      //       questionType: q.questionType,
      //       standardAnswers: '',//标准答案
      //       answers:q.answer,
      //       score:q.score
      //     });
      //   }
      // },
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

