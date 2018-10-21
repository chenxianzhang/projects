<template>
  <div class="login-container">
    <div class="content">
      <div class="title">
        <div class="logo">
          <img src="../../assets/logo.png" />
        </div>
        <p class="zh">
          {{ titleZh }}
        </p>
        <p class="en">
          {{ titleEn }}
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
      titleEn: 'RESEARCH COURSE MANAGEMENT PROCESS ASSESSMENT SYSTEM LOGIN'
    }
  }
}
</script>

<style lang="scss">
.login-container {
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
    width: 460px;
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
      }
      .en {
        font-size: 12px;
        text-align: center;
      }
    }
  }
}
</style>
