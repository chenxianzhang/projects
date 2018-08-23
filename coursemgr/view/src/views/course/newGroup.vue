<template>
  <div class="group-add-container">
      <div>组长：{{leader}}</div>
      <div>请选择组员：</div>
    <el-tree :data="studentsTreeData" show-checkbox node-key="id" :props="defaultProps">
    </el-tree>
  </div>
</template>

<script>
    export default {
        name: "newGroup",
      data(){
          return{
            leader:'张三',
            studentsTreeData:[{'id':1, label:'李四'}, {'id':2, label:'王五'}],
            defaultProps: {
              label: 'label'
            }
          }
      },
      methods: {
        getStudents(){
          //todo 后台获取所有学生列表
        },
      },
    }
</script>

<style scoped>
  .group-add-container{
    width: 100%;
    margin-top: 10px;
    box-shadow: 0px 0px 0px 5px #3276bd;
    -moz-box-shadow: 0px 0px 0px 5px #3276bd;
    -webkit-box-shadow: 0px 0px 0px 5px #3276bd;
    border-radius: 1px;
  }
</style>
