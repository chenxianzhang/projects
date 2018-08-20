<template>
  <el-dialog v-el-drag-dialog @dragDialog="handleDrag" :title="title"
             @close="handleClose" :visible.sync="dialogTableVisible" :width="width">
    <slot></slot>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogTableVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleConfirm">确 定</el-button>
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
    props: ["title", "dialogVisible", "width"],
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
      background-color: #ccc;
      padding: 15px 20px 15px;
      .el-dialog__title {
        color: #677382;
      }
    }
    .el-dialog__body {
      padding: 10px 20px 20px 20px;
    }
  }

</style>
