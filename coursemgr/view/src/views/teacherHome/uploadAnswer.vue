<template xmlns="http://www.w3.org/1999/html">
  <div class="main-container">
    <div class="title"></div>
    <div class="step">{{currentStep}}</div>
    <div class="subjectStatic">选择题 {{subjectTask.subjectForChoose.length}} 道，判断题 {{subjectTask.subjectForJudge.length}} 道，
      主观题 {{subjectTask.subjectForSubjective.length}} 道，总分值 {{subjectTask.totalScore}} 分，权重： {{subjectTask.weight}} %。</div>
    <span>截止日期：{{subjectTask.inspireDate}}</span>

    <div class="subject-for-choose" v-if="subjectTask.subjectForChoose.length !== 0">
      <div style="float: left; margin-right: 20px; line-height: 40px; background-color: gray; padding: 0 10px;">单选题</div>
      <div style="float: left; line-height: 40px;">每题 <span>{{subjectTask.subjectForChooseScore}}</span> 分</div>
      <div class="subject-item" :class="sub.answer === '' ? 'invalid':''" v-for="(sub,index) in subjectTask.subjectForChoose">
        <el-input style="width: calc(100% - 130px); margin-top: 10px;" placeholder="请输入题目"  v-model="sub.stem" disabled/>
        <div style="margin: 20px;">
          <el-radio-group v-model="sub.answer" style=" display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap">
            <el-radio v-for="(cItem,idx) in sub.chooseItem" :label="cItem" :key="idx" style="margin: 5px;">{{cItem}}</el-radio>
          </el-radio-group>
        </div>
      </div>
    </div>

    <div class="subject-for-judge" v-if="subjectTask.subjectForJudge.length !== 0">
      <div style="float: left; margin-right: 20px; line-height: 40px; background-color: gray; padding: 0 10px;">判断题</div>
      <div style="float: left; line-height: 40px;">每题 <span>{{subjectTask.subjectForJudgeScore}}</span> 分</div>
      <div class="subject-item" :class="sub.answer === '' ? 'invalid':''" v-for="(sub,index) in subjectTask.subjectForJudge">
        <el-input style="width: calc(100% - 130px); margin-top: 10px;" placeholder="请输入题目" v-model="sub.stem"/>
        <div style="margin: 20px;">
          <el-radio-group v-model="sub.answer" style=" display: flex; align-items: center; justify-content: space-around; flex-wrap: wrap">
            <el-radio v-for="(cItem,idx) in sub.chooseItem" :label="cItem" :key="idx" style="margin: 5px;">{{cItem}}</el-radio>
          </el-radio-group>
        </div>
      </div>
    </div>

    <div class="subject-for-subjective" v-if="subjectTask.subjectForSubjective.length !== 0">
      <div style="float: left; width: 100%;">
        <div style="float: left; margin-right: 20px; line-height: 40px; background-color: gray; padding: 0 10px;">主观题</div>
      </div>
      <div class="subject-item" :class="sub.answer === '' ? 'invalid':''" v-for="(sub,index) in subjectTask.subjectForSubjective">
        <el-input style="width: calc(100% - 450px); float: left; margin-top: 10px;" placeholder="请输入题目" v-model="sub.stem" disabled/>
        <div style="float: left; line-height: 40px;">分值： <span>{{sub.score}}</span> 分</div>
        <div style="margin: 0px 20px; float: left; line-height: 40px;">
          {{sub.answerType}}
        </div>
        <el-input style="margin: 10px 0" v-model="sub.answer" type="textarea" placeholder="请在此作答"></el-input>

        <el-upload class="upload-demo"
          action="https://jsonplaceholder.typicode.com/posts/"
          :on-change="handleChange"
          :file-list="fileList3">
          <el-button size="small" type="primary">上传附件</el-button>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
        </el-upload>
      </div>
    </div>

    <div class="divider" style="height: 1px; background-color: gray; margin: 10px;"></div>

    <div style="display: flex; align-items: center; justify-content: center; margin: 10px 0">
      <el-button type="primary" @click="submit">提交</el-button>
    </div>

  </div>
</template>

<script>
  export default {
    name: "uploadAnswer",
    components:{},
    data(){
      return {
        dialogFormVisible:false,
        curSubjectIndex:0,//当前单选题的索引
        currentStep:'第二步：上传答案',
        subjectTask:{},
        fileList3: [{
          name: 'food.jpeg',
          url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
        }, {
          name: 'food2.jpeg',
          url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
        }]
      }
    },
    created(){
      //todo 截取路由传过来的数据
      debugger
      this.subjectTask = this.$route.params;
    },
    methods:{
      handleChange(file, fileList) {
        this.fileList3 = fileList.slice(-3);
      },
      submit(){

      }
    },
  }
</script>

<style scoped>
  .main-container{

  }
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
  .invalid{
    border: 1px dashed red;
  }
</style>
