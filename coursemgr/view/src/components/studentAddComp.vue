<template>
  <!--<el-dialog-->
    <!--:visible.sync="showStudentAddDialog"-->
    <!--title="编辑学生信息"-->
    <!--@close="handleClose"-->
    <!--@closed="handleClose">-->
  <drag-dialog title="编辑学生信息" width="36%" :dialogVisible="showStudentAddDialog"
               @close="handleClose" @confirm="handleSubmit"
               :hiddenOperator="studentOperInfo.operType === 'detail'">
    <el-form ref="regForm" v-model="studentOperInfo.student" label-width="120px">
      <el-form-item label="用户名：" required>
        <el-input v-model="studentOperInfo.student.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="学院：" required>
        <el-input v-model="studentOperInfo.student.college" placeholder="请输入学院名称"></el-input>
      </el-form-item>
      <el-form-item label="学号：" required>
        <el-input v-model="studentOperInfo.student.serialNo" placeholder="请输入学号"></el-input>
      </el-form-item>
      <el-form-item label="专业：" required>
        <el-input v-model="studentOperInfo.student.profession" placeholder="请输入专业名称"></el-input>
      </el-form-item>
      <el-form-item label="性别：" required>
        <el-radio-group v-model="studentOperInfo.student.sex">
          <el-radio label="男">男</el-radio>
          <el-radio label="女">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="电话：">
        <el-input v-model="studentOperInfo.student.cellphone" placeholder="请输入电话"></el-input>
      </el-form-item>
      <el-form-item label="邮箱：">
        <el-input v-model="studentOperInfo.student.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
    </el-form>

    <!--<span slot="footer" class="dialog-footer" v-show="this.studentOperInfo.operType !== 'detail'">-->
    <!--<el-button @click="handleClose">取 消</el-button>-->
    <!--<el-button type="primary" @click="handleSubmit">确 定</el-button>-->
  <!--</span>-->
  </drag-dialog>

</template>

<script>
  import { addStudent } from '@/api/student'
  import { update } from '@/api/login';
  import dragDialog from '@/components/dragDialog';

  export default {
    name: "studentAddComp",
    props:["showStudentAddDialog", 'studentOperInfo'],
    data(){
      return {

      }
    },
    components:{dragDialog},
    methods:{
      handleSubmit(){
        if(this.studentOperInfo.student.name.trim() === ''){
          this.$message.warning('用户名不能为空！');
          return;
        }
        if(this.studentOperInfo.student.serialNo.trim() === ''){
          this.$message.warning('学号不能为空！');
          return;
        }
        if(this.studentOperInfo.student.profession.trim() === ''){
          this.$message.warning('专业名称不能为空！');
          return;
        }
        if(this.studentOperInfo.student.college.trim() === ''){
          this.$message.warning('学院名称不能为空！');
          return;
        }
        if(this.studentOperInfo.student.sex.trim() === ''){
          this.$message.warning('性别不能为空！');
          return;
        }
        this.getDefaultPwd(this.studentOperInfo.student.serialNo);
        let self = this;
        /**
         * 学生信息更新
         * */
        if(this.studentOperInfo.operType === 'edit'){
          update(this.studentOperInfo.student).then(response=>{
            if(response.status === 0){
              self.$message.warning(response.msg);
              return;
            }
            self.$message.success("更新成功！");
            self.$emit('hideStudentAddDialog', false);
          });
          return;
        }
        /**
         * 学生信息添加
         * */
        addStudent({user:this.studentOperInfo.student, courseId:this.$store.getters.courseId}).then(response=>{
          if(response.status === 0){
            self.$message.warning(response.msg);
            return;
          }
          self.$message.success("添加成功！");
          self.$emit('hideStudentAddDialog', false);
        });
      },
      getDefaultPwd(serialNo){
        if(!serialNo || serialNo.length <= 6){
          this.$message.warning('请确认学号是否正确！');
          return;
        }
        this.studentOperInfo.student.password = serialNo.substr(serialNo.length - 6, 6);
      },
      handleClose(){
        this.$refs.regForm.resetFields();
        this.$emit('hideStudentAddDialog', false);
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
</style>
