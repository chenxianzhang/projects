<template>
    <el-dialog :visible.sync="showUploadDialog" @close="handleDialogClose()">
      <el-upload
        class="upload-demo"
        drag
        action="https://localhost:8443/service/userMgr/batchUploadStudents"
        :on-remove="handleRemove"
        :before-remove="beforeRemove"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :on-progress="handleUploadProgress"
        multiple>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>
      <div class="upload-progress" v-if="showProgress">
        <span>上传进度：</span>
        <el-progress :percentage="progress" :status="progressStatus"></el-progress>
      </div>
    </el-dialog>
</template>

<script>
    export default {
      name: "uploadStudentComp",
      props:['showUploadDialog'],
      data() {
        return {
          showProgress:false,
          progressStatus:'',
          progress:0,
        };
      },
      created(){
        let cId = this.$store.getters.courseId;
      },
      methods: {
        handleDialogClose(){
          this.$emit('hideUploadDialog', false);
        },
        handleRemove(file, fileList) {
          console.log(file, fileList);
        },
        beforeRemove(file, fileList) {
          return this.$confirm(`确定移除 ${ file.name }？`);
        },
        handleUploadSuccess(response, file, fileList){
          this.showProgress = true;
          this.progressStatus = 'success';
          this.progress = 100;
        },
        handleUploadError(err, file, fileList){
          this.showProgress = true;
          this.progressStatus = 'exception';
        },
        handleUploadProgress(event, file, fileList){
          this.showProgress = true;
          this.progressStatus = '';
        },
      }
    }
</script>

<style scoped>
.upload-progress{
  margin: 20px 20px 10px 30px;
}
</style>
