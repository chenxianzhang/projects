<template>
  <div>
    <el-table
      ref="multipleTable"
      :data="groupableStudents"
      tooltip-effect="dark"
      style="width: 100%; border-bottom: 1px solid gray; padding-bottom: 10px">
      <el-table-column
        prop="name"
        label="姓名">
      </el-table-column>
      <el-table-column
        prop="serialNo"
        label="学号"
        show-overflow-tooltip>
      </el-table-column>
    </el-table>
    <div class="btn-group">
      <el-button type="primary" @click="makeGroup('随机分组')">随机分组</el-button>
      <el-button type="primary" @click="makeGroup('自由分组')">学生自由分组</el-button>
      <el-button type="primary" @click="makeGroup('指定分组')">指定分组</el-button>
      <el-button type="primary" @click="makeGroup('添加到分组')">添加到已有分组</el-button>
    </div>

    <el-dialog
      :visible.sync="dialogVisible"
      width="30%"
      :close="handleClose">
      <span slot="title">
        <div>{{groupType}}</div>
      </span>

      <span v-if="groupType === '随机分组'">平均每组人数：<input style="width: 40px;" v-model="randomGroupPerCnt" />人</span>
      <span v-if="groupType === '自由分组'">若指定自由分组，学生将自行担任组长，并创建分组！</span>
      <div v-if="groupType === '指定分组'">
        <el-transfer
          filterable
          :filter-method="filterMethod"
          :titles="['所有学生', '分组名单']"
          filter-placeholder="请输入姓名"
          v-model="zdGroupMembers"
          :data="zdGroupData"
          @change="transferChange">
          <span slot-scope="{ option }">{{ option.label }}
            <el-radio v-if="!option.source" v-model="groupLeader" :label="option.key" style="float: right;">设为组长</el-radio>
          </span>
        </el-transfer>
      </div>
      <div v-if="groupType === '添加到分组'">
        <el-table
          ref="multipleTable"
          :data="groupableStudents"
          tooltip-effect="dark"
          style="width: 100%; margin-bottom: 20px;"
          @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="120"> </el-table-column>
          <el-table-column prop="name" label="姓名"> </el-table-column>
          <el-table-column prop="serialNo" label="学号" show-overflow-tooltip> </el-table-column>
        </el-table>
        <el-select v-if="groupType === '添加到分组'" v-model="appointGroup" placeholder="请选择分组">
          <el-option
            v-for="item in usableGroups"
            :key="item.groupId"
            :label="item.groupId"
            :value="item.groupId">
          </el-option>
        </el-select>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleMakeGroup">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import { randomGroup, assignGroup, getGroupDetail, appendStudent2Group } from "../../api/group";
  import {getNoGroupStuList} from '../../api/course'

  export default {
      name: "NotGrouped",
      data(){
        return{
          groupableStudents:[],//可分组学生--针对当前任务
          multipleSelection: [],//多选项
          groupType:'',
          dialogVisible:false,
          usableGroups:[],//可选择的分组
          appointGroup:'',
          randomGroupPerCnt:'',

          groupLeader:'',
          zdGroupData:[],
          zdGroupMembers:[],//指定分组学生
          filterMethod(query, item) {
            return item.label.indexOf(query) > -1;
          }
        }
      },
    created(){
      this.getGroupableStudents();
    },
      methods: {
        getGroupableStudents(){
          this.groupableStudents = [];
          getNoGroupStuList({courseId:this.$store.getters.courseId})
            .then(resp=>{
              if(resp.status === 0){
                this.$message.warning('获取学生失败');
                return;
              }
              this.groupableStudents = resp.data;
            });
        },
        //设置穿梭后的数据格式
        transferChange(lost, direct, transferData){
          if(direct === 'left'){
            for(let j = 0; j < transferData.length; ++j){
              for(let i = 0; i < this.zdGroupData.length; ++i){
                if(this.zdGroupData[i].key === transferData[j]){
                  this.zdGroupData[i].source = true;
                  this.$set(this.zdGroupData, i, this.zdGroupData[i]);
                  break;
                }
              }
            }
          }
          else {
            for(let j = 0; j < transferData.length; ++j){
              for(let i = 0; i < this.zdGroupData.length; ++i){
                if(this.zdGroupData[i].key === transferData[j]){
                  this.zdGroupData[i].source = false;
                  this.$set(this.zdGroupData, i, this.zdGroupData[i]);
                  break;
                }
              }
            }
          }
        },
        toggleSelection(rows) {
          if (rows) {
            rows.forEach(row => {
              this.$refs.multipleTable.toggleRowSelection(row);
            });
          } else {
            this.$refs.multipleTable.clearSelection();
          }
        },
        handleSelectionChange(val) {
          this.multipleSelection = val;
        },
        makeGroup(type){
          this.groupType = type;
          if(type === '添加到分组'){
            let self = this;
            //获取分组
            getGroupDetail(this.$store.getters.courseId)
              .then(resp=>{
                if(resp.status === 0){
                  self.$message.warning('获取分组信息失败');
                  return;
                }
                self.usableGroups = resp.data;
              });
          }
          else if(type === '指定分组'){
            let self = this;
            //获取所有待分组学生，拼凑成所需格式
            this.zdGroupData = [];
            this.zdGroupMembers = [];
            for(let item of this.groupableStudents){
              this.zdGroupData.push({label: item.name, key: item.serialNo, source:true});
            }
          }

          this.dialogVisible = true;
        },
        handleClose(){

        },
        handleMakeGroup(){
          let self = this;
          switch (this.groupType) {
            case "随机分组":
              randomGroup({courseId: this.$store.getters.courseId, memberCnt: this.randomGroupPerCnt})
                .then(resp=>{
                  if(resp.status === 0){
                    self.$message.warning('随机分组失败：' + resp.msg);
                    return;
                  }
                  self.$message.success('随机分组成功！');
                  this.getGroupableStudents();
                });
              break;
            case "自由分组":

              break;
            case "指定分组":
              //获取指定分组对象
              let leaderName = '';
              for(let item of this.zdGroupData){
                if(item.key === this.groupLeader){
                  leaderName = item.label;
                }
              }
              let assignGroupObj = {
                courseId: this.$store.getters.courseId,
                leaderName: leaderName,
                groupLeaderNo: this.groupLeader,
                studentNoList: this.zdGroupMembers
              };
              assignGroup(assignGroupObj)
                .then(resp=>{
                  if(resp.status === 0){
                    self.$message.warning('指定分组失败！' + resp.msg);
                    return;
                  }
                  self.$message.success('指定分组成功！');
                  this.getGroupableStudents();
                });
              break;
            case "添加到分组":
              appendStudent2Group({groupId: this.appointGroup, studentNo: this.multipleSelection.join(',')})
                .then(resp=>{
                  if(resp.status === 0){
                    self.$message.warning('添加到分组失败！');
                    return;
                  }
                  self.$message.success('添加到分组成功！')
                  this.getGroupableStudents();
                });
              break;
            default:
              break;
          }
          this.dialogVisible = false;
        },
      }
    }
</script>

<style scoped>
  .btn-group{
    display: flex;
    align-items: center;
    justify-content: space-evenly;
    padding: 10px;
    margin: 0 15%;
  }
  .el-select{
    width: 100%;
  }

  .el-checkbox__label .el-radio{
    line-height: 30px;
  }
</style>
