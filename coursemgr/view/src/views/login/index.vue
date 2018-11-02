<template>
  <div class="login-container">
    <div class="content">
      <div class="title">
        <div class="logo">
          <img src="../../assets/logo.png" />
        </div>
        <p class="zh">
          <span v-for="(ch, index) in titleZh" :key="index">{{ ch }}</span>
        </p>
        <p class="en">
          <span v-for="(en, index) in titleEns" :key="index">{{ en }}</span>
        </p>
      </div>
      <transition name="fade-transform" mode="out-in">
        <router-view />
      </transition>
    </div>
  </div>
</template>

<script>
export default {
  name: 'index',
  data() {
    return {
      titleZh: '研究型课程管理过程性考核系统登录',
      titleEn: 'RESEARCH COURSE MANAGEMENT PROCESS ASSESSMENT SYSTEM LOGIN',
      titleEns: []
    }
  },
  created() {
    this.changeTitle(this.$route.path)
  },
  watch: {
    $route(to, from) {
      this.changeTitle(to.path)
    }
  },
  methods: {
    changeTitle(path) {
      if (path.indexOf("logIn") >= 0) {
        this.titleZh = '研究型课程管理过程性考核系统登录'
        this.titleEn = 'RESEARCH COURSE MANAGEMENT PROCESS ASSESSMENT SYSTEM LOGIN'
      } else if (path.indexOf("register") >= 0) {
        this.titleZh = '研究型课程管理过程性考核系统注册'
        this.titleEn = 'REGISTRATION OF RESEARCH COURSE MANAGEMENT PROCESS ASSESSMENT SYSTEM'
      }
      this.titleEns = []
      let arr = this.titleEn.split(' ')
      for (let i = 0;i < arr.length; i++) {
        let tmp = arr[i];
        if (i < arr.length - 1) {
          tmp = tmp + ""
        }
        this.titleEns.push(tmp)
      }
    }
  }
}
</script>

<style lang="scss">
.login-container {
  .el-form {
  .el-form-item {
    margin-bottom: 20px;
  }
}

  .el-input {
    .el-input__inner {
      border: 1px solid #b4bccc;
      height: 40px;
      border-radius: 0;
      &:focus {
        border-color: #009285;
      }
    }
    // .el-form-item__label {
    //   color: #fff;
    // }
    .el-input__prefix {
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
  .icon {
    width: 22px;
    height: 22px;
    left: 2px;
    color: white;
  }
  .icon-user-name{
    background: url("../../../static/img/icon_user_name.png") no-repeat left;
  }
  .icon-user-pwd{
    background: url("../../../static/img/icon_user_pwd.png") no-repeat left;
  }
  .pwd-eye {
    cursor: pointer;
  }
   .el-button--primary{
    height: 40px !important;
    border-radius: 0;
    border: 0;
  }
}
</style>


<style rel="stylesheet/scss" lang="scss" scoped>
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  color: #fff;
  background: url('../../../static/img/login/register_bg.png') no-repeat;
  background-size: 100% 100%;
  .content {
    width: 520px;
    // margin-top: -120px;
    .title {
      margin-bottom: 20px;
      p {
        margin: 0;
      }
      .logo {
        display: flex;
        justify-content: center;
        margin-bottom: 15px;
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
        text-align: center;
        display: flex;
        justify-content: space-between;
      }
    }
  }
}
</style>
