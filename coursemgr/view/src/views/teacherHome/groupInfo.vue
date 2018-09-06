<template>
  <el-tabs type="card" @tab-click="changeTab">
    <el-tab-pane label="已分组">
      <grouped :groups="groups"></grouped>
    </el-tab-pane>
    <el-tab-pane label="未分组">
      <not-grouped :groupableStudents="groupableStudents"></not-grouped>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import Grouped from './grouped'
  import NotGrouped from "./NotGrouped";
  import { getGroupDetail } from '../../api/group'
  import {getNoGroupStuList} from '../../api/course'

    export default {
      name: "teacherGroupInfo",
      components:{NotGrouped, 'grouped':Grouped},
      data() {
        return {
          currentView: 'new-task',
          groups:[],
          groupableStudents:[]
        }
      },
      methods:{
        changeTab(comp,evt){
          if(comp.label === '已分组'){
            getGroupDetail({'courseId': this.$route.params.courseId})
              .then(response=>{
                if(response.status === 0){
                  self.$message.warning('获取分组信息失败：' + response.msg);
                  return;
                }
                self.groups = response.data;
              });
          }
          else if(comp.label === '未分组'){
            getNoGroupStuList({courseId:this.$route.params.courseId})
              .then(resp=>{
                if(resp.status === 0){
                  this.$message.warning('获取学生失败');
                  return;
                }
                this.groupableStudents = resp.data;
              });
          }
        }
      }
    }
</script>

<style scoped>

</style>
