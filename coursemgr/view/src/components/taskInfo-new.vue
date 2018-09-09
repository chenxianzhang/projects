<template>
  <div style="width: 1200px; margin: 0 auto; border: 1px solid #ee9900; padding: 5px;">
    <div class="task-name">
      <span style="width: 100px; display: inline-block; float: left; text-align: center; line-height: 40px">任务名称：</span>
      <el-input v-model="task.name" placeholder="请输入任务名称" style="width: calc(100% - 100px)"></el-input>
    </div>
    <div class="subjectStatic">总题数 {{task.subjects.length}} 道，
      总分值 <input v-model="totalScore" disabled/> 分，
      权重：<input type="number" min="0" max="100" v-model="task.weight" /> %。</div>
    <div class="subject-container">
      <div class="subject-item" v-for="(item, index) in task.subjects" style="padding-bottom: 10px; border-bottom: 1px solid #dff0d8">
        <!--题干设置区域-->
        <div style="margin-bottom:10px">
          <span>{{index + 1}}.</span>
          <el-input v-show="item.stem.indexOf('img') === -1 && !item.edit" v-model="item.stem"
                    placeholder="请设置题干" style="width: calc(100% - 240px)"></el-input>
          <el-input v-if="item.stem.indexOf('img') !== -1 || item.edit" v-html="item.stem"
                    style="width: calc(100% - 240px)"></el-input>

          <el-select v-model="item.questionType" placeholder="请选择题型">
            <el-option label="单选题" :value="SUBJECT_TYPE.CHOOSE"></el-option>
            <el-option label="判断题" :value="SUBJECT_TYPE.JUDGE"></el-option>
            <el-option label="主观题" :value="SUBJECT_TYPE.SUBJECTIVE"></el-option>
          </el-select>

          分数：<input type="number" min="0" max="100" v-model="item.score" style="width: 40px; height: 30px;" />分
          <el-button type="primary" @click="handleStemHighSetting(index)">高级设置</el-button>
          <Tinymce :height=200 v-if="item.edit" v-model="item.stem" style="margin-top: 5px;"/>
          <el-button v-if="item.edit" type="primary" @click="editConfirm(index, item)">确定</el-button>
        </div>
        <!--单选题 选项设置区域-->
        <div v-if="item.type === SUBJECT_TYPE.CHOOSE">
          <div v-for="(cItem, cIndex) in item.selections" style="margin-bottom: 5px;">
            <el-input v-show="item.selections[cIndex].optionDes.indexOf('img') === -1 && !item.selections[cIndex].edit" v-model="item.selections[cIndex].optionDes"
                      placeholder="请设置选项" style="width: calc(100% - 100px)"></el-input>
            <el-input v-if="item.selections[cIndex].optionDes.indexOf('img') !== -1 || item.selections[cIndex].edit" v-html="item.selections[cIndex].optionDes"
                      style="width: calc(100% - 100px)"></el-input>

            <el-tooltip content="添加选项" placement="top">
              <i class="el-icon-circle-plus-outline" @click="handleAddSelection(cIndex, item.selections)"/>
            </el-tooltip>
            <el-tooltip content="删除选项" placement="top">
              <i class="el-icon-remove-outline" @click="handleRemoveSelection(cIndex, item.selections)"/>
            </el-tooltip>
            <el-tooltip content="添加图片" placement="top">
              <i class="el-icon-picture-outline" @click="handleImageEditSelection(cIndex, item.selections)"/>
            </el-tooltip>
            <el-tooltip content="设置为答案" placement="top">
              <el-radio v-model="item.answer" :label="item.selections[cIndex].optionDes">{{emptyContent}}</el-radio>
            </el-tooltip>
            <Tinymce :height=200 v-if="item.selections[cIndex].edit" v-model="item.selections[cIndex].optionDes" style="margin: 5px"/>
            <el-button v-if="item.selections[cIndex].edit" type="primary" @click="editConfirm(cIndex, item.selections[cIndex])">确定</el-button>
          </div>
        </div>
        <!--主观题  答题设置-->
        <div v-if="item.type === SUBJECT_TYPE.SUBJECTIVE">
          <div style="margin: 7px 20px; line-height: 40px; float: left">
            <el-radio-group v-model="item.markType"
                            style=" display: flex; align-items: center; justify-content: space-around; flex-wrap: wrap">
              <el-radio label="SELF_EVA" style="margin: 5px;">自评</el-radio>
              <el-radio label="GROUP_INNER_EVA" style="margin: 5px;">组内互评</el-radio>
              <el-radio label="GROUP_INTERBLOCK_EVA" style="margin: 5px;">组间互评</el-radio>
            </el-radio-group>
          </div>
          <el-input type="textarea" v-model="item.answer"></el-input>
        </div>
        <!--编辑和完成编辑按钮-->
        <el-button type="primary" v-if="!item.edit" @click="handleSubjectEdit(index, item)">编辑</el-button>
        <el-button type="primary" v-if="item.edit" @click="handleSubjectFinish(index, item)">完成编辑</el-button>
      </div>
      <div class="subject-add el-icon-plus" @click="addSubject"></div>
      <div class="subject-add" @click="handleSaveTask">保存</div>
    </div>
  </div>
