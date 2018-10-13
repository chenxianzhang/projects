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
  </div>
</template>

<script>
import userTable from '../components/userTable'
import { getCourseData, deleteCourse } from '@/api/manager'

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
      delCourseId: ''
    }
  },
  mounted() {
    this.getCourseData()
  },
  methods: {
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
          this.uEditDlgTitle = '成绩信息'
          this.user = data.value
          this.uEditDlgVisible = true
          this.userOperStatus = 'edit'
          break
        case 'deleteCourse':
          this.delDlgVisible = true
          this.delCourseId = data.value.courseId
          break
      }
    },
    handleDelDlgClose() {
      this.delDlgVisible = false
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
