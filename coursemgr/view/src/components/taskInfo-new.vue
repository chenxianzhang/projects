<template>
  <div>
    <div class="task-name">
      <el-input v-model="task.name" placeholder="请输入任务名称"></el-input>
    </div>
    <div class="subject-statistic">分数统计</div>
    <div class="subject-container">
      <div class="subject-item" v-for="(item, index) in task.subjects" style="margin-bottom: 10px;">
        <!--题干设置区域-->
        <div style="margin-bottom:10px">
          <span>{{index + 1}}.</span>
          <el-input v-html="item.stem" v-model="item.stem" placeholder="请设置题干" style="width: 85%"></el-input>
          <el-button type="primary" v-show="!item.edit" @click="handleStemHighSetting(index)">高级设置</el-button>
        </div>
        <Tinymce :height=200 ref="editor" v-if="item.edit" v-model="item.stem" style="margin-bottom: 5px"/>
        <!--单选题 选项设置区域-->
        <div v-if="item.type === SUBJECT_TYPE.CHOOSE">
          <div v-for="(cItem, cIndex) in item.selections" style="margin-bottom: 5px;">
            <el-input v-model="item.selections[cIndex]" :key="cIndex" style="width: calc(100% - 80px)"/>
            <i class="el-icon-circle-plus-outline" @click="handleAddSelection(cIndex)"/>
            <i class="el-icon-remove-outline" @click="handleRemoveSelection(cIndex)"/>
            <i class="el-icon-picture-outline" @click="handleImageEditSelection(cIndex)"/>
          </div>
        </div>
        <!--编辑和完成编辑按钮-->
        <el-button type="primary" v-if="!item.edit" @click="handleSubjectEdit(index, item)">编辑</el-button>
        <el-button type="primary" v-if="item.edit" @click="handleSubjectFinish(index, item)">完成编辑</el-button>
      </div>
      <div class="subject-add" @click="addSubject">
        添加题目
      </div>
    </div>
  </div>
</template>

<script>
  import {SUBJECT_TYPE} from "../utils/statusUtil";
  import {Subject, Task} from "../models/task-model";
  import Tinymce from '../components/Tinymce/index'

  export default {
    name: "taskInfo-new",
    components:{
      Tinymce
    },
      data(){
          return{
            SUBJECT_TYPE:SUBJECT_TYPE,
            task: new Task(),
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
        handleAddSelection(cIndex){

        },
        /**
         * handleRemoveSelection 删除选项
         * */
        handleRemoveSelection(cIndex){

        },
        /**
         * handleImageEditSelection 图片编辑选项
         * */
        handleImageEditSelection(cIndex){

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
}
</style>
