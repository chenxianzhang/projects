<template>
  <div class="main-container">
    <div class="title"></div>
    <div class="step">{{currentStep}}</div>
    <div class="subjectStatic">选择题 {{subject.subjectForChoose.length}} 道，判断题 {{subject.subjectForJudge.length}} 道，
      主观题 {{subject.subjectForSubjective.length}} 道，总分值 {{subject.totalScore}} 分，权重： {{subject.weight}} %。</div>

    <div class="subject-for-choose">
        <div style="float: left; margin-right: 20px; line-height: 40px; background-color: gray; padding: 0 10px;">单选题</div>
        <div style="float: left; line-height: 40px;">设置分值：每题<input style="width: 30px; height: 30px;" />分</div>
        <div class="el-icon-plus" @click="addSubject('choose')" style=" margin-left: 30px; line-height: 40px; cursor: pointer;"></div>
        <div class="subject-item" v-for="(sub,index) in subject.subjectForChoose">
          <el-input style="width: calc(100% - 130px)" placeholder="请输入题目"/>
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
        <div style="float: left; line-height: 40px;">设置分值：每题<input style="width: 30px; height: 30px;" />分</div>
        <div class="el-icon-plus" @click="addSubject('judge')" style=" margin-left: 30px; line-height: 40px; cursor: pointer;"></div>
        <div class="subject-item" v-for="(sub,index) in subject.subjectForJudge">
          <el-input style="width: calc(100% - 130px)" placeholder="请输入题目"/>
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
      <div class="subject-item" v-for="(sub,index) in subject.subjectForSubjective">
        <el-input style="width: calc(100% - 130px)" placeholder="请输入题目"/>
        <div style="float: left; line-height: 40px;">设置分值：<input style="width: 30px; height: 30px;" />分</div>

        <div class="el-icon-remove" @click="removeSubjectItem(index, 'subjective')" style="margin-left: 10px; cursor: pointer;"></div>
        <div style="margin: 20px;">
          <el-radio-group v-model="sub.answerType" style=" display: flex; align-items: center; justify-content: space-around; flex-wrap: wrap">
            <el-radio label="自评" style="margin: 5px;">自评</el-radio>
            <el-radio label="组内互评" style="margin: 5px;">组内互评</el-radio>
            <el-radio label="组间互评" style="margin: 5px;">组间互评</el-radio>
          </el-radio-group>
        </div>
      </div>
    </div>

    <select-setting :dialogFormVisible="dialogFormVisible" @hide="hideDialog" @selectionsHandle="selectionsHandle">
    </select-setting>
  </div>
</template>

<script>
  import SubjectSelectItemSetting from './subjectSelectItemSetting'
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
              subjectForJudge:[],
              subjectForSubjective:[],
              totalScore:100,
              weight:20,
            }
          }
      },
      methods:{
        addSubject(type){
          switch (type) {
            case "choose":
              this.subject.subjectForChoose.push({chooseItem:[], answer:''});
              break;
            case "judge":
              this.subject.subjectForJudge.push({chooseItem:["是","否"], answer:''});
              break;
            case "subjective":
              this.subject.subjectForSubjective.push({chooseItem:[], answer:'', answerType:''});
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
          //todo 设置当前单选题的选项 index当前题目索引
          this.dialogFormVisible = true;
          this.curSubjectIndex = index;
        },
        hideDialog(val){
          this.dialogFormVisible = val;
        },
        selectionsHandle(selections){
          this.subject.subjectForChoose[this.curSubjectIndex].chooseItem = selections;
        },
      },
    }
</script>

<style scoped>
  .subject-item{
    margin: 10px 0;
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
</style>
