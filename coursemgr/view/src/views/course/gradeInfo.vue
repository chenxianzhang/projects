<template>
  <div>
    <h4 style="font-family: cursive; font-size: larger;">当前课程：{{courseName}}</h4>
    <template>
    <el-table :data="tableData" style="width: 100%" border
              :header-cell-style="{background:'rgba(28, 77, 125, 0.8)', color:'white', fontWeight:'bold'}" >
      <el-table-column v-for="(task,index) in columns"
                       :label="task.label"
                       :prop="task.prop"
                       align="center"
                       :key="index">
        <template slot-scope="scope">
          <div v-if="task.type && task.type==='link'"
               @click.stop="viewScoreDetail(task.taskId, scope.row.studentNo)"
               style="cursor: pointer; color: #ee9900;">
            {{scope.row[task.prop]}}
          </div>
          <div v-else>{{scope.row[task.prop]}}</div>
        </template>
      </el-table-column>
    </el-table>
    </template>
    <div class="pagination" v-if="!isStudent">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currPage"
        :page-sizes="[10, 15, 20, 30]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalCount">
      </el-pagination>
    </div>

    <div class="buttons">
      <el-button class="primary" @click="download()">导出为excel</el-button>
      <el-button class="primary" @click="exportZip">过程性打包</el-button>
    </div>
    <!--查看当前任务答题情况-->
    <el-dialog v-if="showAnswer" :visible.sync="showAnswer" width="1240px" title="成绩信息">
      <grade-detail :taskId="taskSelect.taskId" :uId="taskSelect.uId"></grade-detail>
    </el-dialog>
  </div>
</template>

<script>
  import gradeDetail from '../../components/gradeDetail'
  import { getAllGradeInfo, getStuGradeInfo, download, exportZip } from '../../api/grade'
  import { getCourseById } from "../../api/course";

  export default {
      name: "gradeInfo",
      components:{gradeDetail},
      data(){
        return {
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
        let cId = this.$route.params.courseId;
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
            });
          return;
        }
        //获取当前课程所有学生的成绩
        this.getGradeByCourse(cId);
      },
      methods: {
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

            data[item].studentTaskInfos.forEach(task => {
              tableItem[task.taskId + "_score"] = task.score;
              if (!task.score) {
                tableItem[task.taskId + "_score"] = task.status;
              }
              if (!hasInitCol) {
                this.columns.push({prop:task.taskId + "_score",
                 label: task.taskName + "(" + task.taskWeight + "%)", type:'link', taskId: task.taskId});
              }
            });
            hasInitCol = true;
            tableItem.totalScore = data[item].totalScore;
            this.tableData.push(tableItem);
          }
          this.columns.push({prop:"totalScore", label: "加权总分"});
        },
        handleSizeChange(val) {
           this.pageSize = val;
           this.getGradeByCourse(this.$route.params.courseId);
        },
        handleCurrentChange(val) {
          this.currPage = val;
          this.getGradeByCourse(this.$route.params.courseId);
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
          });
        },
        download() {
          download(this.$route.params.courseId, this.$store.state.user.token, this.isStudent);
        },
        exportZip() {
          exportZip(this.$route.params.courseId, this.$store.state.user.token, this.isStudent);
        },
        viewScoreDetail(taskId, uId){
          this.taskSelect.taskId = taskId;
          this.taskSelect.uId = uId;
          this.showAnswer = true;
        }
      },
    }
</script>

<style scoped>
  .buttons{
    margin-top: 10px;
  }

  .buttons > div{
    display: inline-block;
    margin: 0 5px;
  }

  .pagination {
    margin-top: 10px;
  }
</style>

