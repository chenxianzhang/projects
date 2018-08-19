import Vue from 'vue'
import Router from 'vue-router'
import layout from '@/views/layout/layout'

Vue.use(Router)

export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/home', component: () => import('@/views/home/index'), hidden: true },
  { path: '', component: () => import('@/views/home/index'), hidden: true }
]

export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})



export const asyncRouterMap = [
  {
    path: '/course',
    component: layout,
    alwaysShow: true, // will always show the root menu
    meta: {
      title: 'course',
      icon: 'lock',
      roles: ['admin', 'teacher', 'student'] // you can set roles in root nav
    },
  }
]

