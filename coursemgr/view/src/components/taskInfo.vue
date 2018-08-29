<template>
  <div class="main-container">
    <div class="task-name">
      任务名称：
      <el-input class="task-name-input" placeholder="请填写任务名称" v-model="subject.taskName"></el-input>
    </div>
    <div class="subjectStatic">选择题 {{subject.subjectForChoose.length}} 道，判断题 {{subject.subjectForJudge.length}} 道，
      主观题 {{subject.subjectForSubjective.length}} 道，
      总分值 <input v-model="totalScore" disabled/> 分，
      权重：<input v-model="subject.weight" /> %。</div>

    <div class="subject-for-choose">
      <div class="subject-for-title">单选题</div>
      <div class="subject-score-setting">设置分值：每题<input v-model="subject.subjectForChooseScore" style="width: 30px; height: 30px;" />分</div>
      <div class="el-icon-plus" @click="addSubject('choose')" style=" margin-left: 30px; line-height: 40px; cursor: pointer;"></div>
      <div class="subject-item" :class="(sub.stem === '' || sub.chooseItem.length === 0) ? 'invalid' : ''" v-for="(sub,index) in subject.subjectForChoose">
        <el-input style="width: calc(100% - 130px)" placeholder="请输入题目,并设置选项"  v-model="sub.stem"/>
        <div class="el-icon-remove" @click="removeSubjectItem(index, 'choose')" style="margin-left: 10px; cursor: pointer;"></div>
        <div class="el-icon-setting" @click="setChooseItems(index)" style="float: right; line-height: 40px; margin-right: 10px; cursor: pointer;">设置选项</div>
        <div style="margin: 20px;">
          <el-radio-group v-model="sub.answer" style=" display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap">
            <el-radio v-for="(cItem,idx) in sub.chooseItem" :label="cItem" :key="idx" style="margin: 5px;">{{cItem}}</el-radio>
          </el-radio-group>
        </div>
      </div>
    </div>

    <div class="subject-for-judge">
      <div class="subject-for-title">判断题</div>
      <div style="float: left; line-height: 40px;">设置分值：每题<input v-model="subject.subjectForJudgeScore" style="width: 30px; height: 30px;" />分</div>
      <div class="el-icon-plus" @click="addSubject('judge')" style=" margin-left: 30px; line-height: 40px; cursor: pointer;"></div>
      <div class="subject-item" :class="sub.stem === '' ? 'invalid' : ''" v-for="(sub,index) in subject.subjectForJudge">
        <el-input style="width: calc(100% - 130px)" placeholder="请输入题目" v-model="sub.stem"/>
        <div class="el-icon-remove" @click="removeSubjectItem(index, 'judge')" style="margin-left: 10px; cursor: pointer;"></div>
        <div style="margin: 20px;">
          <el-radio-group v-model="sub.answer" style=" display: flex; align-items: center; justify-content: space-around; flex-wrap: wrap">
            <el-radio v-for="(cItem,idx) in sub.chooseItem" :label="cItem" :key="idx" style="margin: 5px;">{{cItem}}</el-radio>
          </el-radio-group>
        </div>
      </div>
    </div>

    <div class="subject-for-subjective">
      <div class="subject-for-title">主观题</div>
      <div class="el-icon-plus" @click="addSubject('subjective')" style=" margin-left: 30px; line-height: 40px; cursor: pointer;"></div>
      <div class="subject-item-subjective" :class="sub.stem === '' ? 'invalid' : ''" v-for="(sub,index) in subject.subjectForSubjective">
        <el-input style="width: calc(100% - 450px); float: left;" placeholder="请输入题目"  v-model="sub.stem"/>
        <div style="float: left; line-height: 40px;">设置分值：<input style="width: 30px; height: 30px;" v-model="sub.score" />分</div>

        <div style="margin: 7px 20px; float: left; line-height: 40px;">
          <el-radio-group v-model="sub.answerType" style=" display: flex; align-items: center; justify-content: space-around; flex-wrap: wrap">
            <el-radio label="自评" style="margin: 5px;">自评</el-radio>
            <el-radio label="组内互评" style="margin: 5px;">组内互评</el-radio>
            <el-radio label="组间互评" style="margin: 5px;">组间互评</el-radio>
          </el-radio-group>
        </div>

        <div class="el-icon-remove" @click="removeSubjectItem(index, 'subjective')" style="margin-left: 10px; cursor: pointer;
          line-height: 40px; float: left;"></div>
      </div>
    </div>

    <div class="inspire-date">
      <span>截止日期</span>
      <el-date-picker v-model="subject.inspireDate" type="date" placeholder="选择日期">
      </el-date-picker>
    </div>

    <div class="submit-btn-container">
      <el-button type="primary" @click="handleNextStep">下一步</el-button>
      <el-button type="primary" @click="handleUploadAnswer">提交</el-button>
    </div>
    <select-setting :dialogFormVisible="dialogFormVisible" @hide="hideDialog" @selectionsHandle="selectionsHandle">
    </select-setting>
  </div>
</template>

<script>
  import SubjectSelectItemSetting from './subjectSelectItemSetting'
  import {saveTask, getTaskDetailByTaskId} from '@/api/task'
  export default {
    name: "newTask",
    props:['taskId'],
    components:{'select-setting': SubjectSelectItemSetting},
    data(){
      return {
        dialogFormVisible:false,
        curSubjectIndex:0,//当前单选题的索引
        currentStep:'第一步：编辑题目',
        subject:{
          taskName:'',
          subjectForChoose:[],
          subjectForChooseScore:'',
          subjectForJudge:[],
          subjectForJudgeScore:'',
          subjectForSubjective:[],
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
        this.subject.weight = taskDetailInfo.task.weight;
        this.subject.totalScore = taskDetailInfo.task.totalScore;
        this.subject.inspireDate = taskDetailInfo.task.deadline;
        for(let item of taskDetailInfo.questionList){
          switch (item.questionType) {
            case "单选题":
              this.subject.subjectForChoose.push({stem:item.stems, chooseItem:item.options.split(','), answer:item.answers});
              break;
            case "判断题":
              this.subject.subjectForJudge.push({stem:item.stems, chooseItem:["是","否"], answer:item.answers});
              break;
            case "主观题":
              this.subject.subjectForSubjective.push({stem:item.stems, chooseItem:[], answer:item.answers,
                answerType:item.markType, score: item.score});
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
            this.subject.subjectForSubjective.push({stem:'', chooseItem:[], answer:'', answerType:'', score:''});
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
          this.$router.push({name:'uploadAnswer', params:{taskId:response.data}});
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
            self.$message({
              showClose: true,
              type: 'warning',
              message: response.msg
            });
            return;
          }
          self.$message({
            showClose: true,
            type: 'success',
            message: "答案上传成功！"
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
          id:this.taskId,
          name:this.subject.taskName,
          courseId:this.$store.getters.courseId,
          weight: this.subject.weight,
          deadline:this.subject.inspireDate,
          totalScore: this.subject.totalScore
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
            markType: item.answerType,
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
</style>
