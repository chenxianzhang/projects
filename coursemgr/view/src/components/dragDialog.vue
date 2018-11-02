<template>
  <el-dialog v-el-drag-dialog @dragDialog="handleDrag" :title="title"
             @close="handleClose" :visible.sync="dialogTableVisible" :width="width">
    <slot></slot>
      <span slot="footer" class="dialog-footer" v-if="!hiddenOperator">
        <el-button @click="dialogTableVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirm">确定</el-button>
      </span>
  </el-dialog>
</template>
<script>

import elDragDialog from '@/directive/el-dragDialog'

  export default {
    name: 'dragDialog',
    directives: { elDragDialog },
    data() {
      return {
        dialogTableVisible: false
      };
    },
    props: ["title", "dialogVisible", "width", "hiddenOperator"],
    created () {
      this.dialogTableVisible = this.dialogVisible;
    },
    mounted () {
    },
    methods: {
      // v-el-drag-dialog onDrag callback function
      handleDrag() {

      },
      handleClose() {
        this.$emit("close");
      },
      handleConfirm() {
        this.$emit("confirm");
      }
    },
    watch: {
      dialogVisible(val) {
        this.dialogTableVisible = val;
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss">

  .el-dialog {
    .el-dialog__header {
      background-color: #009788;
      padding: 15px 20px 15px;
      height: 50px;
      color: white;
      .el-dialog__title {
        color: #fff;
      }
      .el-dialog__close {
        color: #fff;
      }
    }
    .el-dialog__body {
      //padding: 10px 20px 20px 20px;
    }
    .el-dialog__footer {
        text-align: center;
    }
  }

</style>
