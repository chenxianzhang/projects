<template>
    <el-dialog :visible.sync="dialogFormVisible" @close="cancel">
      <div class="selection-setting">
        <div class="setting-label">选项设置</div>
        <el-input v-model="curSelect" placeholder="请输入选项值"/>
        <div class="el-icon-plus setting-plus" @click="addSelection"></div>
      </div>

      <div class="selection-container">
        <div class="select-item" v-for="(select,index) in selections">{{select}}
          <div class="el-icon-remove" @click="removeSelection(index)"></div>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmSelections">确定</el-button>
        <el-button type="primary" @click="cancel">取消</el-button>
      </div>
    </el-dialog>
</template>

<script>
    export default {
      name: "subjectSelectItemSetting",
      props:['dialogFormVisible'],
      data(){
          return{
            curSelect:'',
            selections:[],
          }
      },
      methods:{
        addSelection(){
          this.selections.push(this.curSelect);
          this.curSelect = '';
        },
        removeSelection(index){
          this.selections.splice(index, 1);
        },
        confirmSelections(){
          this.$emit('selections', this.selections);
          this.cancel();
        },
        cancel(){
          this.$emit('hide', false);
        },
      },
    }
</script>

<style scoped>
  .selection-setting{
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .selection-setting > .setting-label{
    width: 90px;
    margin-right: 10px;
  }
  .selection-setting > .setting-plus{
    margin-left: 10px;
  }
</style>
