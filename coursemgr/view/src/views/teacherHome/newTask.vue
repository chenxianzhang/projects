<template>
  <div class="main-container">
    <div class="title"></div>
    <div class="step">{{currentStep}}</div>
    <div class="subjectStatic">选择题 {{subject.subjectForChoose.length}} 道，判断题 {{subject.subjectForJudge.length}} 道，
      主观题 {{subject.subjectForSubjective.length}} 道，
      总分值 <input v-model="subject.totalScore" style="width: 30px; height: 30px;" /> 分，
      权重：<input v-model="subject.weight" style="width: 30px; height: 30px;" /> %。</div>

    <div class="subject-for-choose">
        <div style="float: left; margin-right: 20px; line-height: 40px; background-color: gray; padding: 0 10px;">单选题</div>
        <div style="float: left; line-height: 40px;">设置分值：每题<input v-model="subject.subjectForChooseScore" style="width: 30px; height: 30px;" />分</div>
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
        <div style="float: left; margin-right: 20px; line-height: 40px; background-color: gray; padding: 0 10px;">判断题</div>
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
      <div style="float: left; margin-right: 20px; line-height: 40px; background-color: gray; padding: 0 10px;">主观题</div>
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

    <div style="display: flex; align-items: center; justify-content: center">
      <el-button type="primary" @click="nextStep">下一步</el-button>
    </div>

    <select-setting :dialogFormVisible="dialogFormVisible" @hide="hideDialog" @selectionsHandle="selectionsHandle">
    </select-setting>
  </div>
</template>

<script>
  import SubjectSelectItemSetting from './subjectSelectItemSetting'
  import {saveTask} from '@/api/task'
    export default {
      name: "newTask",
      components:{'select-setting': SubjectSelectItemSetting},
      data(){
          return {
            dialogFormVisible:false,
            curSubjectIndex:0,//当前单选题的索引
            currentStep:'第一步：编辑题目',
            subject:{
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
      methods:{
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
        setChooseItems(index){
          //设置当前单选题的选项 index当前题目索引
          this.dialogFormVisible = true;
          this.curSubjectIndex = index;
        },
        hideDialog(val){
          this.dialogFormVisible = val;
        },
        selectionsHandle(selections){
          this.subject.subjectForChoose[this.curSubjectIndex].chooseItem = selections;
        },
        nextStep(){
          //题目校验--选项是否存在、题干是否存在、分值是否存在、总分值是否存在
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
          // this.subject.inspireDate = this.subject.inspireDate.toLocaleDateString();


          let self  = this;
          let task = {name:'',courseId:this.$store.getters.courseId, weight: this.subject.weight, deadline:this.subject.inspireDate, totalScore: this.subject.totalScore};
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
          saveTask({task:task, questionList:questions}).then(response=>{
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
              message: "保存成功！"
            });
            self.$router.push({name:'uploadAnswer', params:self.subject});
          });


        }
      },
    }
</script>

<style scoped>
  .subject-item{
    margin: 10px 0;
    padding: 5px;
  }
  .subject-item-subjective{
    margin: 10px 0;
    padding: 5px;
    height: 55px;
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
</style>
