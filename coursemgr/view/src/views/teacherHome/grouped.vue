<template>
    <div>
      <div class="grouped-item" v-for="gItem in groups">
        <div>组长：{{gItem.groupLeaderNo}}</div>
        <div>
          <span>组员列表：</span>
          <span v-for="mItem in gItem.groupMemberList">
            {{mItem.name}}（学号：{{mItem.serialNo}}）
          </span>
        </div>
        <span>分组方式：{{gItem.groupedType}}</span>
      </div>
    </div>
</template>

<script>
  import { getGroupDetail } from '../../api/group'
    export default {
      name: "grouped",
      data(){
          return{
            groups:[],
          }
      },
      created() {
        let self = this;
        getGroupDetail({'courseId': this.$store.getters.courseId})
          .then(response=>{
            if(response.status === 0){
              self.$message.warning('获取分组信息失败：' + response.msg);
              return;
            }
            self.groups = response.data;
          });
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
