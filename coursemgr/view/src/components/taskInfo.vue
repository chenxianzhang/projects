<template>
  <div class="main-container">
    <div class="task-name">
      任务名称：
      <el-input :disabled="operate!==TASK_OPERATOR_TYPE.TEACHER_STATEMENT"
                class="task-name-input"
                placeholder="请填写任务名称"
                v-model="subject.taskName">
      </el-input>
    </div>
    <div class="subjectStatic">选择题 {{subject.subjectForChoose.length}} 道，判断题 {{subject.subjectForJudge.length}} 道，
      主观题 {{subject.subjectForSubjective.length}} 道，
      总分值 <input v-model="totalScore" disabled/> 分，
      权重：<input :disabled="operate!==TASK_OPERATOR_TYPE.TEACHER_STATEMENT" v-model="subject.weight" /> %。</div>

    <div class="subject-for-choose">
      <div class="subject-for-title">单选题</div>
      <div class="subject-score-setting">
        设置分值：每题
        <input :disabled="operate!==TASK_OPERATOR_TYPE.TEACHER_STATEMENT"
               v-model="subject.subjectForChooseScore"
               style="width: 30px; height: 30px;" />
        分
      </div>
      <div v-if="operate===TASK_OPERATOR_TYPE.TEACHER_STATEMENT"
           class="el-icon-plus"
           @click="addSubject('choose')"
           style=" margin-left: 30px; line-height: 40px; cursor: pointer;">
      </div>
      <div class="subject-item" :class="(sub.stem === '' || sub.chooseItem.length === 0) ? 'invalid' : ''" v-for="(sub,index) in subject.subjectForChoose">
        <el-input :disabled="operate!==TASK_OPERATOR_TYPE.TEACHER_STATEMENT"
                  style="width: calc(100% - 130px)"
                  placeholder="请输入题目,并设置选项"
                  v-model="sub.stem"></el-input>
        <div v-if="operate===TASK_OPERATOR_TYPE.TEACHER_STATEMENT"
             class="el-icon-remove"
             @click="removeSubjectItem(index, 'choose')"
             style="margin-left: 10px; cursor: pointer;">
        </div>
        <div v-if="operate===TASK_OPERATOR_TYPE.TEACHER_STATEMENT"
             class="el-icon-setting"
             @click="setChooseItems(index)"
             style="float: right; line-height: 40px; margin-right: 10px; cursor: pointer;">
          设置选项
        </div>
        <div style="margin: 20px 80px;">
          <el-radio-group :disabled="operate!==TASK_OPERATOR_TYPE.TEACHER_UPLOAD_ANSWER && operate !== TASK_OPERATOR_TYPE.STUDENT_ANSWER"
                          v-model="sub.answer"
                          style=" display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap">
            <el-radio v-for="(cItem,idx) in sub.chooseItem" :label="cItem" :key="idx" style="margin: 5px;">{{cItem}}</el-radio>
          </el-radio-group>
        </div>
      </div>
    </div>

    <div class="subject-for-judge" v-if="operate===TASK_OPERATOR_TYPE.TEACHER_STATEMENT || subject.subjectForJudgeScore.length>0">
      <div class="subject-for-title">判断题</div>
      <div style="float: left; line-height: 40px;">
        设置分值：每题
        <input :disabled="operate!==TASK_OPERATOR_TYPE.TEACHER_STATEMENT"
               v-model="subject.subjectForJudgeScore"
               style="width: 30px; height: 30px;" />分
      </div>
      <div v-if="operate===TASK_OPERATOR_TYPE.TEACHER_STATEMENT"
           class="el-icon-plus"
           @click="addSubject('judge')"
           style=" margin-left: 30px; line-height: 40px; cursor: pointer;">
      </div>
      <div class="subject-item" :class="sub.stem === '' ? 'invalid' : ''" v-for="(sub,index) in subject.subjectForJudge">
        <el-input :disabled="operate!==TASK_OPERATOR_TYPE.TEACHER_STATEMENT"
          style="width: calc(100% - 130px)"
          placeholder="请输入题目"
          v-model="sub.stem">
        </el-input>
        <div v-if="operate===TASK_OPERATOR_TYPE.TEACHER_STATEMENT"
             class="el-icon-remove"
             @click="removeSubjectItem(index, 'judge')"
             style="margin-left: 10px; cursor: pointer;"></div>
        <div style="margin: 20px;">
          <el-radio-group :disabled="operate!==TASK_OPERATOR_TYPE.TEACHER_UPLOAD_ANSWER && operate !== TASK_OPERATOR_TYPE.STUDENT_ANSWER"
                          v-model="sub.answer"
                          style=" display: flex; align-items: center; justify-content: space-around; flex-wrap: wrap">
            <el-radio v-for="(cItem,idx) in sub.chooseItem" :label="cItem" :key="idx" style="margin: 5px;">{{cItem}}</el-radio>
          </el-radio-group>
        </div>
      </div>
    </div>

    <div class="subject-for-subjective" v-if="operate===TASK_OPERATOR_TYPE.TEACHER_STATEMENT || subject.subjectForSubjective.length>0">
      <div class="subject-for-title">主观题</div>
      <div style="margin: 7px 20px; line-height: 40px; float: left">
        <el-radio-group v-model="subject.subjectForSubjectiveMarkType"
                        :disabled="operate!==TASK_OPERATOR_TYPE.TEACHER_STATEMENT"
                        style=" display: flex; align-items: center; justify-content: space-around; flex-wrap: wrap">
          <el-radio label="SELF_EVA" style="margin: 5px;">自评</el-radio>
          <el-radio label="GROUP_INNER_EVA" style="margin: 5px;">组内互评</el-radio>
          <el-radio label="GROUP_INTERBLOCK_EVA" style="margin: 5px;">组间互评</el-radio>
        </el-radio-group>
      </div>
      <div v-if="operate===TASK_OPERATOR_TYPE.TEACHER_STATEMENT" class="el-icon-plus" @click="addSubject('subjective')"
           style=" margin-left: 30px; line-height: 40px; cursor: pointer;"></div>


      <div class="subject-item-subjective" :class="sub.stem === '' ? 'invalid' : ''" v-for="(sub,index) in subject.subjectForSubjective">
        <el-input :disabled="operate!==TASK_OPERATOR_TYPE.TEACHER_STATEMENT"
                  style="width: calc(100% - 250px); float: left;"
                  placeholder="请输入题目"
                  v-model="sub.stem">
        </el-input>
        <div style="float: left; line-height: 40px;">
          设置分值：<input :disabled="operate!==TASK_OPERATOR_TYPE.TEACHER_STATEMENT" style="width: 30px; height: 30px;" v-model="sub.score" />分
        </div>

        <div v-if="operate===TASK_OPERATOR_TYPE.TEACHER_STATEMENT" class="el-icon-remove subjective-item-remove"
             @click="removeSubjectItem(index, 'subjective')">
        </div>
      </div>
    </div>

    <div class="inspire-date">
      <span>截止日期</span>
      <el-date-picker :disabled="operate!==TASK_OPERATOR_TYPE.TEACHER_STATEMENT" v-model="subject.inspireDate" type="date" placeholder="选择日期">
      </el-date-picker>
    </div>

    <div class="submit-btn-container">
      <el-button v-if="operate===TASK_OPERATOR_TYPE.TEACHER_STATEMENT" type="primary" @click="handleNextStep">下一步</el-button>
      <el-button v-if="operate===TASK_OPERATOR_TYPE.TEACHER_UPLOAD_ANSWER" type="primary" @click="handleUploadAnswer">上传答案</el-button>
      <el-button v-if="operate===TASK_OPERATOR_TYPE.STUDENT_ANSWER" type="primary" @click="handleSubmitAnswer">提交答案</el-button>
    </div>
    <select-setting :dialogFormVisible="dialogFormVisible" @hide="hideDialog" @selectionsHandle="selectionsHandle">
    </select-setting>
  </div>
