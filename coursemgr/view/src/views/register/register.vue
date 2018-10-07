<template>
  <div class="registery-container">
    <el-form ref="regForm" v-model="regInfo" label-width="100px" label-color="#fff" style="width: 400px; padding: 5px;">
      <el-form-item label="姓名：" required>
        <el-input v-model="regInfo.name" placeholder="请输入姓名" style="background: transparent"></el-input>
      </el-form-item>
      <el-form-item label="教工号：" required>
        <el-input v-model="regInfo.serialNo" placeholder="请输入教工号"></el-input>
      </el-form-item>
      <el-form-item label="学院：">
        <el-input v-model="regInfo.college" placeholder="请输入学院名称"></el-input>
      </el-form-item>
      <el-form-item label="电话：">
        <el-input v-model="regInfo.cellphone" placeholder="请输入电话"></el-input>
      </el-form-item>
      <el-form-item label="邮箱：">
        <el-input v-model="regInfo.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <el-form-item label="密码：" required>
        <el-input v-model="regInfo.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码：" required>
        <el-input v-model="pwdConfirm" placeholder="请确认密码"></el-input>
      </el-form-item>

      <el-button type="primary" @click="handleSubmit()" style="float: right; margin-left: 10px;">提 交</el-button>
      <el-button type="primary" @click="handleCancel()" style="float: right" >取 消</el-button>
    </el-form>
  </div>

</template>

<script>
  import {register} from "../../api/login";

  export default {
        name: "registry",
      data(){
          return {
            pwdConfirm:'',
            regInfo:{
              name:'',
              serialNo:'',
              college:'',
              cellphone:'',
              email:'',
              password:'',
              roles:'teacher',
              sex:'男',
              createDate:''
            }
          }
      },
      methods:{
        handleSubmit(){
          if(this.regInfo.name === ''){
            this.$message.warning('用户名不能为空！');
            return;
          }
          if(this.regInfo.serialNo === ''){
            this.$message.warning('教工号不能为空！');
            return;
          }
          // if(this.regInfo.college === ''){
          //   this.$message.warning('学院名称不能为空！');
          //   return;
          // }
          if(this.regInfo.password === ''){
            this.$message.warning('密码不能为空！');
            return;
          }
          if(this.regInfo.password !== this.pwdConfirm){
            this.$message.warning('两次密码不一致！');
            return;
          }
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
        },
        handleCancel(){
          this.$refs.regForm.resetFields();
        },
      },
    }
</script>

<style scoped>
  .registery-container{
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #2d3a4b;
    height: 100%;
    width: 100%;
    color: #fff;
  }
  label{
    color: #fff !important;
  }
</style>
<style>
  .registery-container .el-input__inner{
    background: transparent;
    border: 1px solid;
  }
  .registery-container .el-form-item__label{
    color: #fff;
  }
</style>
