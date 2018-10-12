<template>
  <div class="layout">
    <page-title></page-title>
    <div class="main">
      <sidebar class="sidebar-container"></sidebar>
      <div class="container">
        <tags-view></tags-view>
        <el-scrollbar wrap-class="container-scrollbar" ref="routerScrollbar">
          <transition name="fade-transform" mode="out-in">
            <keep-alive :include="cachedViews">
              <router-view :key="key" />
            </keep-alive>
          </transition>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>
<script>
import tagsView from '@/components/tagsView'
import sidebar from './components/sidebar'
import pageTitle from './components/pageTitle'

export default {
  name: 'layout',
  data() {
    return {
      menus: [],
      basePath: ''
    }
  },
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      return this.$route.fullPath
    }
  },
  components: {
    tagsView,
    sidebar,
    pageTitle
  },
  created() {},
  mounted() {
    this.$refs.routerScrollbar.$el.style.height =
      window.innerHeight - 100 + 'px'

    let self = this
    window.onresize = () => {
      return (() => {
        self.$refs.routerScrollbar.$el.style.height =
          window.innerHeight - 100 + 'px'
      })()
    }
  },
  methods: {}
}
</script>

<style lang="scss">
.layout {
  .main {
    .container {
      .container-scrollbar {
        overflow-x: hidden;
        .el-scrollbar__view {
          padding: 20px;
        }
      }
    }
  }
}
</style>

<style lang="scss" scoped>
.layout {
  .main {
    position: relative;
    .sidebar-container {
      transition: width 0.28s;
      width: 180px !important;
      height: calc(100% - 50px);
      position: fixed;
      font-size: 0px;
      top: 50px;
      bottom: 0;
      left: 0;
      z-index: 1001;
      overflow: hidden;
    }
    // 主体区域
    .container {
      transition: margin-left 0.28s;
      margin-left: 180px;
      position: relative;
      overflow: hidden;
      .is-horizontal {
        display: none;
      }
    }
  }
}
</style>