</template>

<script>
  import SubjectSelectItemSetting from './subjectSelectItemSetting'
  import {saveTask, getTaskDetailByTaskId} from '@/api/task'
  import {TASK_OPERATOR_TYPE} from "../utils/statusUtil";

  export default {
    name: "newTask",
    //operate --教师->拟题(T_statement)、传答案(T_uploadAnswer)、查看详情(T_viewDetail); 学生->查看详情(S_viewDetail)、答题(S_answer)
    props:['taskId', 'operate'],
    components:{'select-setting': SubjectSelectItemSetting},
    data(){
      return {
        TASK_OPERATOR_TYPE:TASK_OPERATOR_TYPE,
        dialogFormVisible:false,
        curSubjectIndex:0,//当前单选题的索引
        subject:{
          taskName:'',
          subjectForChoose:[],
          subjectForChooseScore:'',
          subjectForJudge:[],
          subjectForJudgeScore:'',
          subjectForSubjective:[],
          subjectForSubjectiveMarkType:'',
          totalScore:'',
          weight:'',
          inspireDate:'',
        }
      }
    },
    computed:{
      totalScore: function(){
        let subjectiveScore = 0;
        for(let subjectiveItem of this.subject.subjectForSubjective){
          subjectiveScore += +subjectiveItem.score;
        }
        this.subject.totalScore = subjectiveScore
          + this.subject.subjectForChoose.length * this.subject.subjectForChooseScore
          + this.subject.subjectForJudge.length * this.subject.subjectForJudgeScore;
        return this.subject.totalScore;
      }
    },
    created(){
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
          this.setSubjectByTaskDetailInfo(resp.data);
        });
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
        this.subject.taskName = taskDetailInfo.task.name;
        this.subject.weight = taskDetailInfo.task.weight;
        this.subject.totalScore = taskDetailInfo.task.totalScore;
        this.subject.inspireDate = taskDetailInfo.task.deadline;
        this.subject.subjectForSubjectiveMarkType = taskDetailInfo.task.markType;
        for(let item of taskDetailInfo.questionList){
          switch (item.questionType) {
            case "单选题":
              this.subject.subjectForChoose.push({id:item.id, stem:item.stems, chooseItem:item.options.split(','), answer:item.answers});
              this.subject.subjectForChooseScore = item.score;
              break;
            case "判断题":
              this.subject.subjectForJudge.push({id:item.id, stem:item.stems, chooseItem:["是","否"], answer:item.answers});
              this.subject.subjectForJudgeScore = item.score;
              break;
            case "主观题":
              this.subject.subjectForSubjective.push({id:item.id, stem:item.stems, chooseItem:[], answer:item.answers, score: item.score});
              break;
          }
        }
      },
      /**
       * addSubject 添加题目
       * params type ['choose','judge','subjective']
       * return null
       **/
      addSubject(type){
        switch (type) {
          case "choose":
            this.subject.subjectForChoose.push({stem:'', chooseItem:[], answer:''});
            break;
          case "judge":
            this.subject.subjectForJudge.push({stem:'', chooseItem:["是","否"], answer:''});
            break;
          case "subjective":
            this.subject.subjectForSubjective.push({stem:'', chooseItem:[], answer:'', score:''});
            break;
          default:
            break;
        }
      },
      /**
       * removeSubjectItem 删除题目
       * params index 题目索引，type:['choose','judge','subjective']
       * return null
       **/
      removeSubjectItem(index, type){
        switch (type) {
          case "choose":
            this.subject.subjectForChoose.splice(index, 1);
            break;
          case "judge":
            this.subject.subjectForJudge.splice(index, 1);
            break;
          case "subjective":
            this.subject.subjectForSubjective.splice(index, 1);
            break;
        }
      },
      /**
       * setChooseItems 设置题目选项
       * params index 题目索引
       * return null
       **/
      setChooseItems(index){
        //设置当前单选题的选项 index当前题目索引
        this.dialogFormVisible = true;
        this.curSubjectIndex = index;
      },
      /**
       * hideDialog 隐藏选项设置弹出框
       * params val 是否隐藏
       * return null
       **/
      hideDialog(val){
        this.dialogFormVisible = val;
      },
      /**
       * selectionsHandle 获取子组件设置的选项
       * params selections 选项
       * return null
       **/
      selectionsHandle(selections){
        this.subject.subjectForChoose[this.curSubjectIndex].chooseItem = selections;
      },
      /**
       * handleNextStep 下一步按钮响应时间
       * params selections 选项
       * return null
       **/
      handleNextStep(){
        //题目校验--选项是否存在、题干是否存在、分值是否存在、总分值是否存在
        if(this.subject.taskName === ''){
          this.$message.warning('请填写任务名称！');
          return;
        }
        if(this.subject.subjectForChoose.length + this.subject.subjectForJudge.length + this.subject.subjectForSubjective.length === 0){
          this.$message.warning('请添加相关题目！');
          return;
        }
        if(document.getElementsByClassName('invalid').length !== 0){
          this.$message.warning('请填写相关内容！');
          return;
        }
        if(this.subject.inspireDate === ''){
          this.$message.warning('请设置截止日期！');
          return;
        }
        if(this.subject.subjectForChoose.length !== 0 && this.subject.subjectForChooseScore === ''){
          this.$message.warning('请设置单选题分数！');
          return;
        }
        if(this.subject.subjectForJudge.length !== 0 && this.subject.subjectForJudgeScore === ''){
          this.$message.warning('请设置判断题分数！');
          return;
        }
        if(this.subject.subjectForSubjective.length !== 0){
          for(let item of this.subject.subjectForSubjective){
            if(item.score === ''){
              this.$message.warning('请设置主观题分数！');
              return;
            }
          }
        }
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
          //response.data---taskId
          setTimeout(()=>{
            this.$router.push({name:'uploadAnswer', params:response.data});
          },1000);
        });


      },
      /**
       * handleUploadAnswer  上传答案
       * params type 操作类型（保存/更新）
       * return {task:task, questionList:questions} 保存的数据结构
       * */
      handleUploadAnswer(){
        //todo 参数校验
        let updateData = this.getSaveData();
        saveTask(updateData).then(response=>{
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
            message: "答案上传成功！"
          });
        });
      },
      /**
       * handleSubmitAnswer  提交答案--学生
       * params
       * return
       * */
      handleSubmitAnswer(){

      },
      /**
       * getSaveData  构造需要保存或者更新的task数据
       * params null
       * return {task:task, questionList:questions} 保存的数据结构
       * */
      getSaveData(){
        let task = {
          id:this.taskId,
          name:this.subject.taskName,
          courseId:this.$store.getters.courseId,
          weight: this.subject.weight,
          deadline:this.subject.inspireDate,
          totalScore: this.subject.totalScore,
          markType:this.subject.subjectForSubjectiveMarkType
        };
        let questions = [];
        let questionNo = 1;
        for(let item of this.subject.subjectForChoose){
          questions.push({
            stems:item.stem,
            questionType:'单选题',
            score:this.subject.subjectForChooseScore,
            options:item.chooseItem.join(','),
            questionNo: questionNo
          });
          ++questionNo;
        }
        for(let item of this.subject.subjectForJudge){
          ++questionNo;
          questions.push({
            stems:item.stem,
            questionType:'判断题',
            score:this.subject.subjectForJudgeScore,
            questionNo:questionNo
          });
        }
        for(let item of this.subject.subjectForSubjective){
          ++questionNo;
          questions.push({
            stems:item.stem,
            questionType:'主观题',
            score:item.score,
            questionNo:questionNo
          });
        }
        return {task:task, questionList:questions};
      },
    },
  }
