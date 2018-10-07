<template>
  <div class="course-container">
    <user-table
      :data="courseList"
      :role="role"
      :currPage="currPage"
      :pageSize="pageSize"
      :totalCount="totalCount"
      @handlePage="handlePage"
      @handleCommond="handleCommond"></user-table>
  </div>
</template>

<script>
  import userTable from '../components/userTable'
export default {
  name:'courseMgr',
  components: {
    userTable
  },
  data() {
    return {
      role:'admin',
      courseList: [],
      currPage: 1,
      pageSize: 10,
      totalCount: 0
    }
  },
  methods:{
    handlePage(data) {
      switch (data.type) {
        case 'PAGE_SIZE_CHANGE':
          this.pageSize = data.value;
          break
        case 'PAGE_INDEX_CHANGE':
          this.currPage = data.value;
          break
      }
      this.getCourseData()
    },
    handleCommond(data) {
      switch (data.commond) {
        case 'viewGrade':
          this.uEditDlgTitle = '成绩信息'
          this.user = data.value
          this.uEditDlgVisible = true
          this.userOperStatus = 'edit'
          break
        case 'deleteScore':
          // this.getCourseByUser(data.value)
          this.courseVisible = true
          break
      }
    },
    getUserData() {
      let params = {
        role: this.role,
        nameOrNo: this.nameOrNo,
        pageSize: this.pageSize,
        currPage: this.currPage
      };

      getUserByRole(params).then(response => {
        if (response.status === 0) {
          this.$msg.warning(response.msg)
          return
        }
        this.userList = response.data.pageData
        this.totalCount = response.data.totalCount
      }).catch(err => {
        this.$msg.error(err)
      })

    },
  }
}
</script>

<style>

</style>
