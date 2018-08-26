<template>
  <div>
    <el-table
      ref="multipleTable"
      :data="groupableStudents"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="120">
      </el-table-column>
      <el-table-column
        prop="name"
        label="姓名">
      </el-table-column>
      <el-table-column
        prop="code"
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
      :closed="handleClose">
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
          :data="groupableStudents">
        </el-transfer>
      </div>
      <el-select v-if="groupType === '添加到分组'" v-model="appointGroup" placeholder="请选择分组">
        <el-option
          v-for="item in usableGroups"
          :key="item.groupId"
          :label="item.groupId"
          :value="item.groupId">
        </el-option>
      </el-select>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleMakeGroup">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import { randomGroup, assignGroup, getGroupDetail } from "../../api/group";

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
          zdGroupMembers:[],//指定分组学生
          filterMethod(query, item) {
            return item.indexOf(query) > -1;
          }
        }
      },
      methods: {
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
          this.dialogVisible = true;
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
                });
              break;
            case "自由分组":

              break;
            case "指定分组":
              //todo 获取指定分组对象
              // private String groupLeaderNo;
              // private Integer courseId;
              // private List<String> studentNoList;
              let assignGroupObj = {
                courseId: this.$store.getters.courseId,
                groupLeaderNo:'',
                studentNoList:[]
              };
              assignGroup(assignGroupObj)
                .then(resp=>{
                  if(resp.status === 0){
                    self.$message.warning('指定分组失败！' + resp.msg);
                    return;
                  }
                  self.$message.success('指定分组成功！');
                });
              break;
            case "添加到分组":
              //获取所有可用分组
              this.usableGroups = [{label:'xxx1', value:'xxx1'},{label:'xxx2', value:'xxx2'},{label:'xxx3', value:'xxx3'}];
              break;
            default:
              break;
          }
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
</style>
