import Vue from 'vue'
import Router from 'vue-router'
import layout from '@/views/layout/layout'

Vue.use(Router);

export const studentRouter = [
  { path: '/studentHome/basicInfo', component: () => import('@/views/studentHome/basicInfo'), hidden: true },
  { path: '/studentHome/groupInfo', component: () => import('@/views/studentHome/groupInfo'), hidden: true },
  { path: '/studentHome/taskInfo', component: () => import('@/views/studentHome/taskInfo'), hidden: true },
  { path: '/studentHome/gradeInfo', component: () => import('@/views/studentHome/gradeInfo'), hidden: true },
  { path: '/studentHome/gTaskInfo', component: () => import('@/views/studentHome/gTaskInfo'), hidden: true },//待办
];

export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/home', component: () => import('@/views/home/index'), hidden: true },
  { path: '', component: () => import('@/views/home/index'), hidden: true },
  { path: '/register', component: () => import('@/views/register/register'), hidden: true },
  { path: '/studentHome', component: () => import('@/views/studentHome/index'), hidden: true, children: studentRouter}

//  { path: '/', component: () => import('@/views/home/index'), hidden: true }
];

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

