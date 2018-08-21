<template>
  <div class="main-container">
    <div class="title"></div>
    <div class="step">{{currentStep}}</div>
    <div class="subjectStatic">选择题 {{subject.subjectForChoose.length}} 道，判断题 {{subject.subjectForJudge.length}} 道，
      主观题 {{subject.subjectForSubjective.length}} 道，总分值 {{subject.totalScore}} 分，权重： {{subject.weight}} %。</div>

    <div class="subjectForChoose">
      <div>
        <div>单选题</div>
        <div>设置分值：每题<el-input style="width: 30px;" />分</div>
        <div class="el-icon-plus" @click="addChooseSubject"></div>
        <div class="subject-item" v-for="(sub,index) in subject.subjectForChoose">
          <el-input/><div class="el-icon-remove" @click="removeChooseSubjectItem(index)"></div>
          <div>
            <el-radio-group v-model="sub.answer">
              <el-radio v-for="(cItem,idx) in sub.chooseItem" :label="cItem" :key="idx">cItem</el-radio>
            </el-radio-group>
            <div class="el-icon-setting" @click="setChooseItems(index)">设置选项</div>
          </div>
        </div>
      </div>
    </div>
    <select-setting :dialogFormVisible="dialogFormVisible" @hide="hideDialog" @selection="setSelection(selections)">
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
              subjectForChoose:[{
                chooseItem:[],
                answer:''
              }],
              subjectForJudge:[{
                chooseItem:[],
                answer:''
              }],
              subjectForSubjective:[{
                chooseItem:[],
                answer:''
              }],
              totalScore:100,
              weight:20,
            }
          }
      },
      methods:{
        addChooseSubject(){
          this.subject.subjectForChoose.push({chooseItem:[], answer:''});
        },
        removeChooseSubjectItem(index){
          this.subject.subjectForChoose.splice(index, 1);
        },
        setChooseItems(index){
          //todo 设置当前单选题的选项 index当前题目索引
          this.dialogFormVisible = true;
          this.curSubjectIndex = index;
        },
        hideDialog(val){
          this.dialogFormVisible = val;
        },
        setSelection(selections){
          this.subject.subjectForChoose[this.curSubjectIndex].chooseItem = selections;
        },
      },
    }
</script>

<style scoped>

</style>
