<template>
  <el-scrollbar wrapClass="scrollbar-wrapper">
    <div class="user-info">
      <div class="portrait">
        <i class="custom-icon-protrait"></i>
      </div>
      <div class="curr-user">当前用户：{{ $store.getters.name }}</div>
    </div>
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
      this.resetPath(this.menus, this.variables.courseId)
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
  height: 100%;
  overflow-x: hidden !important;
  background: url('../../../../static/img/menu-bg.png');
  .el-scrollbar__view {
    height: 100%;
  }
  .el-menu {
    border: none;
    height: calc(100% - 196px);
    width: 100% !important;
  }
  .user-info {
    padding: 30px 0;
    border-bottom: 1px solid rgb(19, 27, 34);
    .portrait {
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .curr-user {
      color: rgb(0, 215, 194);
      text-align: center;
      font-size: 14px;
      margin-top: 20px;
    }
  }
}
.is-horizontal {
  display: none;
}
</style>
