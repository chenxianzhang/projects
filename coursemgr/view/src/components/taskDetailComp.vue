<template>
  <div class="main-frame-task-detail">
      <div class="task-name-label">
        任务名称：<span>{{task.name}}</span>
      </div>
      <div class="subjectStatic">总题数 {{task.subjects.length}} 道，总分值 {{task.totalScore}} 分。
        <span v-if="TASK_OPERATOR_TYPE.PRE_VIEW !== operateType" style="margin-left: 30px; display: inline-block;">评分方式：
          <span style="color: #ee5134;">{{task.markType}}</span>
        </span>
        <span v-if="TASK_OPERATOR_TYPE.PRE_VIEW === operateType" style="margin-left: 30px; display: inline-block;">评分方式：
          <span style="color: #ee5134;">{{task.markType === '' ? '系统智评' : (task.markType === 'GROUP_INNER_EVA' ? '组内互评' : (task.markType === 'SELF_EVA' ? '自主评分' : '组间互评'))}}</span>
        </span>
      </div>
      <div class="subject-container">
        <div class="subject-item" v-for="(item, index) in task.subjects"
             style="padding-bottom: 10px; border-bottom: 1px solid #dff0d8">
          <!--题干设置区域-->
          <div style="margin-bottom:10px; line-height: 32px; display: flex">
            <span style="display: inline-block; width: 20px">{{index + 1}}.</span>
            <el-input v-html="item.stem" style="width: calc(100% - 20px); max-height: 300px; overflow: auto;"></el-input>
          </div>
          <!--单选题 选项设置区域-->
          <div v-if="item.questionType === SUBJECT_TYPE.CHOOSE">
            <el-radio-group  v-model="item.answer"
                             style="display: grid; overflow: hidden"
                             :disabled="operateType!==TASK_OPERATOR_TYPE.STUDENT_ANSWER">
              <el-radio v-for="(cItem, cIndex) in item.selections" :label="cItem.optionTag" :key="cIndex"
                        style="margin: 5px; width: calc(100% - 430px)">
                <span>{{cItem.optionTag}}. </span>
                <el-input v-html="cItem.optionDes" style="width: calc(100% - 470px);max-height: 300px; overflow: auto;"></el-input>
              </el-radio>
            </el-radio-group>
          </div>
          <!--判断题 选项设置区域-->
          <div v-if="item.questionType === SUBJECT_TYPE.JUDGE">
            <el-radio-group  v-model="item.answer" style="display: grid"
                             :disabled="operateType!==TASK_OPERATOR_TYPE.STUDENT_ANSWER">
              <el-radio label="是" style="margin: 5px;">
              </el-radio>
              <el-radio label="否" style="margin: 5px;">
              </el-radio>
            </el-radio-group>
          </div>
          <!--主观题 答题 设置区域-->
          <div v-if="item.questionType === SUBJECT_TYPE.SUBJECTIVE">
            <!--主观题 答题-->
            <div v-if="operateType===TASK_OPERATOR_TYPE.MARK_POINT" class="answer-title">参考答案</div>
            <el-input v-if="operateType===TASK_OPERATOR_TYPE.MARK_POINT" v-html="item.sAnswer"
                      style="min-height: 78px; max-height: 300px; overflow: auto; border: 1px solid #DFDFDF; background-color: #F6F6F6;"></el-input>
            <div class="answer-title">
              {{operateType===TASK_OPERATOR_TYPE.STUDENT_ANSWER || operateType===TASK_OPERATOR_TYPE.STUDENT_VIEW_DETAIL || operateType===TASK_OPERATOR_TYPE.MARK_POINT
              ? '学生答案'
              : '参考答案'}}
            </div>
            <el-input v-if="operateType!==TASK_OPERATOR_TYPE.STUDENT_ANSWER"
                      v-html="item.answer"
                      style="min-height: 78px; max-height: 300px; overflow: auto; border: 1px solid #DFDFDF; background-color: #F6F6F6;"></el-input>
            <Tinymce v-if="operateType===TASK_OPERATOR_TYPE.STUDENT_ANSWER"
                     :height=100 v-model="item.answer" placeholder="请填写主观题答案" style="margin: 5px" />

            <div  class="score-label"
                  v-if="operateType===TASK_OPERATOR_TYPE.STUDENT_VIEW_DETAIL">
              得分：<span v-html="item.score"></span>分;
              总分：<span v-html="item.qScore"></span>分
            </div>
            <div  class="score-label"
                  v-if="operateType===TASK_OPERATOR_TYPE.MARK_POINT">
              得分：<input type="number" v-model="item.score"
                        min="0" :max="item.qScore"
                        @blur="handleBlur($event, item.qScore)"
                        style="width: 40px; height: 30px;" />分;
              总分：<span v-html="item.qScore"  />分
            </div>
          </div>
        </div>
        <!--编辑和完成编辑按钮-->
        <el-button class="save-btn"
                   type="primary"
                   v-if="operateType === TASK_OPERATOR_TYPE.STUDENT_ANSWER || operateType === TASK_OPERATOR_TYPE.MARK_POINT"
                   @click="handleSubjectSubmit" :disabled="!canSubmit">提  交</el-button>
      </div>
  </div>