</template>

<script>
  import {SUBJECT_TYPE} from "../utils/statusUtil";
  import {Subject, Task, Selection} from "../models/task-model";
  import Tinymce from '../components/Tinymce/index'
  import {saveTask} from '@/api/task'

  export default {
    name: "taskInfo-new",
    components:{
      Tinymce
    },
      data(){
          return{
            emptyContent:'',
            editContent:'',
            SUBJECT_TYPE:SUBJECT_TYPE,
            task: new Task(),
          }
      },
    computed:{
      totalScore: function(){
        let subjectiveScore = 0;
        for(let subject of this.task.subjects){
          subjectiveScore += +subject.score;
        }
        this.task.totalScore = subjectiveScore;
        return this.task.totalScore;
      }
    },
      methods:{
        addSubject(){
          let subject = new Subject();
          subject.type = SUBJECT_TYPE.CHOOSE;
          this.task.subjects.push(subject);
        },
        /**
         * handleStemHighSetting 题干高级设置   可添加图片
         * */
        handleStemHighSetting(index){
          this.$set(this.task.subjects[index], 'edit', true);
          this.editContent = this.task.subjects[index].stem;
        },
        /**
         * handleSubjectEdit 编辑题干
         * */
        handleSubjectEdit(index, item){
          this.$set(this.task.subjects[index], 'edit', true);
        },
        /**
         * handleSubjectFinish 完成题干编辑
         * */
        handleSubjectFinish(index, item){
          this.$set(this.task.subjects[index], 'edit', false);
        },
        /**
         * handleAddSelection 添加选项
         * */
        handleAddSelection(cIndex, selections){
          selections.splice(cIndex + 1, 0, new Selection(selections.length + 1))
        },
        /**
         * handleRemoveSelection 删除选项
         * */
        handleRemoveSelection(cIndex, selections){
          if(selections.length === 1){
            this.$message.warning('至少需要一个选项！');
            return;
          }
          selections.splice(cIndex, 1);
        },
        /**
         * handleImageEditSelection 图片编辑选项
         * */
        handleImageEditSelection(cIndex, selections){
          selections[cIndex].edit = true;
          this.editContent = selections[cIndex].value;
        },
        /**
         * editConfirm 题干编辑确认
         * */
        editConfirm(index, item){
          this.$set(item, 'edit', false);

          if(item.optionDes){
            if(item.optionDes.indexOf('img') === -1){
              let stem = item.value.substring(3, item.optionDes.length - 4);
              this.$set(item, 'optionDes', stem);
            }
            return;
          }
          if(item.stem.indexOf('img') === -1){
            let stem = item.stem.substring(3, item.stem.length - 4);
            this.$set(item, 'stem', stem);
          }
        },
        /**
         * handleSaveTask 保存任务
         * */
        handleSaveTask(){
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
          });
        },
        /**
         * getSaveData  构造需要保存或者更新的task数据
         * params null
         * return {task:task, questionList:questions} 保存的数据结构
         * */
        getSaveData(){
          let task = {
            name:this.task.name,
            courseId:this.$route.params.courseId,
            weight: this.task.weight,
            deadline:this.task.inspireDate,
            totalScore: this.task.totalScore,
            markType:this.task.markType
          };
          let questionList = [];
          for(let item of this.task.subjects){
            let optionList = [];
            if(item.questionType === SUBJECT_TYPE.CHOOSE){
              for(let opItem of item.selections){
                optionList.push(opItem);
              }
            }
            questionList.push({
              taskQuestions:{
                stems:item.stem,
                questionType:item.questionType,
                score:item.score,
                answers:item.answer,
              },
              optionList:optionList
            });
          }
          return {task:task, questionList:questionList};
        },
      }
    }
</script>

<style scoped>
.subject-add{
  width: 100%;
  height: 40px;
  line-height: 40px;
  color: white;
  cursor: pointer;
  border-radius: 4px;
  background: #9cb945;
  text-align: center;
  margin-bottom: 10px;
}

.subjectStatic{
  margin: 5px 0;
}
.subjectStatic > input{
  width: 40px;
  height: 30px;
}
</style>
