<template>
  <div class="main-container">
    <div v-if="hasGroup" class="grouped-item">
      <div class="group-leader">
        <span>
          <span style="font-weight: bold; font-size: 24px; color: black;">{{groupInfo.groupLeaderName}}</span>
          <div style="border-radius: 12px; background-color: #E8EBF0; font-size: 12px; display: inline-block; height: 24px; line-height: 24px; margin-left: 10px; padding: 0 5px">
            <span>
              <span>组长</span>
              <span style="color: #41A599; display: inline-block; margin-left: 5px;">学号：{{groupInfo.groupLeaderNo}}</span>
            </span>
          </div>
        </span>
      </div>
      <div class="group-members">
        <span>组员列表：</span>
        <span v-for="(member,index) in groupInfo.groupMemberList">
            <span>
              <span>{{member.name}}</span>
              <span style="color: #41A599;">（学号：{{member.serialNo}}）</span>
            </span>
            <span v-if="index < groupInfo.groupMemberList.length - 1">,</span>
        </span>
      </div>
      <div class="divide-type">
          <span style="display: inline-flex; justify-content: center; width: 110px; text-align: center; background-color: rgb(224, 254, 252);
            align-items: center; line-height: 30px; border: 1px solid rgb(232, 235, 240);">
            <img src="../../../static/img/group/group-type.png" style="margin-right: 4px"/>分组方式</span>
          <span style=" display: inline-flex; align-items: center; justify-content: center;
            background-color: rgb(247, 247, 247); border: 1px solid rgb(232, 235, 240); margin-left: -4px;
            width: 80px; text-align: center; line-height: 30px;">{{groupInfo.groupedType}}</span>
      </div>
    </div>
    <div v-if="!hasGroup">
      <div class="no-group" v-if="!freedomGroup">
        <span>你目前还没有加入任何小组，请等待老师为学生分组</span>
      </div>
      <div class="no-group" v-if="freedomGroup">
        <span>该课程分组方式为<span style="color: red">自由分组</span>，你目前还没有加入任何小组，你可以点击“新建分组”按钮自己创建分组，或者线下通知组长将你加入小组。</span>
      </div>
      <div style="margin-top: 20px; float: right" v-if="freedomGroup">
        <el-button class="el-button--primary" @click="createGroup">新建分组</el-button>
      </div>
    </div>

    <el-dialog :visible.sync="showDialog" width="536px">
      <new-group-container ref="newGroupDialog"></new-group-container>
      <span slot="footer" class="dialog-footer">
          <el-button @click="showDialog = false">取 消</el-button>
          <el-button type="primary" @click="handleMakeGroup">确 定</el-button>
        </span>
    </el-dialog>

  </div>
</template>

<script>
  import newGroup from './newGroup'
  import {getGroupDetailByStudent} from '../../api/group'
  import {assignGroup} from "../../api/group";
  import {getCourseById} from "@/api/course";

  export default {
    name: "group-info",
    components: {'new-group-container': newGroup},
    data() {
      return {
        showDialog: false,
        groupInfo: null,
        hasGroup: false,
        freedomGroup: false
      }
    },
    created() {

      getCourseById({courseId: this.variables.courseId}).then(response => {
        if (response.status === 0) {
          this.$message.warning('获取课程分组模式失败');
          return;
        }
        if (response.data.groupingType === '自由分组') {
          this.freedomGroup = true;
        }
      }).catch(err => {
        console.log(err);
      })

      //获取个人分组
      this.getGroupDetails();
    },
    methods: {
      getGroupDetails(){
        getGroupDetailByStudent({courseId: this.variables.courseId, studentNo: this.$store.state.user.token})
          .then(resp => {
            if (resp.status === 0) {
              this.$message.warning('获取个人分组失败');
              return;
            }
            if (resp.data !== '') {
              this.hasGroup = true;
            }
            this.groupInfo = resp.data;
          });
      },
      handleMakeGroup() {
        //获取 新建分组-组员
        let newGroupMembers = this.$refs.newGroupDialog.zdGroupMembers;
        newGroupMembers.push(this.$store.state.user.token);
        //分组
        let assignGroupObj = {
          courseId: this.variables.courseId,
          leaderName: this.$store.state.user.name,
          groupLeaderNo: this.$store.state.user.token,
          studentNoList: newGroupMembers,
          stuSelfGroup: true
        };
        assignGroup(assignGroupObj)
          .then(resp => {
            if (resp.status === 0) {
              this.$message.warning('分组失败！' + resp.msg);
              return;
            }
            this.$message.success('分组成功！');
            this.showDialog = false;
            //获取分组信息
            this.getGroupDetails();
          });
      },
      createGroup() {
        this.showDialog = true;
      },
    },
  }
</script>

<style scoped>
  .main-container {
    height: 100%;
    width: 100%;
    padding: 10px;
  }

  .grouped-item {
    padding: 10px;
    line-height: 30px;
    width: calc(100% - 55px);
    margin: 30px auto;
    display: grid;
    border: 5px solid #E8EBF0;
  }

  .grouped-item:hover {
    /*box-shadow: 0px 0px 10px 4px #3a8ee6;*/
    border: 5px solid #9AEDE5;
  }

  .group-leader{
    border-bottom: 1px solid #F7F7F7;
  }

  .group-leader > span > span, .group-members > span, .group-members > span > span {
    font-size: 12px;
  }

  .divide-type{
    display: flex;
  }

  .no-group {
    height: 200px;
    width: 100%;
    display: flex;
    align-items: center;
    box-shadow: 0 0 4px 2px gray;
  }

  .no-group > span {
    font-size: 20px;
    margin: 0 auto;
  }
</style>
