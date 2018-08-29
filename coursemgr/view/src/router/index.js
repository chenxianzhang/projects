import Vue from 'vue'
import Router from 'vue-router'
import layout from '@/views/layout/layout'

Vue.use(Router);

// export const studentRouter = [
//   { name:'sBasicInfo', path: '/studentHome/basicInfo', component: () => import('@/views/studentHome/basicInfo'), hidden: true },
//   { name:'sGroupInfo', path: '/studentHome/groupInfo', component: () => import('@/views/studentHome/groupInfo'), hidden: true },
//   { name:'taskInfo', path: '/studentHome/taskInfo', component: () => import('@/views/studentHome/taskInfo'), hidden: true },
//   { name:'gradeInfo', path: '/studentHome/gradeInfo', component: () => import('@/views/studentHome/gradeInfo'), hidden: true },
//   { name:'gTaskInfo', path: '/studentHome/gTaskInfo', component: () => import('@/views/studentHome/gTaskInfo'), hidden: true },//待办
// ];
//
// export const teacherRouter = [
//   { name:'tBasicInfo', path: '/teacherHome/basicInfo', component: () => import('@/views/teacherHome/basicInfo'), hidden: true },
//   { name:'newTask', path: '/teacherHome/newTask', component: () => import('@/views/teacherHome/newTask'), hidden: true },
//   { name:'uploadAnswer', path: '/teacherHome/uploadAnswer', component: () => import('@/views/teacherHome/uploadAnswer'), hidden: true },
//   { name:'tGroupInfo', path: '/teacherHome/groupInfo', component: () => import('@/views/teacherHome/groupInfo'), hidden: true },
// ];

export const childrenRouter = [
  { name:'basicInfo', path: '/course/basicInfo', component: () => import('@/views/course/basicInfo'), hidden: true },
  { name:'sGroupInfo', path: '/course/groupInfo', component: () => import('@/views/course/groupInfo'), hidden: true },
  { name:'taskInfoList', path: '/course/taskInfoList', component: () => import('@/views/course/taskInfoList'), hidden: true },
  { name:'gradeInfo', path: '/course/gradeInfo', component: () => import('@/views/course/gradeInfo'), hidden: true },
  { name:'gTaskInfo', path: '/course/gTaskInfo', component: () => import('@/views/course/gTaskInfo'), hidden: true },//待办
  { name:'tGroupInfo', path: '/teacherHome/groupInfo', component: () => import('@/views/teacherHome/groupInfo'), hidden: true },
  { name:'newTask', path: '/teacherHome/grouped', component: () => import('@/views/teacherHome/grouped'), hidden: true },
  { name:'grouped', path: '/teacherHome/NotGrouped', component: () => import('@/views/teacherHome/NotGrouped'), hidden: true },
  { name:'notGrouped', path: '/teacherHome/newTask', component: () => import('@/views/teacherHome/newTask'), hidden: true },
  { name:'uploadAnswer', path: '/teacherHome/uploadAnswer', component: () => import('@/views/teacherHome/uploadAnswer'), hidden: true },
];

export const constantRouterMap = [
  { name:'login', path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { name:'home', path: '/home', component: () => import('@/views/home/index'), hidden: true },
  { name:'registery', path: '/register', component: () => import('@/views/register/register'), hidden: true },
  { name:'course', path: '/course', component: () => import('@/views/course/index'), hidden: true, children: childrenRouter},
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

