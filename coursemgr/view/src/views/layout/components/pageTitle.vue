<template>
  <div class="title">
    <div class="system-logo">
      <i class="logo"></i>
      <!-- <img src="../../../assets/logo.png" /> -->
      <div class="logo-title">
        <p class="zh">
          <span v-for="(ch, index) in titleZh" :key="index">{{ ch }}</span>
        </p>
        <p class="en">
          {{ titleEn }}
        </p>
      </div>
    </div>

    <div class="title-operator">
      <div class="curr-course" v-if="role != 'admin' && !homePage">
        <i class="custom-icon custom-icon-book"></i>
        <span>当前课程：{{ courseName }}</span>
      </div>

      <div class="operator">
        <div class="item" @click="handleCommand('logout')">
          <i class="custom-icon custom-icon-quit"></i>
          <span>退出登录</span>
        </div>
        <div class="item" @click="handleCommand('backHome')">
          <i class="custom-icon custom-icon-home"></i>
          <span>返回首页</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { findUser } from '@/api/login'
import { getCourseById } from '@/api/course'
import { removeCourseId, getLoginRole } from '@/utils/auth'

export default {
  name: 'title',
  data() {
    return {
      userInfo: {},
      courseName: '',
      role: getLoginRole(),
      titleZh: '研究型课程管理过程性考核系统',
      titleEn: 'RESEARCH COURSE MANAGEMENT PROCESS ASSESSMENT SYSTEM'
    }
  },
  props: {
    homePage: {
      type: Boolean,
      default: true
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

    if (this.role != 'admin' && !this.homePage) {
      getCourseById({ courseId: this.variables.courseId }).then(res => {
        if (res.status === 0) {
          this.$msg.error(res.msg)
          return
        }
        this.courseName = res.data.name
      })
    }
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
          removeCourseId()
          this.$router.push('/home')
          break
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.title {
  height: 70px;
  width: 100%;
  background: url('../../../../static/img/title-bg.png');
  color: white;
  padding-left: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  .title-operator {
    margin-right: 20px;
    font-size: 14px;
    height: 100%;
    display: flex;
    align-items: center;
    .curr-course {
      margin-right: 20px;
      display: flex;
      align-items: center;
      i {
        margin-right: 5px;
      }
    }
    .el-dropdown-link {
      color: #fff;
      cursor: pointer;
      .white-color {
        cursor: pointer;
      }
    }
    .operator {
      display: flex;
      height: 100%;
      .item {
        padding: 0 20px;
        height: 100%;
        display: flex;
        align-items: center;
        cursor: pointer;
        i {
          margin-right: 5px;
        }
        &:hover {
          background-color: rgb(0, 87, 79);
        }
      }
    }
  }
  .system-logo {
    display: flex;
    align-items: center;
    .logo {
      width: 75px;
      height: 50px;
      display: inline-block;
      background: url('../../../assets/logo.png') no-repeat;
      background-size: 100% 100%;
      margin-right: 20px;
    }
    .logo-title {
      p {
        margin: 0;
      }
      .zh {
        font-size: 18px;
        font-weight: bold;
        text-align: center;
        display: flex;
        justify-content: space-between;
      }
      .en {
        font-size: 12px;
      }
    }
  }
}
</style>
