<template>
  <div style="height: 100%; overflow: auto;">
    <!--<div v-if="hasGroup" class="grouped-item" v-for="(gItem, index) in groups"-->
         <!--@mousemove="showDismissBtn($event, index)" @mouseleave="hideDIsmissBtn($event, index)">-->
      <!--<el-row>-->
        <!--<el-col :span="22">-->
          <!--<div>组长：{{gItem.groupLeaderName}}（学号：{{gItem.groupLeaderNo}}）</div>-->
          <!--<div>-->
            <!--<span>组员列表：</span>-->
            <!--<span v-for="mItem in gItem.groupMemberList">-->
            <!--{{mItem.name}}（学号：{{mItem.serialNo}}）-->
          <!--</span>-->
          <!--</div>-->
          <!--<span>分组方式：{{gItem.groupedType}}</span>-->
        <!--</el-col>-->
        <!--<el-col :span="2" v-show="gItem.show">-->
          <!--<el-button type="primary" size="small" round-->
                     <!--style="margin-top: 10px;"-->
                     <!--@click.stop="dismissGroup(gItem)">-->
            <!--解散分组-->
          <!--</el-button>-->
          <!--<el-button type="primary" size="small" round-->
                     <!--style="margin-top: 10px; margin-left: 0"-->
                     <!--@click.stop="handleChangeLeader(gItem)">变更组长</el-button>-->
        <!--</el-col>-->
      <!--</el-row>-->
    <!--</div>-->

    <div v-if="hasGroup" class="grouped-item" v-for="(groupInfo, index) in groups"
         @mousemove="showDismissBtn($event, index)" @mouseleave="hideDIsmissBtn($event, index)">
      <el-row>
        <el-col :span="22">
          <div class="group-leader">
        <span>
          <span style="font-weight: bold; font-size: 24px; color: black;">{{groupInfo.groupLeaderName}}</span>
          <div
            style="border-radius: 12px; background-color: #E8EBF0; font-size: 12px; display: inline-block; height: 24px; line-height: 24px; margin-left: 10px; padding: 0 5px">
            <span>
              <span>组长</span>
              <span
                style="color: #41A599; display: inline-block; margin-left: 5px;">学号：{{groupInfo.groupLeaderNo}}</span>
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
        </el-col>
        <el-col :span="2" v-show="groupInfo.show">
          <el-button size="small" round plain
                     style="margin-top: 10px;"
                     @click.stop="dismissGroup(groupInfo)">
            解散分组
          </el-button>
          <el-button type="primary" size="small" round
                     style="margin-top: 10px; margin-left: 0"
                     @click.stop="handleChangeLeader(groupInfo)">变更组长
          </el-button>
        </el-col>
      </el-row>
    </div>


    <div v-if="!hasGroup" class="grouped-item">暂无分组信息</div>
    <el-dialog :visible.sync="changeLeaderDialog" title="更改组长" width="300px" @close="handleDialogClose">
        <el-select v-model="changeLeaderObj.studentNo" placeholder="请选择组长" @change="handleSelectClk" style="width: 100%;">
        <el-option
          v-for="item in options"
          :key="item.id"
          :label="item.name"
          :value="item.serialNo">
        </el-option>
      </el-select>
      <div slot="footer" class="dialog-footer">
        <el-button @click="changeLeaderDialog = false">取 消</el-button>
        <el-button type="primary" @click="changeGroupLeader">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { getGroupDetail, relieveGroup, changeGroupLeader } from '../../api/group'
    export default {
      name: "grouped",
      data(){
          return{
            changeLeaderDialog:false,
            hasGroup:false,
            groups:[],
            curGroup:{},
            changeLeaderObj:{studentName:'', studentNo:''},
            options:[],
          }
      },
      created(){
        this.getData();
      },
      methods:{
        /**
         * getData 获取数据
         * */
        getData(){
          getGroupDetail({'courseId': this.variables.courseId})
            .then(response=>{
              if(response.status === 0){
                this.$message.warning('获取分组信息失败：' + response.msg);
                return;
              }
              if(!response.data || response.data.length === 0){
                this.hasGroup = false;
                return;
              }
              this.hasGroup = true;
              for(let item of response.data){
                item['show'] = false;
              }
              this.groups = response.data;
            });
        },
        /**
         * dismissGroup 解散分组
         * id 分组id
         * no 分组编号
         * */
        dismissGroup(groupInfo){
          relieveGroup({ groupId:groupInfo.groupId, groupNo:groupInfo.groupNo})
            .then(resp=>{
              if(resp.status === 0){
                this.$message.warning('解散分组失败！');
              }
              this.$message.success('解散分组成功！');
              //刷新界面
              this.getData();
            });
        },
        showDismissBtn(e, index){
          this.$set(this.groups[index], 'show', true);
        },
        hideDIsmissBtn(e, index){
          this.$set(this.groups[index], 'show', false);
        },
        /**
         * handleChangeLeader 更改组长点击事件
         * group 当前选择的小组
         * */
        handleChangeLeader(group){
          this.changeLeaderDialog = true;
          this.curGroup = group;
          this.options = [];
          for(let item of group.groupMemberList){
            if(item.serialNo !== group.groupLeaderNo){
              this.options.push(item);
            }
          }
        },
        /**
         * changeLeader 更换组长
         * params { groupId:'', studentNo:'', studentName:''}
         * */
        changeGroupLeader(){
          changeGroupLeader({ groupId: this.curGroup.groupId,
            studentNo:this.changeLeaderObj.studentNo,
            studentName:this.changeLeaderObj.studentName})
            .then(resp=>{
              if(resp.status === 0){
                this.$message.warning('更换组长失败');
                return;
              }
              this.$message.success('更换组长成功');
              this.changeLeaderDialog = false;
              this.getData();
            });
        },
        /**
         * handleSelectClk 下拉框选择事件
         * */
        handleSelectClk(val){
          let id = val;
          for(let item of this.options){
            if(item.serialNo === id){
              this.changeLeaderObj.studentName = item.name;
              break;
            }
          }
          this.changeLeaderObj.studentNo = id;
        },
        /**
         * handleDialogClose 弹窗关闭响应事件
         * */
        handleDialogClose(){
          this.changeLeaderObj.studentNo = '';
          this.changeLeaderObj.studentName = '';
        }
      }
    }
</script>

<style scoped>
  .grouped-item{
    padding: 10px;
    line-height: 30px;
    width: calc(100% - 55px);
    margin: 30px auto;
    display: grid;
    border: 5px solid #E8EBF0;
  }
  .grouped-item:hover{
    border: 5px solid #9AEDE5;
  }

  .group-leader > span > span, .group-members > span, .group-members > span > span {
    font-size: 12px;
  }

  .divide-type{
    display: flex;
  }
</style>
