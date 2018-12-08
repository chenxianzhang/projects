<template>
  <div style="margin: 29px 22px 65px 22px; " class="task-new-container">
    <div class="task-name">
      <el-row :gutter="20">
        <el-col :span="12" style="height: 35px;">
          <span style="width: 100px; display: inline-block; float: left; text-align: center; line-height: 35px">任务名称：</span>
          <el-input v-model="task.name" placeholder="请输入任务名称" style="width: calc(100% - 100px);"></el-input>
        </el-col>
        <el-col :span="12">
          <el-row :gutter="20" style="height: 35px">
            <el-date-picker
              style="width: calc(100% - 80px);"
              v-model="taskDate"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </el-row>
        </el-col>
      </el-row>
    </div>
    <div class="subjectStatic">总题数 {{task.subjects.length}} 道， 总分值 <input v-model="totalScore" disabled/> 分
      <div v-show="haSubjective" style="margin: 7px 20px; display: inline-flex;">
        <el-radio-group v-model="task.markType"
                        style=" display: flex; align-items: center; justify-content: space-around; flex-wrap: wrap">
          <el-radio label="SELF_EVA" style="margin: 5px;">自评</el-radio>
          <el-radio label="GROUP_INNER_EVA" :disabled="courseGroupStatus === 0 ? true : false" style="margin: 5px;">
            组内互评
          </el-radio>
          <el-radio label="GROUP_INTERBLOCK_EVA" :disabled="courseGroupStatus > 1 ? false : true" style="margin: 5px;">
            组间互评
          </el-radio>
        </el-radio-group>
        <span style="margin-left: 15px;font-size: 12px; color: red">{{ tipinfo }}</span>
      </div>
    </div>
    <div class="subject-container">
      <div class="subject-item" v-for="(item, index) in task.subjects">
        <!--题干设置区域-->
        <div style="display: flex; align-items: center; width: 100%">
          <span>{{index + 1}}.</span>
          <el-input v-show="item.stem.indexOf('img') === -1 && !item.edit" v-model="item.stem"
                    placeholder="请设置题干" style="width: calc(100% - 430px); max-height: 300px; overflow: auto;"></el-input>
          <el-input v-show="item.stem.indexOf('img') !== -1 || item.edit" v-html="item.stem"
                    style="width: calc(100% - 430px); max-height: 300px; overflow: auto;"></el-input>

          <el-select v-model="item.questionType" placeholder="请选择题型" style="width: 122px; margin-left: 15px">
            <el-option label="单选题" :value="SUBJECT_TYPE.CHOOSE"></el-option>
            <el-option label="判断题" :value="SUBJECT_TYPE.JUDGE"></el-option>
            <el-option label="主观题" :value="SUBJECT_TYPE.SUBJECTIVE"></el-option>
          </el-select>

          分数：<input type="number" min="0" max="100" v-model="item.score" style="width: 40px; height: 30px;" />分
          <el-tooltip content="高级设置" placement="top">
            <img src="../../static/img/newTask/setting.png" @click="handleStemHighSetting(index)"
                 style="cursor: pointer; margin-left: 10px;"/>
          </el-tooltip>
          <el-tooltip content="删除" placement="top">
            <img src="../../static/img/newTask/remove.png" @click="removeQuestion(index)"
                 style="cursor: pointer; margin-left: 10px;"/>
          </el-tooltip>
        </div>
        <div v-if="item.edit" style="width: calc(100% - 422px); margin-left: 8px;">
          <Tinymce :height=100 v-model="item.stem" style="margin-top: 5px; margin-bottom: 5px" />
          <el-button type="primary" @click="editConfirm(index, item, task.subjects)">确定</el-button>
        </div>
        <!--判断题 选项设置区域-->
        <div v-if="item.questionType === SUBJECT_TYPE.JUDGE" style="width: 100%;">
          <div style="margin-top: 20px; margin-left: 13px; margin-bottom: 10px">选项设置：</div>
          <div style="margin-bottom: 5px;">
            <!--<span>A. </span>-->
            <el-input value="是" disabled style="width: calc(100% - 430px); margin-left: 13px; margin-right: 10px;"></el-input>
            <el-tooltip content="设置为答案" placement="top">
              <el-radio v-model="item.answer" label="是">{{emptyContent}}</el-radio>
            </el-tooltip>
          </div>
          <div style="margin-bottom: 5px;">
            <!--<span>B. </span>-->
            <el-input value="否" disabled style="width: calc(100% - 430px); margin-left: 13px; margin-right: 10px;">否</el-input>
            <el-tooltip content="设置为答案" placement="top">
              <el-radio v-model="item.answer" label="否">{{emptyContent}}</el-radio>
            </el-tooltip>
          </div>
        </div>
        <!--单选题 选项设置区域-->
        <div v-if="item.questionType === SUBJECT_TYPE.CHOOSE" style="width: 100%;">
          <div style="margin-top: 20px; margin-left: 13px; margin-bottom: 10px">选项设置：</div>
          <div v-for="(cItem, cIndex) in item.selections" style="margin-bottom: 5px; margin-left: -3px; ">
            <span v-show="false">{{setOptionTag(cItem, cIndex)}}</span>
            <span>{{String.fromCharCode((65+cIndex))}}. </span>
            <el-input v-show="item.selections[cIndex].optionDes.indexOf('img') === -1 && !item.selections[cIndex].edit"
                      v-model="item.selections[cIndex].optionDes"
                      placeholder="请设置选项"
                      style="width: calc(100% - 430px); max-height: 300px; overflow: auto;"></el-input>
            <el-input v-if="item.selections[cIndex].optionDes.indexOf('img') !== -1 || item.selections[cIndex].edit"
                      v-html="item.selections[cIndex].optionDes"
                      style="width: calc(100% - 430px); max-height: 300px; overflow: auto;"></el-input>

              <el-tooltip content="添加选项" placement="top">
                <div class="icon-option-add" @click="handleAddSelection(cIndex, item.selections)"></div>
              </el-tooltip>
              <el-tooltip content="删除选项" placement="top">
                <div class="icon-option-remove" @click="handleRemoveSelection(cIndex, item.selections)"></div>
              </el-tooltip>
              <el-tooltip content="添加图片" placement="top">
                <div class="icon-option-pic" @click="handleImageEditSelection(cIndex, item.selections)"></div>
              </el-tooltip>

            <el-tooltip content="设置为答案" placement="top">
              <el-radio v-model="item.answer" :label="String.fromCharCode((65+cIndex))">{{emptyContent}}</el-radio>
            </el-tooltip>

            <div v-if="item.selections[cIndex].edit" style="width: calc(100% - 422px); margin-left: 8px;">
              <Tinymce :height=100 v-model="item.selections[cIndex].optionDes" style="margin: 5px" />
              <el-button type="primary" @click="editConfirm(cIndex, item.selections[cIndex])">确定</el-button>
            </div>
          </div>
        </div>
        <!--主观题  答题设置-->
        <div v-if="item.questionType === SUBJECT_TYPE.SUBJECTIVE" class="answer-title">参考答案</div>
        <div v-if="item.questionType === SUBJECT_TYPE.SUBJECTIVE"
             style="width: calc(100% - 422px); margin-left: 8px;">
          <Tinymce :height=100 v-model="item.answer" placeholder="请填写主观题答案" style="margin: 5px" />
        </div>
        <!--编辑和完成编辑按钮-->
        <el-button type="primary" v-show="item.edit" @click="handleSubjectFinish(index, item)">完成编辑</el-button>
      </div>
      <!--添加按钮-->
      <div class="add-sub-container">
        <div class="subject-add" @click="addSubject">
          <img src="../../static/img/newTask/subject-plus.png"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getTaskDetailByTaskId } from '@/api/task'
