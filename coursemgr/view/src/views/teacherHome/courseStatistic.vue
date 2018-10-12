<template>
    <div :style="{height: containerHeight}">
      <el-row class="statistic-all-grade">
        <el-col :span="24" class="grade-item">
          <div ref="grade_static" class="grade-item">
            所有成绩统计
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="statistic-all-grade-bottom">
        <el-col :span="12" class="statistic-task-finish">
          <div ref="task_static" class="grade-item">
            任务完成情况统计
          </div>
        </el-col>
        <el-col :span="12" class="statistic-task-finish">
          <div ref="stem_static" class="grade-item">
            题型数目统计
          </div>
        </el-col>
      </el-row>
    </div>
</template>

<script>
  import {statAllGrade, statTaskSubmitReview, statQuestionTypeCnt} from '@/api/statistics'

    export default {
        name: "courseStatistic",
      data(){
          return{
            containerHeight:0,
            scoreStaticChart:null,
            taskStaticChart:null,
            stemStaticChart:null
          }
      },
      mounted(){
          this.$nextTick(()=>{
            this.containerHeight = document.getElementsByClassName('container')[0].clientHeight - 74 + 'px';
            setTimeout(()=>{
              this.lineGroup();
              this.taskFinishStatus();
              this.stemStatistic();
            },500);
          });
          window.onresize = ()=>{
            setTimeout(()=>{
              this.scoreStaticChart.resize();
              this.taskStaticChart.resize();
              this.stemStaticChart.resize();
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
          let params = {
            courseId: this.variables.courseId,
            sort: 'desc'
          };

          let xData = [], yData = [];
          statAllGrade(params).then(resp => {
            if (resp.status === 0) {
              console.log(resp.msg);
              this.$message.warning('获取全部成绩统计结果失败');
              return;
            }
            if(resp.data.studentScoreList && resp.data.studentScoreList.length !== 0){
              resp.data.studentScoreList.forEach((item)=>{
                xData.push(item.studentName);
                yData.push(item.totalScore);
              });
            }
            let option = {
              title : {
                text: '成绩统计',
                // subtext: '纯属虚构',
                x:'center'
              },
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
                  data: yData
                }
              ]
            };
            this.scoreStaticChart = this.$echarts.init(this.$refs.grade_static);
            this.scoreStaticChart.setOption(option);
            // window.onresize = ()=>{
            //   this.scoreStaticChart.resize();
            // }
          });
        },
        /**
         * 任务完成情况/评阅情况统计
         * */
        taskFinishStatus(){
          let xData = [], finishData=[], submitData=[];
          statTaskSubmitReview({courseId: this.variables.courseId}).then(resp => {
              if (resp.status === 0) {
                console.log(resp.msg);
                this.$message.warning('获取任务完成情况失败！');
                return;
              }
              if(resp.data && resp.data.length !== 0){
                resp.data.forEach((item)=>{
                  xData.push(item.taskName);
                  finishData.push(item.finishedCnt);
                  submitData.push(item.submitCnt);
                });
              }
            let labelOption = {
              normal: {
                show: true,
                formatter: '{c}  {name|{a}}',
                fontSize: 12,
                rich: {
                  name: {
                    textBorderColor: '#fff'
                  }
                }
              }
            };
            let option = {
              color: ['#003366', '#006699', '#4cabce', '#e5323e'],
              tooltip: {
                trigger: 'axis',
                axisPointer: {
                  type: 'shadow'
                }
              },
              legend: {
                data: ['提交次数', '审批次数']
              },
              // toolbox: {
              //   show: true,
              //   orient: 'vertical',
              //   left: 'right',
              //   top: 'center',
              //   feature: {
              //     mark: {show: true},
              //     dataView: {show: true, readOnly: false},
              //     magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
              //     restore: {show: true},
              //     saveAsImage: {show: true}
              //   }
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
                  name:'次数'
                }
              ],
              series: [
                {
                  name: '提交次数',
                  type: 'bar',
                  barGap: 0,
                  label: labelOption,
                  data: submitData
                },
                {
                  name: '审批次数',
                  type: 'bar',
                  label: labelOption,
                  data: finishData
                }
              ]
            };
            this.taskStaticChart = this.$echarts.init(this.$refs.task_static)
            this.taskStaticChart.setOption(option);
            // window.onresize = ()=>{
            //   this.taskStaticChart.resize();
            // }
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
            debugger
            if(resp.data && resp.data.length !== 0){
              resp.data.forEach((item)=>{
                data.push({value:item.count, name:item.getQuestionTypeText});
              });
            }
            let option = {
              title : {
                text: '题型占比统计',
                // subtext: '纯属虚构',
                x:'center'
              },
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
                    }
                  }
                }
              ]
            };
            this.stemStaticChart = this.$echarts.init(this.$refs.stem_static);
            this.stemStaticChart.setOption(option);
            // window.onresize = ()=>{
            //   this.stemStaticChart.resize();
            // }
          });
        },
      }
    }
</script>

<style scoped>
  .statistic-all-grade{
    height: calc(50% - 10px);
  }

  .grade-item{
    height: 100%;
    width: 100%;
    box-shadow: 0px 0px 2px 1px gray;
  }

  .statistic-all-grade-bottom{
    margin-top: 20px;
    height: calc(50% - 10px);
  }

  .statistic-task-finish{
    height: 100%;
  }
</style>
