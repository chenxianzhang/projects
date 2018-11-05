<template>
    <div :style="{height: containerHeight}">
      <el-row class="statistic-all-grade">
        <el-col :span="24" class="grade-item">
          <div class="card-title-span">
            <span style="display: flex; align-items: center; justify-content: center;">
              <div style="height: 22px; width: 4px; background-color: #009687; margin:0 4px"></div>
              <div style="height: 22px; line-height: 22px; margin:0 4px">成绩统计</div>
              <div style="height: 22px; line-height: 22px; color: #ADADAD; margin:0 4px">GRADE STATISTIC</div>
            </span>
            <span>
              <el-button v-show="!showBackBtn" size="small" style="height: 30px; line-height: 5px; cursor: pointer;" @click.stop="handleSortFunc('asc')">升序</el-button>
              <el-button v-show="!showBackBtn" size="small" style="height: 30px; line-height: 5px; cursor: pointer;" @click.stop="handleSortFunc('desc')">降序</el-button>
              <el-button v-show="showBackBtn" size="small" style="height: 30px; line-height: 5px; cursor: pointer;" @click.stop="back2AllGradeStatistic">返回</el-button>
            </span>
          </div>
          <div ref="grade_static" class="grade-item">
            所有成绩统计
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="statistic-all-grade-bottom">
        <el-col :span="12" class="statistic-task-finish">
          <div class="card-title-span">
            <span style="display: flex; align-items: center; justify-content: center;">
              <div style="height: 20px; width: 35px; border-radius: 10px; background-color: #00C8B5; margin-right: 10px;"></div>
              <span style=" margin-right: 15px;">提交次数</span>
              <div style="height: 20px; width: 35px; border-radius: 10px; background-color: #008276; margin-right: 10px;"></div>
              <span>审批次数</span>
            </span>
          </div>
          <div ref="task_static" class="grade-item">
            任务完成情况统计
          </div>
        </el-col>
        <el-col :span="12" class="statistic-task-finish">
          <div class="card-title-span">
            <span style="display: flex; align-items: center; justify-content: center;">
              <div style="height: 22px; width: 4px; background-color: #009687; margin:0 4px"></div>
              <div style="height: 22px; line-height: 22px; margin:0 4px">题型占比统计</div>
              <div style="height: 22px; line-height: 22px; color: #ADADAD; margin:0 4px">ITEM TYPE PROPORTION STATISTIC</div>
          </span>
          </div>
          <div ref="stem_static" class="grade-item">
            题型数目统计
          </div>
        </el-col>
      </el-row>
    </div>
</template>

