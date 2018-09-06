<template>
  <div>
    <div class="task-name">
      <el-input v-model="task.name"></el-input>
    </div>
    <div class="subject-statistic">分数统计</div>
    <div class="subject-container">
      <div class="subject-item" v-for="(item, index) in task.subjects">
        <!--题干设置区域-->
        <div>
          <span>{{index + 1}}.</span>
          <el-input v-model="item.stem" placeholder="请设置题干"></el-input>
          <el-button type="primary" @click="handleStemHighSetting(index)">高级设置</el-button>
        </div>
        <subject-add-comp v-if="item.edit" :defaultMsg="defaultMsg" :config="config"></subject-add-comp>
        <!--单选题 选项设置区域-->
        <div v-if="item.type === SUBJECT_TYPE.CHOOSE">

        </div>
        <!--编辑和完成编辑按钮-->
        <el-button type="primary" v-if="!item.edit" @click="handleSubjectEdit(index, item)">编辑</el-button>
        <el-button type="primary" v-if="item.edit" @click="handleSubjectFinish(index, item)">完成编辑</el-button>
      </div>
      <div class="subject-add">
        <el-button type="primary" @click="addSubject">添加题目</el-button>
      </div>
    </div>
  </div>
</template>

<script>
  import {SUBJECT_TYPE} from "../utils/statusUtil";
  import {Subject, Task} from "../models/task-model";
  import SubjectAddComp from '../components/subjectAddComp'

  export default {
    name: "taskInfo-new",
    components:{
      SubjectAddComp
    },
      data(){
          return{
            SUBJECT_TYPE:SUBJECT_TYPE,
            task: new Task(),
            defaultMsg: '这里是UE测试',
            config: {
              initialFrameWidth: null,
              initialFrameHeight: 350
            },
          }
      },
      methods:{
        addSubject(){
          let subject = new Subject();
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
        }
      }
    }
</script>

<style scoped>

</style>
