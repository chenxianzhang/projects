<template>
    <el-form class="login-form" autoComplete="on" :model="loginForm" :rules="loginRules" ref="loginForm" label-position="left">
        <div class="role-list">
            <div class="role-item" v-for="item in roleList" :key="item.id" :class="item.value === selectRoleValue ? 'select' : ''" @click="handleRoleSelect(item)">
                <i :class="'role-icon-' + item.value"></i>
                <span>{{ item.name }}</span>
            </div>
        </div>
        <el-form-item prop="username">
            <el-input name="username" type="text" v-model="loginForm.username" autoComplete="on" placeholder="请输入用户名">
                <i slot="prefix" class="icon icon-user-name"></i>
            </el-input>
        </el-form-item>
        <el-form-item prop="password">
            <el-input name="password" :type="passwordType" v-model="loginForm.password" autoComplete="on" placeholder="请输入密码">
                <i slot="prefix" class="icon icon-user-pwd"></i>
                <i slot="suffix" class="el-input__icon el-icon-view pwd-eye" @click.stop="showPwd"></i>
            </el-input>
        </el-form-item>
        <el-form-item prop="verificationCode">
            <el-input name="verificationCode" type="text" v-model="loginForm.verificationCode" @keyup.enter.native="handleLogin" autoComplete="on" placeholder="请输入验证码">
                <i slot="prefix" class="icon icon-user-pwd"></i>
                <img slot="suffix" class="verification-code-img" alt="验证码" :src="verCodeImageUrl" @click="generateVerCode" />
            </el-input>
        </el-form-item>
        <div class="login-foot">
            <el-button type="text">忘记密码？</el-button>
            <el-button type="text" v-if="selectRoleValue === 'teacher'" @click="registery">立即注册！</el-button>
        </div>
        <el-button type="primary" style="width:100%;margin-bottom:30px;" :loading="loading" @click.native.prevent="handleLogin">登录</el-button>
    </el-form>
</template>

<script>
import { isvalidUsername } from '@/utils/validate'
import { getVerCodeImageUrl, getRoleList } from '@/api/login'

export default {
  name: 'login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!isvalidUsername(value)) {
        callback(new Error('请输入正确的用户名称'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码长度不能少于6个字符'))
      } else {
        callback()
      }
    }
    const validateCode = (rule, value, callback) => {
      if (value === '' || value === 'undefined' || value === null) {
        callback(new Error('验证码不能为空'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: '',
        password: '',
        verificationCode: '',
        role: ''
      },
      loginRules: {
        username: [
          { required: true, trigger: 'blur', validator: validateUsername }
        ],
        password: [
          { required: true, trigger: 'blur', validator: validatePassword }
        ],
        verificationCode: [
          { required: true, trigger: 'blur', validator: validateCode }
        ]
      },
      passwordType: 'password',
      loading: false,
      verCodeImageUrl: '',
      roleList: [],
      selectRoleValue: 'student'
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
    },
    handleLogin() {
      let self = this
      if (this.loginForm.role === '') {
        self.$message({
          showClose: true,
          type: 'warning',
          message: '用户角色不能为空'
        })
        return
      }
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          self.loading = true
          self.$store
            .dispatch('loginProcess', self.loginForm)
            .then(data => {
              self.loading = false
              if (!data.hasLogin || data.hasLogin === 0) {
                self.$router.push('/login/resetpwd')
              } else {
                self.$router.push('/')
              }
            })
            .catch(err => {
              self.loading = false
              self.$message({
                showClose: true,
                type: 'error',
                message: err
              })
              self.generateVerCode()
            })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    generateVerCode() {
      this.verCodeImageUrl = getVerCodeImageUrl()
      this.loginForm.verificationCode = ''
    },
    getRoleList() {
      getRoleList().then(response => {
        if (response.status == 1) {
          this.roleList = response.data
        } else {
          self.$message({
            showClose: true,
            type: 'error',
            message: response.msg
          })
        }
      })
    },
    handleRoleSelect(roleItem) {
      this.selectRoleValue = roleItem.value
      this.loginForm.role = this.selectRoleValue
    },
    registery() {
      this.$router.push('/login/register')
    }
  },
  created() {
    this.verCodeImageUrl = getVerCodeImageUrl()
    this.getRoleList()
    if (
      this.$route.params &&
      this.$route.params.from &&
      this.$route.params.from === 'register'
    ) {
      this.loginForm.role = this.$route.params.role
      this.loginForm.username = this.$route.params.serialNo
      this.loginForm.password = this.$route.params.pwd
    } else {
      this.loginForm.role = this.selectRoleValue;
    }
  },
  destroyed() {}
}
</script>

<style lang="scss">
.login-form {
  .el-input__suffix {
    padding: 5px 0;
  }
  .el-input__inner {
    height: 50px !important;
  }
  .el-step {
    .el-step__arrow {
      display: none;
    }
  }
  .el-button--primary {
    padding: 8px 30px;
    background-color: #009788 !important;
    border: 1px solid #009788;
    color: #fff !important;
    &:hover {
      background-color: #008B7D !important;
      color: #fff !important;
    }
  }
}
</style>


<style lang="scss" scoped>
.login-form {
  background-color: #fff;
  box-shadow: 0px 0px 12px 1px grey;
  padding: 20px;
  .verification-code-img {
    height: 100%;
  }
  .role-list {
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: #000;
    margin-bottom: 10px;
    border-bottom: 1px solid #ccc;
    .role-icon-teacher {
      width: 22px;
      height: 20px;
      display: inline-block;
      background: url('../../../static/img/login/role-icon-teacher.png')
        no-repeat;
      background-size: 100% 100%;
    }
    .role-icon-student {
      width: 22px;
      height: 20px;
      display: inline-block;
      background: url('../../../static/img/login/role-icon-student.png')
        no-repeat;
      background-size: 100% 100%;
    }
    .role-icon-admin {
      width: 22px;
      height: 20px;
      display: inline-block;
      background: url('../../../static/img/login/role-icon-admin.png')
        no-repeat;
      background-size: 100% 100%;
    }
    .role-item {
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      height: 50px;
      width: 20%;
      &:hover {
        color: #777;
      }
      i {
        margin-right: 8px;
      }
    }
    .select {
      border-bottom: 2px solid #00574f;
    }
  }
  .login-foot {
    display: flex;
    justify-content: space-between;
  }
}
</style>