</template>

<script>
  import {SUBJECT_TYPE, TASK_OPERATOR_TYPE} from '../utils/statusUtil'
  import {Subject, Task, Selection} from "../models/task-model";
  import {getTaskDetailByTaskId, submitTaskPaper, getStuTaskDetail} from '@/api/task'
  import { updateSubjectScore } from '@/api/grade'
  import Tinymce from '../components/Tinymce/index'

    export default {
      name: "taskDetailComp",
      props:['operateType', 'taskId', 'markUid', 'preViewTask'],
      components: {
        Tinymce
      },
      data(){
        return{
          canSubmit:true,
          SUBJECT_TYPE:SUBJECT_TYPE,
          TASK_OPERATOR_TYPE:TASK_OPERATOR_TYPE,
          task: new Task(),
          taskStudent: {
            studentNo:'',
            courseId:'',
            taskId:'',
            questionList:[]
          },
          stuSubmit: false
        }
      },
      created(){
        //教师预览
        if(this.TASK_OPERATOR_TYPE.PRE_VIEW === this.operateType){
          this.task = this.preViewTask;
          return;
        }
        //获取任务详情
        if(!this.taskId || this.taskId === ''){
          return;
        }
        //获取任务信息
        if(this.operateType !== this.TASK_OPERATOR_TYPE.STUDENT_VIEW_DETAIL
          && this.operateType !== this.TASK_OPERATOR_TYPE.STUDENT_ANSWER
          && this.operateType !== this.TASK_OPERATOR_TYPE.MARK_POINT){
          getTaskDetailByTaskId({taskId: this.taskId})
            .then(resp=>{
              if(resp.status === 0){
                this.$message.warning('获取任务信息失败');
                return;
              }
              //根据结果设置subject值
              this.setSubjectByTaskDetailInfo(resp.data);
          });
        }
        //学生查看任务详情
        else{
          let uid = this.$store.state.user.token;
          if(this.operateType === this.TASK_OPERATOR_TYPE.MARK_POINT){
            uid = this.markUid;
          }
          getStuTaskDetail({taskId:this.taskId, studentNo: uid})
            .then(resp=>{
              if(resp.status === 0){
                this.$message.warning('获取学生任务信息失败');
                return;
              }
              //根据结果设置subject值
              this.setSubjectByTaskDetailInfo(resp.data);
            });
        }
      },
      methods:{
        /**
         * 根据task的信息  设置学生答卷信息
         * params taskInfo
         * return null
         **/
        setStudentPaperByTask(task){
          this.taskStudent.courseId = this.$route.params.courseId;
          this.taskStudent.studentNo = this.$store.state.user.token;
          this.taskStudent.taskId = this.taskId;
          for(let q of task.subjects){
            this.taskStudent.questionList.push({
              questionId: q.id,
              questionType: q.questionType,
              standardAnswers: '',//标准答案
              answers:q.answer,
              score:q.score
            });
          }
        },
        /**
         * 根据接口返回的任务信息结果，构造subject对象的值
         * params taskDetailInfo
         * return null
         **/
        setSubjectByTaskDetailInfo(taskDetailInfo){
          if(!taskDetailInfo){
            return;
          }
          this.task.id = this.taskId;
          this.task.name = taskDetailInfo.task.name;
          // this.task.weight = taskDetailInfo.task.weight;
          this.task.totalScore = taskDetailInfo.task.totalScore;
          this.task.inspireDate = taskDetailInfo.task.deadline;
          switch (taskDetailInfo.task.markType) {
            case "SELF_EVA":
              this.task.markType = '自主评分';
              break;
            case "GROUP_INNER_EVA":
              this.task.markType = '组内互评';
              break;
            case "GROUP_INTERBLOCK_EVA":
              this.task.markType = '组间互评';
              break;
            default:
              this.task.markType = '系统智评';
          }
          for(let item of taskDetailInfo.questionList){
            let subject_c = new Subject();
            subject_c.id = item.taskQuestions.id;
            subject_c.edit = false;
            subject_c.selections = item.optionList;
            subject_c.answer = item.taskQuestions.answers;
            subject_c.sAnswer = item.taskQuestions.standardAnswer;
            subject_c.score = item.taskQuestions.score;
            subject_c.qScore = item.taskQuestions.questionScore;
            subject_c.stem = item.taskQuestions.stems;
            subject_c.questionType = item.taskQuestions.questionType;
            subject_c.teacherScore = item.taskQuestions.teacherScore;
            this.task.subjects.push(subject_c);
          }
        },
        /**
         * 学生答题  提交答案
         * */
        handleSubjectSubmit(){
          this.setStudentPaperByTask(this.task);
          //学生答题
          if(this.operateType === this.TASK_OPERATOR_TYPE.STUDENT_ANSWER){
            if (this.stuSubmit) {
              return
            }
            this.stuSubmit = true
            submitTaskPaper(this.taskStudent)
              .then(resp=>{
                this.stuSubmit = false
              if(resp.status === 0){
                this.$message.warning('上传答案失败');
                return;
              }
                this.$message.success('上传答案成功');
                this.$emit('answerEmit');
            }).catch(err => {
              this.stuSubmit = false
            });
          }
          else {
            let updateScoreObj = {
              taskId:this.taskId,
              studentNo:this.markUid,
              subjectList:[]
            };
            for(let item of this.task.subjects){
              if(item.questionType === SUBJECT_TYPE.SUBJECTIVE){
                if (item.score < 0) {
                  this.$message.warning('评分不能小于0');
                  return
                }
                var scoreStr = '' + item.score
                if (scoreStr.indexOf('.') > 0) {
                  var tmpArr = scoreStr.split('.');
                  if (tmpArr.length > 2) {
                    this.$message.warning('主观题评分异常，请检查输入内容');
                    return
                  }
                  if (tmpArr[1].length > 1) {
                    this.$message.warning('评分只能保留一位小数');
                    return
                  }
                }
                updateScoreObj.subjectList.push(
                  {
                    taskId:this.taskId,
                    studentNo:this.markUid,
                    questionId:item.id,
                    questionType:item.questionType,
                    answers:item.answer,
                    score:item.score,
                    teacherScore:item.teacherScore
                  }
                );
              }
            }
            if(updateScoreObj.subjectList.length === 0){
              return
            }
            updateSubjectScore(updateScoreObj)
              .then(resp=>{
                if(resp.status === 0){
                  this.$message.warn('评分失败')
                  return
                }
                this.$message.success('评分成功')
                this.$emit('closeDlg');
              });
          }
        },
        /**
         * handleBlur 打分 分值 校验
         * @params e
         * @params maxScore
         * */
        handleBlur(e, maxScore){
          if(+e.currentTarget.value > maxScore){
            this.canSubmit = false;
            this.$message.warning('当前设置分值超过最大分值，请重新设置！');
          }
          else {
            this.canSubmit = true;
          }
        }
      },
    }
</script>

<style scoped>
  .main-frame-task-detail{
    width: 1200px;
    margin: 0 auto;
    border: 1px solid #EBEBEB;
  }

  .main-frame-task-detail .el-radio-group{
    line-height: unset;
  }

  .main-frame-task-detail .el-radio{
    line-height: unset;
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

  .save-btn{
    margin-left: calc(50% - 40px);
    margin-top: 10px;
    margin-bottom: 10px;
  }
  .task-name-label > span{
    font-family: cursive;
    color: #e6a23c;
  }

  .subjectStatic{
    height: 45px;
    width: 100%;
    line-height: 45px;
    background-color: #F6F6F6;
    padding: 0 20px;
  }

  .score-label{
    height: 35px;
    line-height: 35px;
    padding-left: 10px;
    margin-top: 10px;
    background-color: #FEF4EA;
    border: 1px solid #FE9226;
  }
  .answer-title{
    color: #009788;
    width: 80px;
    text-align: center;
    border-radius: 20px;
    margin-bottom: 5px;
    border: 1px solid #009788;
  }
</style>
