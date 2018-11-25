<template>
    <el-form :model="resetpwdData" status-icon :rules="resetpwdRule" ref="resetpwdForm" class="resetpwd-form">
        <el-form-item prop="oldPass">
            <el-input name="password" type="password" v-model="resetpwdData.oldPass" autoComplete="on" placeholder="请输入旧密码...">
                <i slot="prefix" class="icon icon-user-pwd"></i>
            </el-input>
        </el-form-item>
        <el-form-item prop="newPass">
            <el-input name="password" type="password" v-model="resetpwdData.newPass" autoComplete="on" placeholder="请输入新密码...">
                <i slot="prefix" class="icon icon-user-pwd"></i>
            </el-input>
        </el-form-item>
        <el-form-item prop="checkPass">
            <el-input name="password" type="password" v-model="resetpwdData.checkPass" autoComplete="on" placeholder="请输入确认密码...">
                <i slot="prefix" class="icon icon-user-pwd"></i>
            </el-input>
        </el-form-item>
        <div class="back-login">
            <el-button type="text" @click="logout">
                <i class="custom-icon-back"></i>返回登录界面</el-button>
        </div>
        <div class="operator">
            <el-button type="primary" @click="submitForm('resetpwdForm')">提交</el-button>
            <el-button type="primary" @click="resetForm('resetpwdForm')">重置</el-button>
        </div>
    </el-form>
</template>

<script>
import { findUser, update } from '@/api/login'

export default {
  name: 'reset-pwd',
  data() {
    var checkOldPass = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('密码不能为空'))
      }
      if (value.toString() !== this.userInfo.password) {
        return callback(new Error('密码不正确'))
      }
      callback()
    }
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.resetpwdData.checkPass !== '') {
          this.$refs.resetpwdForm.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.resetpwdData.newPass) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }

    return {
      userInfo: {},
      resetpwdData: {
        newPass: '',
        checkPass: '',
        oldPass: ''
      },
      resetpwdRule: {
        newPass: [{ validator: validatePass, trigger: 'blur' }],
        checkPass: [{ validator: validatePass2, trigger: 'blur' }],
        oldPass: [{ validator: checkOldPass, trigger: 'blur' }]
      }
    }
  },
  created() {},
  mounted() {
    findUser(this.$store.state.user.token).then(response => {
      if (response.status === 0) {
        self.$message({
          showClose: true,
          type: 'error',
          message: response.msg
        })
        return
      }
      this.userInfo = response.data
    })
  },
  methods: {
    submitForm(formName) {
      let self = this
      this.$refs[formName].validate(valid => {
        if (valid) {
          self.userInfo.password = self.resetpwdData.newPass
          self.save()
        } else {
          this.$message.warning('请填写正确的信息！')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    logout() {
      this.$store.dispatch('logOut').then(() => {
        // 清空tagsview
        this.$store.dispatch('delAllViews')
        //   this.$router.push('/login')
        location.reload()
      })
    },
    save() {
      let self = this
      update(this.userInfo)
        .then(response => {
          if (response.status === 0) {
            self.$message({
              showClose: true,
              type: 'warning',
              message: response.msg
            })
            return
          }
          self.$router.push('/')
          self.$message({
            showClose: true,
            type: 'success',
            message: '修改成功'
          })
        })
        .catch(err => {
          console.log(err)
          self.$message({
            showClose: true,
            type: 'error',
            message: '保存个人信息异常'
          })
        })
    }
  }
}
</script>

<style lang="scss">
.resetpwd-form {
  .back-login {
    .custom-icon-back {
      margin-right: 5px;
    }
    .el-button--text {
      span {
        display: flex;
        align-items: center;
        color: rgb(0, 150, 136);
        &:hover {
            color: rgba(0, 150, 136, 0.8);
        }
      }
    }
  }
  .el-button--primary {
    width: calc(50% - 8px);
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
.resetpwd-form {
  background-color: #fff;
  box-shadow: 0px 0px 12px 1px grey;
  padding: 60px 60px 30px 60px;
  .back-login {
      margin-top: -16px;
  }
  .operator {
      display: flex;
      justify-content: center;
  }
}
</style>

