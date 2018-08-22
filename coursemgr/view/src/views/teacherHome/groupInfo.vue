<template>
  <el-tabs type="card">
    <el-tab-pane label="已分组">
      <grouped></grouped>
    </el-tab-pane>
    <el-tab-pane label="未分组">

    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import NewTask from './newTask'
  import Grouped from './grouped'

    export default {
      name: "teacherGroupInfo",
      components:{'new-task': NewTask, 'grouped':Grouped},
      data() {
        return {
          currentView:'new-task',
          editableTabsValue2: '2',
          editableTabs2: [{
            title: 'Tab 1',
            name: '1',
            content: '<new-task></new-task>'
          }, {
            title: 'Tab 2',
            name: '2',
            content: 'Tab 2 content'
          }],
          tabIndex: 2
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
