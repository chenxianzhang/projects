<template>
    <div class="main-container">
      <div v-if="basicInfo.hasGroup">
        <div class="group-leader">
          <span>组长：<span>{{basicInfo.groupLeader.name}}  （学号：{{basicInfo.groupLeader.code}}）</span></span>
        </div>
        <div class="group-members">
          <span>组员列表：</span>
          <span v-for="member in basicInfo.groupMembers">
          <span>{{member.name}}  （学号：{{member.code}}）</span>
        </span>
        </div>
        <div class="divide-type">
          <span>分组方式：<span>{{basicInfo.divideType}}</span></span>
        </div>
      </div>
      <div v-if="!basicInfo.hasGroup">
        <div class="no-group">
          <span>该课程分组方式为“{{basicInfo.divideType}}”，你目前还没有加入任何小组，你可以点击“新建分组”按钮自己创建分组，或者线下通知组长将你加入小组。</span>
        </div>
        <div>
          <el-button class="el-button--primary">新建分组</el-button>
        </div>
      </div>
    </div>
</template>

<script>
    export default {
      name: "group-info",
      data(){
          return{
            basicInfo:{
              hasGroup:false,
              groupLeader:{name:'xxx', code:'12345678'},
              groupMembers:[{name:'xxx', code:'12345678'},{name:'xxx', code:'12345678'},{name:'xxx', code:'12345678'}],
              divideType:'自由分组',
            },
          }
      },
      methods:{
        getBasicInfo(){
          //todo 后台获取基本信息
        },
        createGroup(){

        },
      },
    }
</script>

<style scoped>
  .main-container{
    height: 100%;
    width: 100%;
    padding: 10px;
  }

  .group-leader > span > span,.group-members > span > span, .divide-type > span > span{
    font-size: 12px;
  }

  .no-group{
    height: 300px;
    width: 100%;
    display: flex;
    align-items: center;
    border: 1px solid;
  }
</style>
