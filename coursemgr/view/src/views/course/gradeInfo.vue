<template>
  <div>
    <div style="display: flex; align-items: center; justify-content: space-between">
      <div style="display: flex; align-items: center; justify-content: center;">
        <!--<i class="custom-icon-course-info"></i>-->
        <!--<span class="title">当前课程：{{courseName}}</span>-->
        <!--<span style="margin-left: 10px; font-weight: bold; color: #009589">THE CURRENT COURSE</span>-->

      </div>
      <div class="buttons">
        <el-button size="small" @click="download()">导出为excel</el-button>
        <el-button size="small" @click="exportZip">过程性打包</el-button>
      </div>
    </div>

    <el-table :data="tableData" :height="tableHeight"
              :header-cell-style="{background:'rgba(28, 77, 125, 0.8)', color:'white', fontWeight:'bold'}" >
      <el-table-column v-for="(task,index) in columns"
                       :label="task.label"
                       :prop="task.prop"
                       align="center"
                       :key="index">
        <template slot-scope="scope">
          <div v-if="task.type && task.type==='link'"
               @click.stop="viewScoreDetail(task.taskId, scope.row.studentNo, scope.row[task.prop])"
               style="cursor: pointer; color: #ee9900;">
            {{scope.row[task.prop]}}
          </div>
          <div v-else>{{scope.row[task.prop]}}</div>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="isStudent" class="statistic">
      <div class="left">
        <div class="card-title-span">
            <span style="display: flex; align-items: center; justify-content: center;">
              <div style="height: 22px; width: 4px; background-color: #009687; margin:0 4px"></div>
              <div style="height: 22px; line-height: 22px; margin:0 4px">任务得分统计</div>
              <div style="height: 22px; line-height: 22px; color: #ADADAD; margin:0 4px">TASK SCORE STATISTIC</div>
              <!--<div style="height: 20px; width: 35px; border-radius: 10px; background-color: #FE9226; margin-right: 10px;"></div>-->
              <!--<span style=" margin-right: 15px;">得分</span>-->
              <!--<div style="height: 20px; width: 35px; border-radius: 10px; background-color: #7266BA; margin-right: 10px;"></div>-->
              <!--<span>总分</span>-->
            </span>
        </div>
        <div style="height: calc(100% - 54px); width: 100%; background-color: white;" ref="statisticTaskScoreEl"></div>
      </div>

      <div class="right">
        <div class="card-title-span">
            <span style="display: flex; align-items: center; justify-content: center;">
              <div style="height: 22px; width: 4px; background-color: #009687; margin:0 4px"></div>
              <div style="height: 22px; line-height: 22px; margin:0 4px">排名-总分-得分率统计</div>
              <div style="height: 22px; line-height: 22px; color: #ADADAD; margin:0 4px">THE STATISTICAL FIGURE</div>
          </span>
        </div>
        <div style="height: calc(100% - 54px); width: 100%; background-color: white;" ref="statisticSortEl"></div>
      </div>
    </div>
    <div class="pagination" v-if="!isStudent">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currPage"
        :page-sizes="[10, 15, 20, 30]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalCount" style="float: right">
      </el-pagination>
    </div>

    <!--查看当前任务答题情况-->
    <el-dialog v-if="showAnswer" :visible.sync="showAnswer" width="1240px" title="成绩信息">
      <grade-detail :taskId="taskSelect.taskId" :uId="taskSelect.uId" @remarkSuccess="remarkSuccess"></grade-detail>
    </el-dialog>
  </div>
</template>

