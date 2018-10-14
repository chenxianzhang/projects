<template>
  <div class="main-frame">
    <div>
      <div class="task-name-label">
        任务名称：<span>{{task.name}}</span>
      </div>
      <div class="subjectStatic">总题数 {{task.subjects.length}} 道，总分值 {{task.totalScore}} 分，权重：{{task.weight}} %。</div>
      <div class="subject-container">
        <div class="subject-item" v-for="(item, index) in task.subjects"
             style="padding-bottom: 10px; border-bottom: 1px solid #dff0d8">
          <!--题干设置区域-->
          <div style="margin-bottom:10px">
            <span>{{index + 1}}.</span>
            <el-input v-html="item.stem" style="width: calc(100% - 240px)"></el-input>
            <span v-if="operateType!==TASK_OPERATOR_TYPE.STUDENT_ANSWER && operateType!==TASK_OPERATOR_TYPE.MARK_POINT">分数：<el-input v-html="item.score" style="width: 40px; height: 30px;" />分</span>
            <span v-if="operateType===TASK_OPERATOR_TYPE.MARK_POINT">总分：<el-input v-html="item.qScore" style="width: 40px; height: 30px;" />分</span>
          </div>
          <!--单选题 选项设置区域-->
          <div v-if="item.questionType === SUBJECT_TYPE.CHOOSE">
            <el-radio-group  v-model="item.answer"
                             style="display: grid"
                             :disabled="operateType!==TASK_OPERATOR_TYPE.STUDENT_ANSWER">
              <el-radio v-for="(cItem, cIndex) in item.selections" :label="cItem.optionDes" :key="cIndex" style="margin: 5px;">
                <span v-html="cItem.optionDes"></span>
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
            <el-input type="textarea"
                      v-model="item.answer"
                      style="width: calc(100% - 100px)"
                      placeholder="请填写答案"
                      :disabled="operateType!==TASK_OPERATOR_TYPE.STUDENT_ANSWER">
            </el-input>

            <div v-if="operateType===TASK_OPERATOR_TYPE.STUDENT_VIEW_DETAIL"
                 style="width: 100px; float: right; line-height: 50px; text-align: right;">
              评分：<input style="width: 40px; height: 30px;" v-model="item.score" disabled />分
            </div>
            <div v-if="operateType===TASK_OPERATOR_TYPE.MARK_POINT"
                 style="width: 100px; float: right; line-height: 50px; text-align: right;">
              评分：<input style="width: 40px; height: 30px;" type="number" min="0" :max="item.qScore" v-model="item.score" />分
            </div>


            <el-row :gutter="10">
              <el-col :span="2" style="height: 100%;"><div style="height: 100%; display: flex;align-items: center;justify-content: center"><span>标准答案：</span></div></el-col>
              <el-col :span="22">
                <span v-if="operateType===TASK_OPERATOR_TYPE.MARK_POINT"
                          style="margin-top: 5px; background: #ee9900; color: white">{{item.sAnswer}}</span>
              </el-col>
            </el-row>
          </div>
        </div>
        <!--编辑和完成编辑按钮-->
        <el-button class="save-btn"
                   type="primary"
                   v-if="operateType === TASK_OPERATOR_TYPE.STUDENT_ANSWER || operateType === TASK_OPERATOR_TYPE.MARK_POINT"
                   @click="handleSubjectSubmit">提  交</el-button>
      </div>
    </div>
  </div>
</template>

<script>
  import {SUBJECT_TYPE, TASK_OPERATOR_TYPE} from '../utils/statusUtil'
  import {Subject, Task, Selection} from "../models/task-model";
  import {getTaskDetailByTaskId, submitTaskPaper, getStuTaskDetail} from '@/api/task'
  import { updateSubjectScore } from '@/api/grade'

    export default {
      name: "taskDetailComp",
      props:['operateType', 'taskId', 'markUid'],
      data(){
        return{
          SUBJECT_TYPE:SUBJECT_TYPE,
          TASK_OPERATOR_TYPE:TASK_OPERATOR_TYPE,
          task: new Task(),
          taskStudent: {
            studentNo:'',
            courseId:'',
            taskId:'',
            questionList:[]
          },
        }
      },
      created(){
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
          this.task.weight = taskDetailInfo.task.weight;
          this.task.totalScore = taskDetailInfo.task.totalScore;
          this.task.inspireDate = taskDetailInfo.task.deadline;
          this.task.markType = taskDetailInfo.task.markType;
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
            submitTaskPaper(this.taskStudent)
              .then(resp=>{
              if(resp.status === 0){
                this.$message.warning('上传答案失败');
                return;
              }
                this.$message.success('上传答案成功');
                this.$emit('answerEmit');
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
              });
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