import { checkCourseGrouped } from '@/api/group'
import { SUBJECT_TYPE } from '../utils/statusUtil'
import { Subject, Task, Selection } from '../models/task-model'
import Tinymce from '../components/Tinymce/index'

export default {
  name: 'taskInfo-new',
  props: ['operateType', 'taskId'],
  components: {
    Tinymce
  },
  data() {
    return {
      taskDate:[],
      emptyContent: '',
      editContent: '',
      SUBJECT_TYPE: SUBJECT_TYPE,
      task: new Task(),
      courseGroupStatus: 0,
      tipinfo: ''
    }
  },
  created() {
    checkCourseGrouped({ courseId: this.variables.courseId }).then(res => {
      if (res.data.status === 0) {
        this.$msg.warning(res.data.msg);
        return
      }
      this.courseGroupStatus = res.data;
      if (this.courseGroupStatus === 0) {
        this.tipinfo =
          '当前尚有学生未进行分组，请先设置分组或者采用其他评分方式'
      } else if (this.courseGroupStatus === 1) {
        this.tipinfo = '当前课程只存在一个组，不能选择组间互评'
      } else {
        this.tipinfo = ''
      }
    });

    //获取任务详情
    if (!this.taskId || this.taskId === '') {
      return
    }
    getTaskDetailByTaskId({ taskId: this.taskId }).then(resp => {
      if (resp.status === 0) {
        this.$message.warning('获取任务信息失败');
        return
      }
      //根据结果设置subject值
      this.setSubjectByTaskDetailInfo(resp.data)
    })
  },
  computed: {
    totalScore: function() {
      let subjectiveScore = 0
      for (let subject of this.task.subjects) {
        subjectiveScore += +subject.score
      }
      this.task.totalScore = subjectiveScore
      return this.task.totalScore
    },
    haSubjective() {
      for (let item of this.task.subjects) {
        if (item.questionType === SUBJECT_TYPE.SUBJECTIVE) {
          return true
        }
      }
      return false
    }
  },
  mounted() {
    this.$on('reset', () => {
      // debugger
      this.task = new Task();
      this.taskDate = [];
    })
  },
  methods: {
    /**
     * handleTextChange
     * */
    // handleTextChange(val){
    //   debugger
    //   if (val.indexOf('<p>') !== -1) {
    //     val = val.substring(3, val.length - 4)
    //   }
    // },
    /**
     * 根据接口返回的任务信息结果，构造subject对象的值
     * params taskDetailInfo
     * return null
     **/
    setSubjectByTaskDetailInfo(taskDetailInfo) {
      if (!taskDetailInfo) {
        return
      }
      this.task.id = this.taskId;
      this.task.name = taskDetailInfo.task.name;
      this.task.weight = taskDetailInfo.task.weight;
      this.task.totalScore = taskDetailInfo.task.totalScore;
      this.task.inspireDate = taskDetailInfo.task.deadline;
      this.task.startDate = taskDetailInfo.task.startTime;
      this.taskDate = [taskDetailInfo.task.startTime, taskDetailInfo.task.deadline];
      this.task.markType = taskDetailInfo.task.markType;
      for (let item of taskDetailInfo.questionList) {
        let subject_c = new Subject();
        subject_c.id = item.taskQuestions.id;
        subject_c.edit = false;
        subject_c.selections = item.optionList;
        subject_c.selections &&
          subject_c.selections.forEach(item => {
            item.edit = false
          });
        subject_c.answer = item.taskQuestions.answers;
        subject_c.score = item.taskQuestions.score;
        subject_c.stem = item.taskQuestions.stems;
        subject_c.questionType = item.taskQuestions.questionType;
        this.task.subjects.push(subject_c);
      }
    },
    addSubject() {
      let subject = new Subject();
      subject.type = SUBJECT_TYPE.CHOOSE;
      this.task.subjects.push(subject)
    },
    /**
     * handleStemHighSetting 题干高级设置   可添加图片
     * */
    handleStemHighSetting(index) {
      this.$set(this.task.subjects[index], 'edit', true)
      this.editContent = this.task.subjects[index].stem
    },
    /**
     * removeQuestion 删除题目
     * */
    removeQuestion(index) {
      this.task.subjects.splice(index, 1)
    },
    /**
     * handleSubjectEdit 编辑题干
     * */
    handleSubjectEdit(index, item) {
      this.$set(this.task.subjects[index], 'edit', true)
    },
    /**
     * handleSubjectFinish 完成题干编辑
     * */
    handleSubjectFinish(index, item) {
      if (item.stem && item.stem.indexOf('img') !== -1) {
        item.stem = item.stem.substring(3, item.stem.length - 4)
      }
      this.$set(this.task.subjects[index], 'edit', false)
    },
    /**
     * handleAddSelection 添加选项
     * */
    handleAddSelection(cIndex, selections) {
      selections.splice(cIndex + 1, 0, new Selection(selections.length + 1))
    },
    /**
     * handleRemoveSelection 删除选项
     * */
    handleRemoveSelection(cIndex, selections) {
      if (selections.length === 1) {
        this.$message.warning('至少需要一个选项！');
        return
      }
      selections.splice(cIndex, 1)
    },
    /**
     * handleImageEditSelection 图片编辑选项
     * */
    handleImageEditSelection(cIndex, selections) {
      selections[cIndex].edit = true
      // this.editContent = selections[cIndex].value
    },
    /**
     * editConfirm 题干编辑确认
     * */
    editConfirm(index, item, data) {
      this.$set(item, 'edit', false)

      if (item.optionDes) {
        if (item.optionDes.indexOf('img') === -1) {
          let stem = item.optionDes
          if (item.optionDes.indexOf('<p>') !== -1) {
            stem = item.optionDes.substring(3, item.optionDes.length - 4)
          }
          this.$set(item, 'optionDes', stem)
        }
        return
      }
      if (item.stem.indexOf('img') === -1) {
        let stem = item.stem
        if (item.stem.indexOf('<p>') !== -1) {
          stem = item.stem.substring(3, item.stem.length - 4)
        }
        this.$set(data[index], 'stem', stem)
      }
    },
    /**
     * taskVerify 任务有效性校验
     * params selections 选项
     * return null
     **/
    taskVerify() {
      this.task.startDate = this.taskDate[0];
      this.task.inspireDate = this.taskDate[1];
      debugger
      //题目校验--选项是否存在、题干是否存在、分值是否存在、总分值是否存在
      if (this.task.name.trim() === '') {
        this.$message.warning('请填写任务名称！');
        return false;
      }
      if (this.taskDate == [] || this.taskDate[0] == this.taskDate[1]) {
        this.$message.warning('请设置日期！');
        return false;
      }
      if (this.task.subjects.length === 0) {
        this.$message.warning('请添加相关题目！');
        return false;
      }
      for (let subject of this.task.subjects) {
        if (subject.questionType === '') {
          this.$message.warning('请选择题目类型！');
          return false;
        }
        if (subject.score == 0) {
          this.$message.warning('请设置分数！');
          return false;
        }
        if (subject.questionType === this.SUBJECT_TYPE.CHOOSE) {
          let validSelection = true;
          subject.selections.forEach((v, k) => {
            console.log(v + '  ' + k);
            if (v.optionDes === '') {
              validSelection = false;
              return;
            }
          })

          if (subject.answer.trim() === '' || subject.stem.trim() === '' || !validSelection) {
            this.$message.warning('请设置单选题相关内容！')
            return false
          }
        }
        if (subject.questionType === this.SUBJECT_TYPE.JUDGE) {
          if (subject.answer.trim() === '' || subject.stem.trim() === '') {
            this.$message.warning('请设置判断题题干或答案！')
            return false
          }
        }
        if (subject.questionType === this.SUBJECT_TYPE.SUBJECTIVE) {
          if (
            subject.answer.trim() === '' ||
            subject.stem.trim() === '' ||
            this.task.markType === ''
          ) {
            this.$message.warning('请设置主观题题干或答案或评分方式！')
            return false
          }
        }
      }
      return true
    },
    setOptionTag(cItem, cIndex) {
      cItem.optionTag = String.fromCharCode(65 + cIndex)
    }
  }
}
</script>