<script>
  import {statAllGrade, statTaskSubmitReview, statQuestionTypeCnt, statStudentTaskScore} from '@/api/statistics'

    export default {
        name: "courseStatistic",
      data(){
          return{
            sortType:'desc',
            containerHeight:0,
            scoreStaticChart:null,
            taskStaticChart:null,
            stemStaticChart:null,
            showBackBtn:false,
          }
      },
      mounted(){
          let self = this;
          this.$nextTick(()=>{
            this.containerHeight = document.getElementsByClassName('container')[0].clientHeight - 94 + 'px';
            setTimeout(()=>{
              this.lineGroup();
              this.taskFinishStatus();
              this.stemStatistic();
            },500);
          });
          window.onresize = ()=>{
            setTimeout(()=>{
              self.scoreStaticChart && self.scoreStaticChart.resize();
              self.taskStaticChart && self.taskStaticChart.resize();
              self.stemStaticChart && self.stemStaticChart.resize();
            }, 100);
          }
      },
      destroyed(){
          this.scoreStaticChart = null;
          this.taskStaticChart = null;
          this.stemStaticChart = null;
      },
      methods:{
        /**
         * 所有成绩统计
         * */
        lineGroup() {
          let self = this;
          let params = {
            courseId: this.variables.courseId,
            sort: this.sortType
          };

          let xData = [], yData = [];
          statAllGrade(params).then(resp => {
            if (resp.status === 0) {
              console.log(resp.msg);
              this.$message.warning('获取全部成绩统计结果失败');
              return;
            }

            this.showBackBtn = false;
            let maxValue=0,minValue=0,avgValue=0,cnt=0,totalValue=0;
            if(resp.data.studentScoreList && resp.data.studentScoreList.length !== 0){
              minValue=maxValue=avgValue=resp.data.studentScoreList[0].totalScore;
              resp.data.studentScoreList.forEach((item)=>{
                xData.push(item.studentName);
                yData.push(item.totalScore);
                cnt++;
                totalValue += item.totalScore;
                maxValue = maxValue >= item.totalScore ? maxValue : item.totalScore;
                minValue = minValue <= item.totalScore ? minValue : item.totalScore;
              });
              avgValue = avgValue / cnt;
            }
            let option = {
              barMaxWidth:60,
              tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                  type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
              },
              grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
              },
              xAxis: [
                {
                  name:'姓名',
                  type: 'category',
                  data: xData
                }
              ],
              yAxis: [
                {
                  name:'成绩',
                  type: 'value',
                  min:0,
                  max:100
                }
              ],
              dataZoom: [
                {
                  type: 'inside',
                  start: 0,
                  end: 100
                }
              ],
              series: [
                {
                  name: '总分',
                  type: 'bar',
                  data: yData,
                  itemStyle: {
                    normal: {
                      color: function(params) {
                        var colorList = ['#7266BA', '#01C8B5', '#FE9226'];
                        return colorList[params.dataIndex % 3]
                      }
                    },
                  },
                  markLine: {
                    silent: true,
                    lineStyle:{
                      type:'solid'
                    },
                    data: [
                      {
                        type : 'max',
                        name: '最大值',
                        itemStyle:{
                          color:'#FFC892'
                        },
                        label:{
                          formatter: maxValue
                        }
                      },
                      {
                        type : 'min',
                        name: '最小值',
                        itemStyle:{
                          color:'#B9B2DC'
                        },
                        label:{
                          formatter: minValue
                        }
                      },
                      {
                        type : 'average',
                        name: '平均值',
                        itemStyle:{
                          color:'#7FE3D9'
                        },
                        label:{
                          formatter: avgValue
                        }
                      }
                    ]
                  }
                }
              ]
            };
            if(this.scoreStaticChart === null){
              this.scoreStaticChart = this.$echarts.init(this.$refs.grade_static);
            }
            this.scoreStaticChart.clear();
            this.scoreStaticChart.setOption(option);
            this.scoreStaticChart.on('dblclick', (params) => {
              let studentNo = resp.data.studentScoreList[params.dataIndex].studentNo;
              self.go2StudentGradeStatistic(studentNo);
            });
          });
        },
        /**
         * handleSortFunc
         * */
        handleSortFunc(sortType){
          this.sortType = sortType;
          this.lineGroup();
        },
        /**
         * 任务完成情况/评阅情况统计
         * */
        taskFinishStatus(){
          let xData = [], yData=[];
          statTaskSubmitReview({courseId: this.variables.courseId}).then(resp => {
              if (resp.status === 0) {
                console.log(resp.msg);
                this.$message.warning('获取任务完成情况失败！');
                return;
              }
              if(resp.data && resp.data.length !== 0){
                resp.data.forEach((item)=>{
                  xData.push(item.taskName);
                  yData.push(item.submitCnt);
                  yData.push(item.finishedCnt);
                });
              }
            let option = {
              barMaxWidth:60,
              color: ['#00C8B5', '#008276'],
              tooltip: {
                trigger: 'axis',
                axisPointer: {
                  type: 'shadow'
                },
                formatter: function (a,b,c) {
                  if(a[0].dataIndex % 2 === 0){
                    return `<span>提交次数：` + a[0].value + `次</span>`;
                  }
                  return `<span>审核次数：` + a[0].value + `次</span>`;
                }
              },
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
                  name:'次数'
                }
              ],
              series: [
                {
                  name: '次数',
                  type: 'line',
                  barGap: 0,
                  data: yData,
                  symbol:'circle',
                  symbolSize:10,
                  itemStyle: {
                    normal: {
                      color: function(params) {
                        var colorList = ['#00C8B5', '#008276'];
                        return colorList[params.dataIndex % 2]
                      },
                      lineStyle:{
                        color:'#00C8B5'
                      }
                    },
                  },
                }
              ]
            };
            this.taskStaticChart = this.$echarts.init(this.$refs.task_static)
            this.taskStaticChart.setOption(option);
          });
        },
        /**
         * 题型统计
         * */
        stemStatistic(){
          let data = [];
          statQuestionTypeCnt({courseId: this.variables.courseId}).then(resp => {
            if (resp.status === 0) {
              console.log(resp.msg);
              this.$message.warning('获取题型统计情况失败！');
              return;
            }
            if(resp.data && resp.data.length !== 0){
              resp.data.forEach((item)=>{
                data.push({value:item.count, name:item.getQuestionTypeText});
              });
            }
            let option = {
              tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
              },
              series : [
                {
                  name: '题型统计',
                  type: 'pie',
                  radius : '55%',
                  center: ['50%', '60%'],
                  data:data,
                  itemStyle: {
                    emphasis: {
                      shadowBlur: 10,
                      shadowOffsetX: 0,
                      shadowColor: 'rgba(0, 0, 0, 0.5)'
                    },
                    normal:{
                      color: function(params) {
                        var colorList = ['#FE9226', '#7266BA', '#01C8B5'];
                        return colorList[params.dataIndex % 3]
                      },
                    }
                  }
                }
              ]
            };
            this.stemStaticChart = this.$echarts.init(this.$refs.stem_static);
            this.stemStaticChart.setOption(option);
          });
        },
        /**
         * back2AllGradeStatistic 返回所有成绩统计
         * */
        back2AllGradeStatistic(){
          this.lineGroup();
        },
        /**
         * go2StudentGradeStatistic 下钻到学生个人成绩统计
         * */
        go2StudentGradeStatistic(sNo){
          let xData = [], score=[], totalScore=[];
          statStudentTaskScore({courseId: this.variables.courseId, studentNo: sNo})
            .then(resp => {
              if (resp.status === 0) {
                console.log(resp.msg);
                this.$message.warning('获取任务完成情况失败！');
                return;
              }
              this.showBackBtn = true;
              this.scoreStaticChart.clear();

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
                legend: {
                  data: ['得分', '总分']
                },
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
                    itemStyle: {
                      normal: {
                        color:'#FE9226'
                      },
                    },
                  },
                  {
                    name: '总分',
                    type: 'bar',
                    data: totalScore,
                    itemStyle: {
                      normal: {
                        color:'#7266BA'
                      },
                    },
                  }
                ]
              };
              this.scoreStaticChart.setOption(option);
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
  .statistic-all-grade{
    height: calc(50% - 10px);
  }

  .grade-item{
    height: calc(100% - 54px);
    width: 100%;
    background-color: white;
    border: 1px solid #EBEBEB;
  }


  .statistic-all-grade-bottom{
    margin-top: 20px;
    height: calc(50% - 10px);
  }

  .statistic-task-finish{
    height: 100%;
  }
  .container-scrollbar{
    background-color: #F3F4F8;
  }
</style>
