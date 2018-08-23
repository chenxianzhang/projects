<template>
  <el-tabs type="card" @tab-click="changeTab">
    <el-tab-pane label="已分组">
      <grouped></grouped>
    </el-tab-pane>
    <el-tab-pane label="未分组">
      <not-grouped></not-grouped>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import Grouped from './grouped'
  import NotGrouped from "./NotGrouped";

    export default {
      name: "teacherGroupInfo",
      components:{NotGrouped, 'grouped':Grouped},
      data() {
        return {
          currentView: 'new-task',
        }
      },
      methods:{
        changeTab(a,b,c){
          console.log(a)
          console.log(b)
          console.log(c)
        }
      }
    }
</script>

<style scoped>

</style>
