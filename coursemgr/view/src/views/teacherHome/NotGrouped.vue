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
      <el-button type="primary" @click="makeGroup('randomGroup')">随机分组</el-button>
      <el-button type="primary" @click="makeGroup('freeGroup')">学生自由分组</el-button>
      <el-button type="primary" @click="makeGroup('appointGroup')">指定分组</el-button>
      <el-button type="primary" @click="makeGroup('add2AppointGroup')">添加到已有分组</el-button>
    </div>

    <el-dialog
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <span slot="title">
        <div>{{groupType}}</div>
      </span>

      <span v-if="groupType === '随机分组'">平均每组人数：<input style="width: 40px;" />人</span>
      <span v-if="groupType === '自由分组'">若指定自由分组，学生将自行担任组长，并创建分组！</span>
      <div v-if="groupType === '指定分组'">
        成都市城市
      </div>
      <el-select v-if="groupType === '添加到分组'" v-model="appointGroup" placeholder="请选择分组">
        <el-option
          v-for="item in usableGroups"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
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
          switch (type) {
            case "randomGroup":
              this.groupType = '随机分组';

              break;
            case "freeGroup":
              this.groupType = '自由分组';

              break;
            case "appointGroup":
              this.groupType = '指定分组';

              break;
            case "add2AppointGroup":
              this.groupType = '添加到分组';
              //获取所有可用分组
              this.usableGroups = [{label:'xxx1', value:'xxx1'},{label:'xxx2', value:'xxx2'},{label:'xxx3', value:'xxx3'}];
              break;
            default:
              break;
          }
        },
        handleClose(){

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
