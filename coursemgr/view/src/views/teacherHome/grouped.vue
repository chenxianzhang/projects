<template>
    <div>
      <div class="grouped-item" v-for="gItem in groups">
        <div>组长：{{gItem.leader}}</div>
        <div>
          <span>组员列表：</span>
          <span v-for="mItem in gItem.members">
            {{mItem.name}}（学号：{{mItem.code}}）
          </span>
        </div>
        <span>分组方式：{{gItem.groupType}}</span>
      </div>
    </div>
</template>

<script>
    export default {
      name: "grouped",
      data(){
          return{
            groups:[],
          }
      },
      created() {
        this.groups = [{leader: '王富贵', members: [{name: '张麻子', code: '123143546532342'}], groupType: '自由分组'},
          {leader: '王富贵', members: [{name: '张麻子', code: '123143546532342'},{name: '张麻子', code: '123143546532342'},{name: '张麻子', code: '123143546532342'}], groupType: '自由分组'},
          {leader: '王富贵', members: [{name: '张麻子', code: '123143546532342'},{name: '张麻子', code: '123143546532342'},{name: '张麻子', code: '123143546532342'}], groupType: '自由分组'},
          {leader: '王富贵', members: [{name: '张麻子', code: '123143546532342'},{name: '张麻子', code: '123143546532342'},{name: '张麻子', code: '123143546532342'}], groupType: '自由分组'}];
      },
    }
</script>

<style scoped>
  .grouped-item{
    padding: 10px;
    line-height: 30px;
    width: 90%;
    margin: 30px auto;
    display: grid;
    background-color: #FFD04B;
  }
  .grouped-item:hover{
    box-shadow: 0px 0px 10px 4px #3a8ee6;
  }
</style>
