<template>
  <el-scrollbar wrapClass="scrollbar-wrapper">
    <navbar :menus="menus" :base-path="basePath" :router="true" :active="$route.path"></navbar>
  </el-scrollbar>
</template>
<script>
import navbar from './navbar'

export default {
  name: 'sidebar',
  data() {
    return {
      menus: [],
      basePath: ''
    }
  },
  components: {
    navbar
  },
  created() {
    if (
      this.$store.getters.addRouters &&
      this.$store.getters.addRouters.length > 0
    ) {
      this.menus = this.$store.getters.addRouters[0].children
      this.basePath = this.$store.getters.addRouters[0].path
      this.resetPath(this.menus, this.$route.params.courseId)
    }
  },
  methods: {
    resetPath(menus, courseId) {
      menus &&
        menus.forEach(menu => {
          menu.path = menu.path.replace(':courseId', courseId)
          this.resetPath(menu.children, courseId)
        })
    }
  }
}
</script>
<style lang="scss">
.scrollbar-wrapper {
  overflow-x: hidden !important;
  .el-scrollbar__view {
    height: 100%;
  }
  .el-menu {
    border: none;
    height: 100%;
    width: 100% !important;
  }
}
.is-horizontal {
  display: none;
}
</style>
