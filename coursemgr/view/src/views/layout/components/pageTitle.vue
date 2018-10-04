<template>
    <div class="title">
        <span>课程管理系统</span>
        <div class="title-operator">
            当前用户：
            <el-dropdown @command="handleCommand">
                <span class="el-dropdown-link">
                    {{userInfo.name}}
                    <i class="el-icon-arrow-down el-icon--right white-color"></i>
                </span>
                <el-dropdown-menu slot="dropdown" class="el-menu-vertical-demo">
                    <el-dropdown-item command="backHome">返回首页</el-dropdown-item>
                    <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>
<script>
import { findUser } from '@/api/login'

export default {
  name: 'title',
  data() {
    return {
      userInfo: {}
    }
  },
  mounted() {
    //获取用户信息
    let self = this
    findUser(this.$store.state.user.token).then(response => {
      if (response.status === 0) {
        self.$message({
          showClose: true,
          type: 'error',
          message: response.msg
        })
      }
      self.userInfo = response.data
    })
  },
  methods: {
    logout() {
      this.$store.dispatch('logOut').then(() => {
          // 清空tagsview
          this.$store.dispatch('delAllViews')
        //   this.$router.push('/login')
        location.reload()
      })
    },
    handleCommand(cmd) {
      switch (cmd) {
        case 'logout':
          this.logout()
          break
        case 'backHome':
          this.$router.push('/home')
          break
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.title {
  height: 50px;
  width: 100%;
  background-color: #456;
  line-height: 50px;
  color: white;
  padding-left: 20px;
  .title-operator {
    float: right;
    margin-right: 50px;
    font-size: 14px;
    .el-dropdown-link {
      color: #fff;
      cursor: pointer;
      .white-color {
        cursor: pointer;
      }
    }
  }
}
</style>