<style scoped>
  .task-name {
    background-color: #ffffff;
    height: 65px;
    padding-top: 15px;
  }

  .add-sub-container{
    height: 80px;
    background-color: #ffffff;
    padding: 0 20px;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .subject-add {
    width: calc(100% - 40px);
    height: 50px;
    line-height: 50px;
    color: white;
    cursor: pointer;
    background: #009788;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .subjectStatic {
    margin-top: 12px;
    height: 62px;
    background-color: #F6F6F6;
    line-height: 62px;
    padding-left: 30px;
  }

  .subjectStatic > input {
    width: 40px;
    height: 30px;
  }

  .el-button + .el-button {
    margin-left: 0;
  }

  .subject-item {
    padding-left: 30px;
    padding-top: 16px;
    padding-bottom: 23px;
    border: 1px solid #EBEBEB;
    background-color: #ffffff;
  }

  .icon-option-add{
    display: inline-block;
    height: 19px;
    width: 19px;
    margin-left: 15px;
    margin-right: 5px;
    cursor: pointer;
    background-image: url("../../static/img/newTask/icon-option-add.png");
  }

  .icon-option-remove{
    display: inline-block;
    height: 19px;
    width: 19px;
    margin-right: 5px;
    cursor: pointer;
    background-image: url("../../static/img/newTask/icon-option-sub.png");
  }

  .icon-option-pic{
    display: inline-block;
    height: 19px;
    width: 25px;
    margin-right: 5px;
    cursor: pointer;
    background-image: url("../../static/img/newTask/icon-pic.png");
  }
  .answer-title{
    color: #009788;
    width: 80px;
    text-align: center;
    border-radius: 20px;
    margin-top: 10px;
    margin-left: 10px;
    border: 1px solid #009788;
  }
</style>
<style>
  .task-new-container .el-input__inner {
    height: 35px;
    line-height: 35px;
    border-radius: 0;
  }

  task-new-container .task-name .el-input__icon{
    line-height: 35px !important;
  }

</style>
