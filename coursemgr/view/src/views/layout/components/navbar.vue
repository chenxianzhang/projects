<template>
    <el-menu
        class="custom-menu"
        :router="router"
        :collapse="collapse"
        :default-active="active"
        text-color="#fff"
        @select="handleSelect">
        <template v-for="menu in menus">
            <el-menu-item v-if="!menu.meta.isGroup" :key="menu.name" :index="resolvePath(menu.path)">
                <i :class="getIcon(menu)"></i>
                <span v-if="menu.meta.title" slot="title">{{ menu.meta.title }}</span>
            </el-menu-item>

            <el-submenu v-else :key="menu.name" :index="resolvePath(menu.path)">
                <template slot="title">
                    <i :class="getIcon(menu)"></i>
                    <span v-if="menu.meta.title" slot="title">{{ menu.meta.title }}</span>
                </template>
                <template v-for="child in menu.children">
                    <el-menu-item :key="child.name" :index="resolvePath(menu.path, child.path)">
                        <i :class="getIcon(child)"></i>
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
  },
  methods: {
    getIcon(tag) {
      if (tag.meta.activeIcon) {
        if (tag.path.indexOf('userMgr') >= 0
          || tag.path.indexOf('courseMgr') >= 0
          || tag.path.indexOf('backlog') >= 0) {
          return !this.isActive(tag) ? tag.meta.menuActiveIcon : tag.meta.icon
        }
        return this.isActive(tag) ? tag.meta.menuActiveIcon : tag.meta.icon
      }
      return tag.meta.icon
    },
    isActive(route) {
      return route.path === this.$route.path
    },
    resolvePath(...paths) {
      return path.resolve(this.basePath, ...paths)
    },
    handleSelect(index, indexPath) {

    }
  }
}
</script>
<style lang="scss">
.custom-menu {
  background-color: transparent !important;
  .el-menu {
    background-color: transparent !important;
  }
  .el-menu-item, .el-submenu__title {
    border-bottom: 1px solid rgb(19, 27, 34) !important;
    background-color: transparent !important;
    i {
      margin-right: 5px;
    }
    &:hover {
      background: url('../../../../static/img/menu-item-hover.png');
    }
  }
  .is-active {
    color: rgb(0, 149, 137) !important;
    border-left: 3px solid rgb(0, 150, 135);
  }
  .el-submenu {
    border-left: 0;
  }
}
</style>

<style lang="scss" scoped>

</style>
