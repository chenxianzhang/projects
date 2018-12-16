<template>
  <!--<div class="layout" style="height: 100%;">-->
    <!--<page-title class="page-title" :homePage="false"></page-title>-->
    <!--<div class="main">-->
      <!--<sidebar class="sidebar-container"></sidebar>-->
      <!--<div class="container">-->
        <!--<tags-view></tags-view>-->
        <!--<el-scrollbar wrap-class="container-scrollbar" ref="routerScrollbar">-->
            <!--<transition name="fade-transform" mode="out-in" >-->
              <!--<keep-alive>-->
                <!--<router-view v-if="$route.meta.keepAlive" />-->
              <!--</keep-alive>-->
            <!--</transition>-->
          <!--<transition name="fade-transform" mode="out-in">-->
            <!--<router-view v-if="!$route.meta.keepAlive" />-->
          <!--</transition>-->
        <!--</el-scrollbar>-->
      <!--</div>-->
    <!--</div>-->

    <el-container class="main">

      <el-header style="height: 70px;">
        <page-title class="page-title" :homePage="false"></page-title>
      </el-header>

      <el-container>

        <el-aside width="240px">
          <sidebar style="height: 100%"></sidebar>
        </el-aside>

        <el-main style="padding: 0; background-color: #f3f4f8">
          <tags-view></tags-view>
          <div style="overflow: auto; padding: 20px; height: calc(100% - 50px);">
            <transition name="fade-transform" mode="out-in">
              <keep-alive>
                <router-view v-if="$route.meta.keepAlive"/>
              </keep-alive>
            </transition>
            <transition name="fade-transform" mode="out-in">
              <router-view v-if="!$route.meta.keepAlive"/>
            </transition>
          </div>
        </el-main>

      </el-container>
    </el-container>
  <!--</div>-->
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
  },
  methods: {}
}
</script>

<style lang="scss" scoped>
  .main {
    height: 100%;
    .el-header{
      padding: 0;
    }
    .sidebar-container {
      transition: width 0.28s;
      width: 240px !important;
      height: 100%;
      position: fixed;
      font-size: 0px;
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
</style>
