<template>
  <!-- <div class="registery-container"> -->
    <el-form class="registery-form" ref="regForm" :model="regInfo" label-color="#fff" :rules="registerRules"
             style="background: white; box-shadow: 0px 0px 12px 1px grey; padding: 20px;">
      <el-form-item prop="name">
        <el-input placeholder="请输入姓名" v-model="regInfo.name">
          <i slot="prefix" class="icon icon-user-name"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="serialNo">
        <el-input placeholder="请输入教工号" v-model="regInfo.serialNo">
          <i slot="prefix" class="icon icon_user_code"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="college">
        <el-input placeholder="请输入学院" v-model="regInfo.college">
          <i slot="prefix" class="icon icon-user-college"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="cellphone">
        <el-input placeholder="请输入电话" v-model="regInfo.cellphone">
          <i slot="prefix" class="icon icon-user-tel"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="email">
        <el-input placeholder="请输入邮箱" v-model="regInfo.email">
          <i slot="prefix" class="icon icon-user-email"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input placeholder="请输入密码" v-model="regInfo.password" :type="pwdType">
          <i slot="prefix" class="icon icon-user-pwd"></i>
          <i slot="suffix" class="el-input__icon el-icon-view pwd-eye" @click.stop="showPwd('pwd')"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="pwdConfirm">
        <el-input placeholder="确认密码" v-model="regInfo.pwdConfirm" :type="pwdTypeConfirm">
          <i slot="prefix" class="icon icon-user-pwd"></i>
          <i slot="suffix" class="el-input__icon el-icon-view pwd-eye" @click.stop="showPwd('confirm')"></i>
        </el-input>
      </el-form-item>

      <div class="register-operator">
        <el-button type="primary" @click="handleCancel()" style="background-color: #009285" >取 消</el-button>
        <el-button type="primary" @click="handleSubmit()" style="background-color: #00574F;">提 交</el-button>
      </div>

      <!-- <el-row :gutter="20">
        <el-col :span="12">
          <el-button type="primary" @click="handleSubmit()" style="background-color: #00574F;">提 交</el-button>
        </el-col>
        <el-col :span="12">
          <el-button type="primary" @click="handleCancel()" style="background-color: #009285" >取 消</el-button>
        </el-col>
      </el-row> -->
    </el-form>
  <!-- </div> -->

</template>

<script>
  import {register} from "../../api/login";

  export default {
    name: "registry",
    data() {
      const validateUsername = (rule, value, callback) => {
        if (!value) {
          callback(new Error('用户名不能为空'))
        }
        else if (value.length > 10) {
          callback(new Error('用户名不能超过10个字符'))
        } else {
          callback()
        }
      };

      const validateSerialNo = (rule, value, callback) => {
        if (!value) {
          callback(new Error('教工号不能为空'))
        } else {
          callback()
        }
      };

      const validatePassword = (rule, value, callback) => {
        if (!value) {
          callback(new Error('密码不能为空'))
        }
        else if (value.length < 6) {
          callback(new Error('密码长度不能少于6个字符'))
        } else {
          callback()
        }
      };

      const validateConfirmPwd = (rule, value, callback) => {
        if (!value) {
          callback(new Error('密码不能为空'))
        }
        else if (value !== this.regInfo.pwdConfirm) {
          callback(new Error('两次密码不一致'))
        } else {
          callback()
        }
      };

      return {
        pwdType:'password',
        pwdTypeConfirm:'password',
        regInfo: {
          name: '',
          serialNo: '',
          college: '',
          cellphone: '',
          email: '',
          password: '',
          pwdConfirm: '',
          roles: 'teacher',
          sex: '男',
          createDate: '',
        },
        registerRules: {
          name: [
            {required: true, trigger: 'blur', validator: validateUsername}
          ],
          serialNo: [
            {required: true, trigger: 'blur', validator: validateSerialNo}
          ],
          password: [
            {required: true, trigger: 'blur', validator: validatePassword}
          ],
          pwdConfirm: [
            {required: true, trigger: 'blur', validator: validateConfirmPwd}
          ]
        },
      }
    },
      methods:{
        handleSubmit(){
          this.$refs.regForm.validate(valid => {
            if (valid) {
              this.regInfo.createDate = new Date();
              //保存注册信息
              register(this.regInfo)
                .then(resp=>{
                  if(resp.status === 0){
                    this.$message.warning('注册失败!');
                    return;
                  }
                  this.$message.success('注册成功');
                  this.$router.push({
                    name:"login",
                    params:{from:'registery',role:'teacher', serialNo:this.regInfo.serialNo, pwd:this.regInfo.password}});
                });
            }
            else {
              console.log('error submit!!')
              return false
            }
          });
        },
        handleCancel(){
          this.$refs.regForm.resetFields();
        },
        showPwd(type){
          if(type === 'pwd'){
            if(this.pwdType === 'password'){
              this.pwdType = 'text';
            }
            else {
              this.pwdType = 'password'
            }
          }
          else {
            if(this.pwdTypeConfirm === 'password'){
              this.pwdTypeConfirm = 'text';
            }
            else {
              this.pwdTypeConfirm = 'password'
            }
          }
        },
      },
    }
</script>

<style scoped>
  .registery-form .register-item{
    width: 100%;
    height: 40px;
    line-height: 40px;
    align-items: center;
    justify-content: space-around;
    display: flex;
    margin-top: 10px;
  }
  label{
    color: #fff !important;
  }
  .icon_user_code{
    background: url("../../../static/img/icon_user_code.png") no-repeat left;
  }
  .icon-user-college{
    background: url("../../../static/img/icon_user_college.png") no-repeat left;
  }
  .icon-user-tel{
    background: url("../../../static/img/icon_user_tel.png") no-repeat left;
  }
  .icon-user-email{
    background: url("../../../static/img/icon_user_email.png") no-repeat left;
  }
  .el-button--primary{
    width: 170px !important;
  }

  .pwd-eye:hover{
    cursor: pointer;
    color: #009285;
  }

  .register-operator {
    display: flex;
    justify-content: space-around;
  }
</style>
<style>
  .registery-form .el-input__inner{
    border: 1px solid #b4bccc;
    height: 40px;
    border-radius: 0;
  }
  .registery-form .el-input__inner:focus{
    border-color: #009285;
  }
  .registery-form .el-form-item__label{
    color: #fff;
  }
</style>
<style lang="scss">
  .registery-form{
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
