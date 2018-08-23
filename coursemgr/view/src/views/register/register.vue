<template>
  <div class="registery-container">
    <el-form ref="regForm" v-model="regInfo" label-width="120px">
      <el-form-item label="姓名：" required>
        <el-input v-model="regInfo.regName" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="教工号：" required>
        <el-input v-model="regInfo.regNo" placeholder="请输入教工号"></el-input>
      </el-form-item>
      <el-form-item label="学院：" required>
        <el-input v-model="regInfo.regUnit" placeholder="请输入学院名称"></el-input>
      </el-form-item>
      <el-form-item label="电话：">
        <el-input v-model="regInfo.regTel" placeholder="请输入电话"></el-input>
      </el-form-item>
      <el-form-item label="邮箱：">
        <el-input v-model="regInfo.regEmail" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <el-form-item label="密码：" required>
        <el-input v-model="regInfo.pwd" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码：" required>
        <el-input v-model="regInfo.pwdConfirm" placeholder="请确认密码"></el-input>
      </el-form-item>

      <el-button type="primary" @click="handleSubmit()">提 交</el-button>
      <el-button type="primary" @click="handleCancel()">取 消</el-button>
    </el-form>
  </div>

</template>

<script>
    export default {
        name: "registry",
      data(){
          return {
            regInfo:{
              regName:'',
              regNo:'',
              regUnit:'',
              regTel:'',
              regEmail:'',
              pwd:'',
              pwdConfirm:''
            }
          }
      },
      methods:{
        handleSubmit(){
          if(this.regInfo.regName === ''){
            this.$message.warning('用户名不能为空！');
            return;
          }
          if(this.regInfo.regNo === ''){
            this.$message.warning('教工号不能为空！');
            return;
          }
          if(this.regInfo.regUnit === ''){
            this.$message.warning('学院名称不能为空！');
            return;
          }
          if(this.regInfo.pwd === ''){
            this.$message.warning('密码不能为空！');
            return;
          }
          if(this.regInfo.pwd !== this.regInfo.pwdConfirm){
            this.$message.warning('两次密码不一致！');
            return;
          }
          //todo 保存注册信息
          this.$router.push({name:'login', params:{from:'registery',role:'teacher', name:this.regInfo.regName, pwd:this.regInfo.pwd}});
        },
        handleCancel(){
          this.$refs.regForm.resetField();
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