<script>
  import gradeDetail from '../../components/gradeDetail'
  import { getAllGradeInfo, getStuGradeInfo, download, exportZip } from '@/api/grade'
  import { getCourseById } from "../../api/course";
  import {statStudentTaskScore, statStuSynthesizeInfo} from '@/api/statistics'

  export default {
    name: "gradeInfo",
    components:{gradeDetail},
    data(){
      return {
        tableHeight:'100%',
        taskStaticChart:null,
        sortChart:null,
        showAnswer: false,
        isStudent:false,
        courseName:'',
        taskList:[],
        totalCount: 0,
        pageSize: 10,
        currPage: 1,
        columns: [],
        tableData: [],
        taskSelect:{
          taskId:'',
          uId:''
        }
      }
    },
    created() {
      let self = this;
      this.isStudent = this.$store.state.user.roles.in_array('student');
      let cId = this.variables.courseId;
      getCourseById({courseId:cId})
        .then(resp=>{
          if(resp.status === 0){
            this.$message.warn('获取任务信息失败！');
            return;
          }
          this.courseName = resp.data.name;
        });

      if (this.isStudent) {
        //获取该学生该课程的所有任务的成绩
        getStuGradeInfo({courseId: cId, studentNo: this.$store.state.user.token})
          .then(resp=>{
            if (resp.status === 0) {
              self.$message.warning('获取成绩信息失败：' + resp.msg);
              return;
            }
            self.transferData([resp.data]);
            this.calcTableHeight();
          });
        return;
      }
      //获取当前课程所有学生的成绩
      this.getGradeByCourse(cId);
    },
    mounted(){
      if(this.isStudent){
        this.statisticTaskScore();
        this.statisticSort();

        window.onresize = ()=>{
          setTimeout(()=>{
            this.calcTableHeight();
          }, 100);
        }
      }
    },
    destroyed(){
      this.taskStaticChart = null;
      this.sortChart = null;
    },
    methods: {
      remarkSuccess(){
        this.showAnswer = false;
        this.getGradeByCourse(this.variables.courseId );
      },
      calcTableHeight(){
        let subHeight = 230;
        if(this.isStudent){
          subHeight = 510;
        }
        setTimeout(()=>{
          if(!document.getElementsByClassName('el-table__header-wrapper')[0]){
            return;
          }
          let totalHeight = document.body.getBoundingClientRect().height;
          let headHeight = document.getElementsByClassName('el-table__header-wrapper')[0].clientHeight;
          document.getElementsByClassName('el-table__body-wrapper')[0].style.height = totalHeight - subHeight - headHeight + 'px';
          document.getElementsByClassName('el-table__body-wrapper')[0].style.overflowY = 'auto';
        }, 100);
      },
      transferData(data) {
        if (!data) {
          return;
        }
        this.columns = [];
        this.tableData = [];
        this.columns.push({prop:"studentName", label: "姓名"});
        this.columns.push({prop:"studentNo", label: "学号"});
        this.columns.push({prop:"groupNo", label: "所在小组"});
        let hasInitCol = false;
        for (let item = 0; item < data.length; item++) {
          let tableItem = {};
          tableItem.studentName = data[item].studentName;
          tableItem.studentNo = data[item].studentNo;
          tableItem.groupNo = data[item].groupNo;

          data[item].studentTaskInfos && data[item].studentTaskInfos.length !== 0 && data[item].studentTaskInfos.forEach(task => {
            tableItem[task.taskId + "_score"] = task.score;
            if (!task.score && task.score !== 0) {
              tableItem[task.taskId + "_score"] = task.status;
            }
            if (!hasInitCol) {
              this.columns.push({prop:task.taskId + "_score",
                //  label: task.taskName + "(" + task.taskWeight + "%)", type:'link', taskId: task.taskId});
                label: task.taskName, type:'link', taskId: task.taskId});
            }
          });
          hasInitCol = true;
          tableItem.totalScore = data[item].totalScore;
          this.tableData.push(tableItem);
        }
        this.columns.push({prop:"totalScore", label: "总分"});
      },
      handleSizeChange(val) {
        this.pageSize = val;
        this.getGradeByCourse(this.variables.courseId);
      },
      handleCurrentChange(val) {
        this.currPage = val;
        this.getGradeByCourse(this.variables.courseId);
      },
      getGradeByCourse(courseId) {
        let self = this;
        getAllGradeInfo({courseId: courseId,
          pageSize: this.pageSize, currPage: this.currPage})
          .then(resp => {
            if (resp.status === 0) {
              self.$message.warning('获取成绩信息失败：' + resp.msg);
              return;
            }

            self.transferData(resp.data.pageData);
            self.totalCount = resp.data.totalCount;
            this.calcTableHeight();
          });
      },
      download() {
        download(this.variables.courseId, this.$store.state.user.token, this.isStudent);
      },
      exportZip() {
        exportZip(this.variables.courseId, this.$store.state.user.token, this.isStudent);
      },
      viewScoreDetail(taskId, uId, val){
        if(val === '未作答'){
          this.$message.warning('该学生还未答题，无可查看成绩！')
          return;
        }
        this.taskSelect.taskId = taskId;
        this.taskSelect.uId = uId;
        this.showAnswer = true;
      },
      statisticTaskScore(){
        let xData = [], score=[], totalScore=[];
        statStudentTaskScore({courseId: this.variables.courseId, studentNo:this.$store.state.user.token})
          .then(resp => {
            if (resp.status === 0) {
              console.log(resp.msg);
              this.$message.warning('获取任务完成情况失败！');
              return;
            }
            if(resp.data && resp.data.length !== 0){
              resp.data.forEach((item)=>{
                xData.push(item.taskName);
                score.push(item.score);
                totalScore.push(item.totalScore);
              });
            }
            let option = {
              barMaxWidth:60,
              grid:{
                top:40,
                left:40,
                right:50,
                bottom:30,
              },
              tooltip: {
                trigger: 'axis',
                axisPointer: {
                  type: 'shadow'
                }
              },
              // legend: {
              //   data: ['得分', '总分']
              // },
              calculable: true,
              xAxis: [
                {
                  name:'任务',
                  type: 'category',
                  axisTick: {show: false},
                  data: xData
                }
              ],
              yAxis: [
                {
                  type: 'value',
                  name:'分数'
                }
              ],
              series: [
                {
                  name: '得分',
                  type: 'bar',
                  barGap: 0,
                  data: score,
                  itemStyle:{
                    color:'#FE9226'
                  },
                },
                {
                  name: '总分',
                  type: 'bar',
                  data: totalScore,
                  itemStyle:{
                    color:'#7266BA'
                  },
                }
              ]
            };
            this.taskStaticChart = this.$echarts.init(this.$refs.statisticTaskScoreEl);
            this.taskStaticChart.setOption(option, true);
            window.onresize = () => {
              this.taskStaticChart.resize();
            }
          });
      },
      statisticSort(){
        statStuSynthesizeInfo({courseId: this.variables.courseId, studentNo:this.$store.state.user.token})
          .then(resp => {
            if (resp.status === 0) {
              console.log(resp.msg);
              this.$message.warning('获取综合统计情况失败！');
              return;
            }
            debugger
            let option = {
              color:['#7266BA','#02C9B6','#FE9226'],
              tooltip : {
                formatter: "{a}：{c}"
              },
              series : [
                {
                  name: '名次',
                  type: 'gauge',
                  min: 0,
                  max: resp.data.totalStudentCnt/5===0 ? resp.data.totalStudentCnt : (resp.data.totalStudentCnt/5 + 1)*5,
                  splitNumber: resp.data.totalStudentCnt/5 === 0 ? resp.data.totalStudentCnt : (resp.data.totalStudentCnt/5 + 1),
                  radius: '85%',
                  axisLine: {         // 坐标轴线
                    lineStyle: {       // 属性lineStyle控制线条样式
                      color: [[0.2, '#7266BA'], [0.8, '#02C9B6'], [1, '#FE9226']],
                      width: 10,
                    }
                  },
                  axisTick: {            // 坐标轴小标记
                    length: 15,        // 属性length控制线长
                    lineStyle: {       // 属性lineStyle控制线条样式
                      color: 'auto'
                    }
                  },
                  splitLine: {           // 分隔线
                    length: 20,         // 属性length控制线长
                    lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                      color: 'auto'
                    }
                  },
                  axisLabel: {
                    backgroundColor: 'auto',
                    borderRadius: 2,
                    color: '#eee',
                    padding: 3,
                    textShadowBlur: 2,
                    textShadowOffsetX: 1,
                    textShadowOffsetY: 1,
                    textShadowColor: '#222'
                  },
                  title : {
                    // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                    fontWeight: 'bolder',
                    fontSize: 14,
                    fontStyle: 'italic'
                  },
                  detail : {
                    fontWeight: 'bolder',
                    borderRadius: 3,
                    backgroundColor: '#444',
                    borderColor: '#aaa',
                    shadowBlur: 5,
                    shadowColor: '#333',
                    shadowOffsetX: 0,
                    shadowOffsetY: 3,
                    borderWidth: 2,
                    textBorderColor: '#000',
                    textBorderWidth: 2,
                    textShadowBlur: 2,
                    textShadowColor: '#fff',
                    textShadowOffsetX: 0,
                    textShadowOffsetY: 0,
                    fontFamily: 'Arial',
                    width: 40,
                    color: '#eee',
                    rich: {}
                  },
                  data:[{value: (!resp.data.rank || resp.data.rank === "") ? resp.data.totalStudentCnt : resp.data.rank, name: '名次'}]
                },
                {
                  name: '得分',
                  type: 'gauge',
                  center: ['28%', '55%'],    // 默认全局居中
                  radius: '50%',
                  min:0,
                  max:100,
                  splitNumber: 5,
                  endAngle:45,
                  axisLine: {            // 坐标轴线
                    lineStyle: {       // 属性lineStyle控制线条样式
                      width: 8,
                      color: [[0.2, '#7266BA'], [0.8, '#02C9B6'], [1, '#FE9226']],
                    }
                  },
                  axisTick: {            // 坐标轴小标记
                    length:6,        // 属性length控制线长
                    lineStyle: {      // 属性lineStyle控制线条样式
                      color: 'auto'
                    }
                  },
                  splitLine: {           // 分隔线
                    length:20,         // 属性length控制线长
                    lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                      color: 'auto'
                    }
                  },
                  pointer: {
                    width:5
                  },
                  title: {
                    offsetCenter: [0, '-30%'],       // x, y，单位px
                  },
                  detail: {
                    // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                    fontWeight: 'bolder',
                    fontSize: '16'
                  },
                  data:[{value: (!resp.data.totalScore || resp.data.totalScore === "") ? 0 : resp.data.totalScore, name: '得分'}]
                },
                {
                  name: '客观题得分率',
                  type: 'gauge',
                  center: ['72%', '50%'],    // 默认全局居中
                  radius: '50%',
                  min: 0,
                  max: 1,
                  startAngle: 135,
                  endAngle: 45,
                  axisLine: {            // 坐标轴线
                    lineStyle: {       // 属性lineStyle控制线条样式
                      width: 8,
                      color: [[0.2, '#7266BA'], [0.8, '#02C9B6'], [1, '#FE9226']],
                    }
                  },
                  axisTick: {            // 坐标轴小标记
                    splitNumber: 5,
                    length: 10,        // 属性length控制线长
                    lineStyle: {        // 属性lineStyle控制线条样式
                      color: 'auto'
                    }
                  },
                  axisLabel: {
                    formatter:function(v){
                      switch (v + '') {
                        case '0' : return '';
                        case '1' : return '客观题得分率';
                        case '2' : return '';
                      }
                    }
                  },
                  splitLine: {           // 分隔线
                    length: 15,         // 属性length控制线长
                    lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                      color: 'auto'
                    }
                  },
                  pointer: {
                    width:2
                  },
                  title : {
                    show: false
                  },
                  detail : {
                    show: false
                  },
                  data:[{value: resp.data.objectiveItemScoreRate, name: '客观题得分率'}]
                },
                {
                  name: '主观题得分率',
                  type: 'gauge',
                  center : ['72%', '50%'],    // 默认全局居中
                  radius : '50%',
                  min: 0,
                  max: 1,
                  startAngle: 315,
                  endAngle: 225,
                  axisLine: {            // 坐标轴线
                    lineStyle: {       // 属性lineStyle控制线条样式
                      width: 8,
                      color: [[0.2, '#7266BA'], [0.8, '#02C9B6'], [1, '#FE9226']],
                    }
                  },
                  axisTick: {            // 坐标轴小标记
                    show: false
                  },
                  axisLabel: {
                    formatter:function(v){
                      switch (v + '') {
                        case '0' : return '';
                        case '1' : return '主观题得分率';
                        case '2' : return '';
                      }
                    }
                  },
                  splitLine: {           // 分隔线
                    length: 15,         // 属性length控制线长
                    lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                      color: 'auto'
                    }
                  },
                  pointer: {
                    width:2
                  },
                  title: {
                    show: false
                  },
                  detail: {
                    show: false
                  },
                  data:[{value: resp.data.subjectScoreRate, name: '主观题得分率'}]
                }
              ]
            };
            this.sortChart = this.$echarts.init(this.$refs.statisticSortEl);
            this.sortChart.setOption(option,true);
            window.onresize = () => {
              this.sortChart.resize();
            }
          });
      },
    },
  }
</script>

<style scoped>
  .card-title-span{
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 16px;
    padding: 0 20px;
    height: 54px;
    background-color: #F6F6F6;
    border: 1px solid #EBEBEB;
    border-bottom: 0px;
  }

  .statistic{
    margin-top: 10px;
    height: 300px;
  }
  .statistic .left{
    height: 100%;
    width: calc(50% - 5px);
    margin-right: 5px;
    float: left;
  }
  .statistic .right{
    height: 100%;
    width: calc(50% - 5px);
    margin-left: 5px;
    float: left;
  }
  .title{
    font-size: larger;
    line-height: 30px;
    margin-left: 5px;
  }
  .buttons{
    margin-bottom: 10px;
    float: right;
    align-items: right;
  }

  .buttons > div{
    display: inline-block;
    margin: 0 5px;
  }

  .pagination {
    margin-top: 10px;
  }
</style>

