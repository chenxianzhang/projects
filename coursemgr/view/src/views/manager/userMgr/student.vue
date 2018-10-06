<template>
  <div class="student-container">
    <user-table 
      :data="userList" 
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
import { getUserByRole } from '@/api/manager'

export default {
  name: 'student',
  data() {
    return {
      role: 'student',
      userList: [],
      nameOrNo: '',
      currPage: 1,
      pageSize: 10,
      totalCount: 0
    }
  },
  components: {
    userTable
  },
  created() {
    this.getUserData()
  },
  mounted() {
   this.$on('query', (nameOrNo) => {
      this.nameOrNo = nameOrNo
      console.log(nameOrNo)
      this.queryAction()
    })
  },
  methods: {
    queryAction() {
      this.getUserData()
    },
    handlePage(data) {
      switch (data.type) {
        case 'PAGE_SIZE_CHANGE':
          this.pageSize = data.value;
          break
        case 'PAGE_INDEX_CHANGE':
          this.currPage = data.value;
          break
      }
      this.getUserData()
    },
    handleCommond(data) {
      switch (data.commond) {
        case 'edit':
          break
        case 'course':
          break
        case 'reset':
          break
        case 'delete':
          break
      }
    },
    getUserData() {
      let params = {
        role: this.role,
        nameOrNo: this.nameOrNo,
        pageSize: this.pageSize,
        currPage: this.currPage
      }

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

    }
  }
}
</script>
<style lang="scss" scoped>
</style>

