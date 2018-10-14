<template>
  <div class="course-container">

    <drag-dialog :title="delDlgTitle" width="36%" :dialogVisible="delDlgVisible" @close="handleDelDlgClose" @confirm="handleDelDlgConfirm">
      <div class="del-container">
        <div class="tipinfo">
          <span>删除课程的同时会删除相关的所有成绩信息，请输入二级密码进行确认</span>
        </div>
        <div class="level-pwd-input">
          <el-input v-model="levelPwd" type="password" placeholder="请输入二级密码" :style="{width: '60%'}"></el-input>
        </div>
      </div>
    </drag-dialog>

    <div class="operation">
      <div class="right">
        <el-input placeholder="输入课程名称或任课教师" v-model="courseOrTeacher" @input="queryChange">
          <el-button slot="append" icon="el-icon-search"></el-button>
        </el-input>
      </div>
    </div>

    <user-table :data="courseList" :role="role" :currPage="currPage" :pageSize="pageSize" :totalCount="totalCount" @handlePage="handlePage" @handleCommond="handleCommond"></user-table>

    <drag-dialog title="课程成绩" width="80%" :dialogVisible="viewCourseGradeDlg" :hiddenOperator="true" @close="handleDelDlgClose">
      <el-table :data="tableData" style="width: 100%" border height="80%"
                :header-cell-style="{background:'rgba(28, 77, 125, 0.8)', color:'white', fontWeight:'bold'}" >
        <el-table-column v-for="(task,index) in columns"
                         :label="task.label"
                         :prop="task.prop"
                         align="center"
                         :key="index">
          <template slot-scope="scope">
            <!--<div v-if="task.type && task.type==='link'"-->
                 <!--@click.stop="viewScoreDetail(task.taskId, scope.row.studentNo)"-->
                 <!--style="cursor: pointer; color: #ee9900;">-->
              <!--{{scope.row[task.prop]}}-->
            <!--</div>-->
            <div>{{scope.row[task.prop]}}</div>
          </template>
        </el-table-column>
      </el-table>
    </drag-dialog>
  </div>
</template>

<script>
import userTable from '../components/userTable'
import { getCourseData, deleteCourse } from '@/api/manager'
import { getAllGradeInfo } from '@/api/grade'

export default {
  name: 'courseMgr',
  components: {
    userTable
  },
  data() {
    return {
      role: 'admin',
      courseList: [],
      currPage: 1,
      pageSize: 10,
      totalCount: 0,
      courseOrTeacher: '',
      delDlgTitle: '删除确认',
      delDlgVisible: false,
      levelPwd: '',
      delCourseId: '',

      viewCourseGradeDlg:false,
      columns:[],
      tableData:[],
    }
  },
  mounted() {
    this.getCourseData()
  },
  methods: {
    getGradeByCourse(courseId) {
      let self = this;
      getAllGradeInfo({courseId: courseId, pageSize: 100, currPage: 1})
        .then(resp => {
          if (resp.status === 0) {
            self.$message.warning('获取成绩信息失败：' + resp.msg);
            return;
          }

          self.transferData(resp.data.pageData);
          this.viewCourseGradeDlg = true;
          //todo 动态计算表格高度
        });
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

        data[item].studentTaskInfos.forEach(task => {
          tableItem[task.taskId + "_score"] = task.score;
          if (!task.score && task.score !== 0) {
            debugger

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
    queryChange() {
      this.currPage = 1
      this.pageSize = 10
      this.getCourseData()
    },
    handlePage(data) {
      switch (data.type) {
        case 'PAGE_SIZE_CHANGE':
          this.pageSize = data.value
          break
        case 'PAGE_INDEX_CHANGE':
          this.currPage = data.value
          break
      }
      this.getCourseData()
    },
    getCourseData() {
      getCourseData({
        currPage: this.currPage,
        pageSize: this.pageSize,
        courseOrTeacher: this.courseOrTeacher
      })
        .then(data => {
          if (data.status == 0) {
            this.$msg.error(data.msg)
            return
          }
          this.courseList = data.data.pageData
          this.totalCount = data.data.totalCount
        })
        .catch(err => {
          this.$msg.error(err)
        })
    },
    handleCommond(data) {
      switch (data.commond) {
        case 'viewGrade':
          debugger
          this.getGradeByCourse(data.value.courseId);
          break
        case 'deleteCourse':
          this.delDlgVisible = true
          this.delCourseId = data.value.courseId
          break
      }
    },
    handleDelDlgClose() {
      this.delDlgVisible = false;
      this.viewCourseGradeDlg = false;
    },
    handleDelDlgConfirm() {
      if (!this.levelPwd || this.levelPwd.trim() === '') {
        this.$msg.warning('二级密码不能为空')
        return
      }
      deleteCourse({
        courseId: this.delCourseId,
        levelPwd: this.levelPwd,
        currUserSerialNo: this.$store.state.user.token
      })
        .then(data => {
          if (data.status === 0) {
            this.$msg.error(data.msg)
            return
          }
          this.delDlgVisible = false
          this.levelPwd = ''
          this.$msg.success('删除课程信息成功')
          this.getCourseData()
        })
        .catch(err => {
          this.$msg.error(err)
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.course-container {
  .operation {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    border-bottom: 1px solid gray;
    padding-bottom: 5px;
  }
}

.del-container {
  .tipinfo {
    text-align: center;
  }
  .level-pwd-input {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}
</style>
