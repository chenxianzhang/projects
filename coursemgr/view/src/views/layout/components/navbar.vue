<template>
    <el-menu 
        :router="router" 
        :collapse="collapse" 
        :default-active="active"
        background-color="#545c64"
        text-color="#fff" 
        active-text-color="#ffd04b"
        @select="handleSelect">
        <template v-for="menu in menus">
            <el-menu-item v-if="!menu.meta.isGroup" :key="menu.name" :index="resolvePath(menu.path)">
                <i :class="menu.meta.icon"></i>
                <span v-if="menu.meta.title" slot="title">{{ menu.meta.title }}</span>
            </el-menu-item>

            <el-submenu v-else :key="menu.name" :index="resolvePath(menu.path)">
                <template slot="title">
                    <i :class="menu.meta.icon"></i>
                    <span v-if="menu.meta.title" slot="title">{{ menu.meta.title }}</span>
                </template>
                <template v-for="child in menu.children">
                    <el-menu-item :key="child.name" :index="resolvePath(menu.path, child.path)">
                        <i :class="child.meta.icon"></i>
                        <span v-if="child.meta.title" slot="title">{{ child.meta.title }}</span>
                    </el-menu-item>
                </template>
            </el-submenu>
        </template>
    </el-menu>
</template>
<script>
import path from 'path'

export default {
  name: 'navbar',
  data() {
    return {}
  },
  props: {
    menus: {
      type: Array,
      required: true
    },
    basePath: {
      type: String,
      default: ''
    },
    router: {
      type: Boolean,
      default: false
    },
    collapse: {
      type: Boolean,
      default: false
    },
    active: {
        type: String,
        default: ''
    }
  },
  mounted() {
    console.log(this.menus)
  },
  methods: {
    resolvePath(...paths) {
      return path.resolve(this.basePath, ...paths)
    },
    handleSelect(index, indexPath) {

    }
  }
}
</script>
<style lang="scss" scoped>
</style>
