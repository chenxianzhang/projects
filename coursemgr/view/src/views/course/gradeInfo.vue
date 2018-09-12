<template>
  <div>
    <h4 style="font-family: cursive; font-size: larger;">当前课程：{{courseName}}</h4>
    <el-table :data="gradeList" style="width: 100%">
      <el-table-column type="selection" width="55" :render-header="renderHeader"> </el-table-column>
      <el-table-column prop="studentName" label="姓名"> </el-table-column>
      <el-table-column prop="studentNo" label="学号"> </el-table-column>
      <el-table-column prop="groupNo" label="所在小组"> </el-table-column>

      <el-table-column v-for="(task,index) in gradeList.studentTaskInfos" :label="task.taskName" :key="index">
        <template slot-scope="scope">
          <span style="cursor: pointer; text-decoration: underline; font-weight: bold;" @click="handleClick(scope.row)">{{ task.score }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="totalScore" label="加权总分"> </el-table-column>
    </el-table>

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
      <el-button class="primary">过程性打包</el-button>
    </div>
    <!--查看当前任务答题情况-->
    <el-dialog :visible.sync="showAnswer">
      <upload-answer></upload-answer>
    </el-dialog>
  </div>
</template>

<script>
  import UploadAnswer from '../teacherHome/uploadAnswer'
  import { getAllGradeInfo, getStuGradeInfo, download } from '../../api/grade'
  import { getCourseById } from "../../api/course";

  export default {
      name: "gradeInfo",
      components:{UploadAnswer},
      data(){
        return {
          showAnswer: false,
          isStudent:false,
          courseName:'',
          gradeList:[],
          totalCount: 0,
          pageSize: 10,
          currPage: 1
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
              self.gradeList.push(resp.data);
            });
          return;
        }
        //获取当前课程所有学生的成绩
        this.getGradeByCourse(cId);
      },
      methods: {
        renderHeader(h,{column, $index}){
          h()
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

            self.gradeList = resp.data.pageData;
            self.totalCount = resp.data.totalCount;
            console.log(self.gradeList);
          });
        },
        handleClick(row) {
          this.showAnswer = true;
          //todo 通过任务id和学生id，获取任务题目信息
          console.log(row);
        },
        download() {
          download(this.$route.params.courseId, this.$store.state.user.token, this.isStudent);
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

