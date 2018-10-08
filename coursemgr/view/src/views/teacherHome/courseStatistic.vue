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
    export default {
        name: "courseStatistic",
      data(){
          return{
            containerHeight:0,
          }
      },
      mounted(){
          // setTimeout(()=>{
          //   this.containerHeight = document.getElementsByClassName('container')[0].clientHeight - 74 + 'px';
          // }, 100);
          this.$nextTick(()=>{
            this.containerHeight = document.getElementsByClassName('container')[0].clientHeight - 74 + 'px';
            setTimeout(()=>{
              this.lineGroup();
              this.taskFinishStatus();
              this.stemStatistic();
            },500);

          })
      },
      methods:{
        lineGroup(){
            let option = {
              tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                  type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
              },
              legend: {
                data:['直接访问','邮件营销','联盟广告','视频广告','搜索引擎','百度','谷歌','必应','其他']
              },
              grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
              },
              xAxis : [
                {
                  type : 'category',
                  data : ['周一','周二','周三','周四','周五','周六','周日']
                }
              ],
              yAxis : [
                {
                  type : 'value'
                }
              ],
              series : [
                {
                  name:'直接访问',
                  type:'bar',
                  data:[320, 332, 301, 334, 390, 330, 320]
                },
                {
                  name:'邮件营销',
                  type:'bar',
                  stack: '广告',
                  data:[120, 132, 101, 134, 90, 230, 210]
                },
                {
                  name:'联盟广告',
                  type:'bar',
                  stack: '广告',
                  data:[220, 182, 191, 234, 290, 330, 310]
                },
                {
                  name:'视频广告',
                  type:'bar',
                  stack: '广告',
                  data:[150, 232, 201, 154, 190, 330, 410]
                },
                {
                  name:'搜索引擎',
                  type:'bar',
                  data:[862, 1018, 964, 1026, 1679, 1600, 1570],
                  markLine : {
                    lineStyle: {
                      normal: {
                        type: 'dashed'
                      }
                    },
                    data : [
                      [{type : 'min'}, {type : 'max'}]
                    ]
                  }
                },
                {
                  name:'百度',
                  type:'bar',
                  barWidth : 5,
                  stack: '搜索引擎',
                  data:[620, 732, 701, 734, 1090, 1130, 1120]
                },
                {
                  name:'谷歌',
                  type:'bar',
                  stack: '搜索引擎',
                  data:[120, 132, 101, 134, 290, 230, 220]
                },
                {
                  name:'必应',
                  type:'bar',
                  stack: '搜索引擎',
                  data:[60, 72, 71, 74, 190, 130, 110]
                },
                {
                  name:'其他',
                  type:'bar',
                  stack: '搜索引擎',
                  data:[62, 82, 91, 84, 109, 110, 120]
                }
              ]
            };
            let myChart = this.$echarts.init(this.$refs.grade_static)
            myChart.setOption(option);
          },
        taskFinishStatus(){
          let labelOption = {
            normal: {
              show: true,
              formatter: '{c}  {name|{a}}',
              fontSize: 16,
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
              data: ['Forest', 'Steppe', 'Desert', 'Wetland']
            },
            toolbox: {
              show: true,
              orient: 'vertical',
              left: 'right',
              top: 'center',
              feature: {
                mark: {show: true},
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                restore: {show: true},
                saveAsImage: {show: true}
              }
            },
            calculable: true,
            xAxis: [
              {
                type: 'category',
                axisTick: {show: false},
                data: ['2012', '2013', '2014', '2015', '2016']
              }
            ],
            yAxis: [
              {
                type: 'value'
              }
            ],
            series: [
              {
                name: 'Forest',
                type: 'bar',
                barGap: 0,
                label: labelOption,
                data: [320, 332, 301, 334, 390]
              },
              {
                name: 'Steppe',
                type: 'bar',
                label: labelOption,
                data: [220, 182, 191, 234, 290]
              },
              {
                name: 'Desert',
                type: 'bar',
                label: labelOption,
                data: [150, 232, 201, 154, 190]
              },
              {
                name: 'Wetland',
                type: 'bar',
                label: labelOption,
                data: [98, 77, 101, 99, 40]
              }
            ]
          };
          let myChart = this.$echarts.init(this.$refs.task_static)
          myChart.setOption(option);
        },
        stemStatistic(){
          let option = {
            title : {
              text: '某站点用户访问来源',
              subtext: '纯属虚构',
              x:'center'
            },
            tooltip : {
              trigger: 'item',
              formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
              orient: 'vertical',
              left: 'left',
              data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
            },
            series : [
              {
                name: '访问来源',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                  {value:335, name:'直接访问'},
                  {value:310, name:'邮件营销'},
                  {value:234, name:'联盟广告'},
                  {value:135, name:'视频广告'},
                  {value:1548, name:'搜索引擎'}
                ],
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
          let myChart = this.$echarts.init(this.$refs.stem_static)
          myChart.setOption(option);
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
