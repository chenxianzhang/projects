<template>
  <div>
    <Tinymce :height=400 ref="editor" v-model="postForm.content" />
  </div>
</template>

<script>
  import { Tinymce } from '../components/Tinymce/index'
    export default {
      name: "subjectAddComp",
      props:{
        defaultMsg: {
          type: String
        },
        config: {
          type: Object
        }
      },
      data () {
        return {
          editor: null
        }
      },
      mounted() {
        const _this = this;
        this.editor = UE.getEditor('editor', this.config); // 初始化UE
        this.editor.addListener("ready", function () {
          _this.editor.setContent(_this.defaultMsg); // 确保UE加载完成后，放入内容。
        });
      },
      methods: {
        getUEContent() { // 获取内容方法
          return this.editor.getContent()
        }
      },
      destroyed() {
        this.editor.destroy();
      }
    }
</script>

<style scoped>

</style>
