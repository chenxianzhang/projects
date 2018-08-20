<template>
    <div class="layout">
        <!--TODO 左侧菜单栏-->

        <div class="container">
          <tags-view></tags-view>
          <router-view/>
        </div>
    </div>
</template>
<script>

import tagsView from '@/components/tagsView'

export default {
  name: "layout",
  data() {
    return {
    }
  },
  components: {
    tagsView
  },
  created () {
  },
  mounted() {

  }
}
</script>
<style scoped>

</style>
