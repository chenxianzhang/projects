<template>
  <div style="height: 100px; width: 100px">
    <image-upload
      class="image_upload"
      url='https://tsesb.yunjuhe.com.cn/medicalServer/doctor/uploadServerImage'
      :touch-size = 1
      :multiple = true
      field-name = 'serverImgFile'
      :max-count = 10
      @chooseImages='bindtap_chooseImages'>
    </image-upload>

    <img :src="image.src" alt="" v-for="(image , i) in images" @click="bingtap_preview(i)">

    <!-- 图片预览 -->
    <image-preview
      style="z-index:200"
      :images="preImages"
      v-model="index"
      :numIsShow="true"
      :deleteIsShow="true"
      @delete="bindtap_delete">
    </image-preview>

    <div @click="bindtap_upload">
      上传
    </div>
  </div>
</template>

<script>
  import {ImageUpload,ImagePreview} from 'vue-image-upload-preview'
    export default {
      name: "uploadAndPreviewImageComp",
      components:{'image-preview':ImagePreview, 'image-upload':ImageUpload},
      data(){
        return {
          index: -1,
          images: []
        }
      },
      computed:{
        preImages() {
          return this.images.map(v=>{return v.src});
        }
      },
      methods:{
        /**
         *  绑定函数 -- 选择图片
         */
        bindtap_chooseImages(res){
          if (Array.isArray(res)) {
            this.images = this.images.concat(res);
          }else {
            console.log(res);
          }
        },
        /**
         *  绑定函数 -- 删除图片
         */
        bindtap_delete(){
          this.images = this.images.filter((v,i) => {
            return this.index!==i;
          })
        },
        /**
         *  绑定函数 -- 预览图片
         */
        bingtap_preview(i){
          this.index = i;
        },
        /**
         *  绑定函数 -- 取消预览
         */
        bingtap_hiddenImg() {
          this.index = -1;
        },
        /**
         *  绑定函数 -- 上传图片
         */
        bindtap_upload(){
          this.$refs.imgaeUpload.uploadImages(this.images)
            .then(res => {
              console.log(res);
            }).catch(err => {
            console.log(err);
          })
        }
      }
    }
</script>

<style scoped>
  img{
    height: 60px;
    width: 60px
  }

  .image_upload{
    height: 60px;
    width: 60px;
    background: red
  }
</style>
