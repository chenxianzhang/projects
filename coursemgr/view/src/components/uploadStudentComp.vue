<template>
    <!--<el-dialog :visible.sync="showUploadDialog" @close="handleDialogClose()">-->
  <drag-dialog title="批量导入" width="350px" :dialogVisible="showUploadDialog"
               @close="handleDialogClose" @confirm="handleImportStudents"
               :hiddenOperator="false">
    <el-upload ref="upload" v-if="showUploadDialog"
      class="upload-demo"
      name="file"
      drag
      accept=".xlsx, .xls"
      multiple="true"
      :auto-upload="false"
      :action="uploadAction"
      :before-upload="handleBeforeUpload"
      :on-remove="handleRemove"
      :before-remove="beforeRemove"
      :on-success="handleUploadSuccess"
      :on-error="handleUploadError"
      :on-progress="handleUploadProgress"
      :data="uploadData">
      <i class="custom-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <div class="el-upload__tip" slot="tip">只能上传[*.xls或*.xlsx]文件</div>
    </el-upload>
    <div class="upload-progress" v-if="showProgress">
      <span>上传进度：</span>
      <el-progress :percentage="progress" :status="progressStatus"></el-progress>
    </div>
  </drag-dialog>
</template>

<script>
import dragDialog from '@/components/dragDialog';

    export default {
      name: "uploadStudentComp",
      data() {
        return {
          showProgress:false,
          progressStatus:'',
          progress:0,
          uploadData:{}
        };
      },
      created(){
        this.uploadData.courseId = this.variables.courseId
      },
      props: {
        uploadAction: {
          type: String,
          default: window.global.BASE_API + "/userMgr/batchUploadStudents"
        },
        showUploadDialog: {
          type: Boolean,
          default: false
        }
      },
      watch: {
        uploadAction (val) {
          this.uploadAction = val;
        },
        showUploadDialog(val){
          if(val === false){
            this.progress = 0;
            this.showProgress = false;
          }
        }
      },
      components:{dragDialog},
      methods: {
        handleDialogClose(){
          this.$emit('hideUploadDialog', false);
        },
        handleBeforeUpload(file){
          let fileType = file.name.substr(file.name.lastIndexOf('.') + 1, 4);
          if(fileType !== 'xls' && fileType !== 'xlsx'){
            this.$message.warning('只能上传xls或者xlsx的文件！');
            this.progress = 0;
            return;
          }
        },
        handleRemove(file, fileList) {
          console.log(file, fileList);
        },
        beforeRemove(file, fileList) {
          return this.$confirm(`确定移除 ${ file.name }？`);
        },
        handleUploadSuccess(response, file, fileList){
          console.log(response)
          this.showProgress = true;
          this.progressStatus = 'success';
          this.progress = 100;
          this.$emit('hideUploadDialog', false);
        },
        handleUploadError(err, file, fileList){
          this.showProgress = true;
          this.progressStatus = 'exception';
        },
        handleUploadProgress(event, file, fileList){
          this.showProgress = true;
          this.progressStatus = '';
        },
        handleImportStudents(){
          //todo upload file
          this.$refs.upload.submit()
        },
      }
    }
</script>

<style scoped>
.upload-progress{
  margin: 20px 20px 10px 30px;
}
</style>

<style>

/* .el-upload{
  width: 100% !important;
}
.el-upload .el-upload-dragger{
  margin-left: 50px;
} */

</style>
