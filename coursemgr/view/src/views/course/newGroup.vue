<template>
  <div class="group-add-container">
    <!--<div class="leader-span">组长：{{leader}}</div>-->
    <div>
      <span>组员选择：</span>
      <el-transfer
        filterable
        :filter-method="filterMethod"
        :titles="['所有学生', '分组名单']"
        filter-placeholder="请输入姓名"
        v-model="zdGroupMembers"
        :data="zdGroupData">
          <span slot-scope="{ option }">{{ option.label }}</span>
      </el-transfer>
    </div>
  </div>
</template>

<script>
    import {getStudentsByCourseId} from '../../api/course'
    export default {
      name: "newGroup",
      data() {
        return {
          leader:'',
          zdGroupData:[],
          zdGroupMembers:[],//指定分组学生
          filterMethod(query, item) {
            return item.label.indexOf(query) > -1;
          }
        }
      },
      created(){
        this.leader = this.$store.state.user.name;
        getStudentsByCourseId({courseId:this.$store.getters.courseId})
          .then(resp=>{
            if(resp.status === 0){
              this.$message.warning('获取学生失败');
              return;
            }
            this.groupableStudents = resp.data;
            this.zdGroupData = [];
            this.zdGroupMembers = [];
            for(let item of this.groupableStudents){
              if(item.serialNo !== this.$store.state.user.token){
                this.zdGroupData.push({label: item.name, key: item.serialNo, source:true});
              }
            }
          });
      },
      methods: {

      },
    }
</script>

<style scoped>
  .group-add-container{
    /*width: 100%;*/
    /*margin-top: 10px;*/
    /*box-shadow: 0px 0px 5px 5px #3276bd;*/
    /*border-radius: 1px;*/
    /*padding: 10px;*/
  }
  .leader-span{
    margin: 10px;
    height: 30px;
    line-height: 30px;
  }
  .member-choose{

  }
</style>
