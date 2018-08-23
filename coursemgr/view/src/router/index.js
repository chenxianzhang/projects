import Vue from 'vue'
import Router from 'vue-router'
import layout from '@/views/layout/layout'

Vue.use(Router);

export const studentRouter = [
  { name:'sBasicInfo', path: '/studentHome/basicInfo', component: () => import('@/views/studentHome/basicInfo'), hidden: true },
  { name:'sGroupInfo', path: '/studentHome/groupInfo', component: () => import('@/views/studentHome/groupInfo'), hidden: true },
  { name:'taskInfo', path: '/studentHome/taskInfo', component: () => import('@/views/studentHome/taskInfo'), hidden: true },
  { name:'gradeInfo', path: '/studentHome/gradeInfo', component: () => import('@/views/studentHome/gradeInfo'), hidden: true },
  { name:'gTaskInfo', path: '/studentHome/gTaskInfo', component: () => import('@/views/studentHome/gTaskInfo'), hidden: true },//待办
];

export const teacherRouter = [
  { name:'tBasicInfo', path: '/teacherHome/basicInfo', component: () => import('@/views/teacherHome/basicInfo'), hidden: true },
  { name:'newTask', path: '/teacherHome/newTask', component: () => import('@/views/teacherHome/newTask'), hidden: true },
  { name:'uploadAnswer', path: '/teacherHome/uploadAnswer', component: () => import('@/views/teacherHome/uploadAnswer'), hidden: true },
  { name:'tGroupInfo', path: '/teacherHome/groupInfo', component: () => import('@/views/teacherHome/groupInfo'), hidden: true },
];

export const constantRouterMap = [
  { name:'login', path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { name:'home', path: '/home', component: () => import('@/views/home/index'), hidden: true },
  { name:'registery', path: '/register', component: () => import('@/views/register/register'), hidden: true },
  { name:'sHome', path: '/studentHome', component: () => import('@/views/studentHome/index'), hidden: true, children: studentRouter},
  { name:'tHome', path: '/teacherHome', component: () => import('@/views/teacherHome/index'), hidden: true, children: teacherRouter},
  { name:'', path: '', component: () => import('@/views/home/index'), hidden: true },

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