</script>

<style scoped>
  .task-name{
    margin-bottom: 20px;
  }
  .task-name-input{
    width: 300px;
  }
  .subjectStatic > input{
    width: 40px;
    height: 30px;
  }
  .subject-item{
    margin: 10px 0;
    padding: 5px;
  }
  .subject-item-subjective{
    margin: 10px 0;
    padding: 5px;
    height: 55px;
  }
  .subject-for-title{
    float: left;
    margin-right: 20px;
    line-height: 40px;
    background-color: gray;
    padding: 0 10px;
  }
  .subject-score-setting{
    float: left;
    line-height: 40px;
  }
  .subject-score-setting > input{
    width: 40px;
    height: 30px;
  }
  .subject-for-choose{
    margin: 10px 0;
  }
  .subject-for-judge{
    margin: 10px 0;
  }
  .subject-for-subjective{
    margin: 10px 0;
  }
  .inspire-date{
    display: inline-block;
    margin: 10px 0;
  }
  .invalid{
    border: 1px dashed red;
  }
  .main-container .step{
    height: 40px;
    line-height: 40px;
    width: 150px;
    background: #456;
    color: #fff;
    text-align: center;
    box-shadow: 0 0 5px 4px #35506b;
    margin-bottom: 10px;
  }
  .submit-btn-container{
    display: flex;
    align-items: center;
    justify-content: center;
    border-top: 1px solid gray;
    padding-top: 10px;
  }
  .subjective-item-remove{
    margin-left: 10px;
    cursor: pointer;
    line-height: 40px;
    float: left;
  }
</style>
