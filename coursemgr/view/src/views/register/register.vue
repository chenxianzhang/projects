<template>
    <el-form ref="regForm" v-model="regInfo">
      <el-form-item label="姓名：">
        <el-input v-model="regInfo.regName" placeholder="请输入姓名" required></el-input>
      </el-form-item>
      <el-form-item label="教工号：">
        <el-input v-model="regInfo.regUnit" placeholder="请输入教工号" required></el-input>
      </el-form-item>
      <el-form-item label="学院：">
        <el-input v-model="regInfo.regUnit" placeholder="请输入学院名称" required></el-input>
      </el-form-item>
      <el-form-item label="电话：">
        <el-input v-model="regInfo.regTel" placeholder="请输入电话" required></el-input>
      </el-form-item>
      <el-form-item label="邮箱：">
        <el-input v-model="regInfo.regEmail" placeholder="请输入邮箱" required></el-input>
      </el-form-item>
      <el-form-item label="密码：">
        <el-input v-model="regInfo.pwd" placeholder="请输入密码" required></el-input>
      </el-form-item>
      <el-form-item label="确认密码：">
        <el-input v-model="regInfo.pwdConfirm" placeholder="请确认密码" required></el-input>
      </el-form-item>

      <el-button type="primary" @click="handleSubmit()">提 交</el-button>
      <el-button type="primary" @click="handleCancel()">取 消</el-button>
    </el-form>
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

        },
        handleCancel(){
          this.$refs.regForm.resetField();
        },
      },
    }
</script>

<style scoped>

</style>
