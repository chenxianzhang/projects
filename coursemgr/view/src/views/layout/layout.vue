<template>
  <div class="layout">
    <page-title :homePage="false"></page-title>
    <div class="main">
      <sidebar class="sidebar-container"></sidebar>
      <div class="container">
        <tags-view></tags-view>
        <el-scrollbar wrap-class="container-scrollbar" ref="routerScrollbar">
            <transition name="fade-transform" mode="out-in" >
              <keep-alive>
                <router-view v-if="$route.meta.keepAlive" />
              </keep-alive>
            </transition>
          <transition name="fade-transform" mode="out-in">
            <router-view v-if="!$route.meta.keepAlive" />
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
      return ['new']
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
      width: 240px !important;
      height: calc(100% - 70px);
      position: fixed;
      font-size: 0px;
      top: 70px;
      bottom: 0;
      left: 0;
      z-index: 1001;
      overflow: hidden;
    }
    // 主体区域
    .container {
      transition: margin-left 0.28s;
      margin-left: 240px;
      position: relative;
      overflow: hidden;
      .is-horizontal {
        display: none;
      }
    }
  }
}
</style>
