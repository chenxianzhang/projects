import Vue from 'vue'
import Router from 'vue-router'
import layout from '@/views/layout/layout'

Vue.use(Router);

export const constantRouterMap = [
  { name: 'login', path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { name: 'home', path: '/home', component: () => import('@/views/home/index'), hidden: true },
  { name: 'registery', path: '/register', component: () => import('@/views/register/register'), hidden: true }
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
      roles: ['admin', 'teacher', 'student'] // you can set roles in root nav
    },
    children: [{
      path: 'basicInfo',
      component: () => import('@/views/course/basicInfo'),
      name: 'basicInfo',
      meta: {
        title: '基本信息',
        icon: 'el-icon-info',
        roles: ['teacher', 'student']
      }
    }, {
      path: 'groupInfo',
      component: () => import('@/views/course/groupInfo'),
      name: 'groupInfo',
      meta: {
        title: '我的分组',
        icon: 'el-icon-menu',
        roles: ['student']
      }
    }, {
      path: 'myTask',
      component: () => import('@/views/course/taskInfoList'),
      name: 'myTask',
      meta: {
        title: '我的任务',
        icon: 'el-icon-document',
        roles: ['student']
      }
    }, {
      path: 'myGrade',
      component: () => import('@/views/course/gradeInfo'),
      name: 'myGrade',
      meta: {
        title: '我的成绩',
        icon: 'el-icon-tickets',
        roles: ['student']
      }
    }, {
      path: 'backlog',
      component: () => import('@/views/course/gTaskInfo'),
      name: 'backlog',
      meta: {
        title: '我的待办',
        icon: 'el-icon-bell',
        roles: ['student']
      }
    }, {        // 教师
      path: 'group',
      component: () => import('@/views/group/index'),
      name: 'group',
      meta: {
        title: '分组情况',
        isGroup: true,
        icon: 'el-icon-menu',
        roles: ['teacher']
      },
      children: [
        {
          path: 'grouped',
          component: () => import('@/views/teacherHome/grouped'),
          name: 'grouped',
          meta: {
            title: '已分组',
            icon: 'el-icon-success',
            roles: ['teacher']
          }
        },
        {
          path: 'nogroup',
          component: () => import('@/views/teacherHome/NotGrouped'),
          name: 'nogroup',
          meta: {
            title: '未分组',
            icon: 'el-icon-error',
            roles: ['teacher']
          }
        }
      ]
    }, {
      path: 'task',
      component: () => import('@/views/task/index'),
      name: 'task',
      meta: {
        title: '课程任务',
        isGroup: true,
        icon: 'el-icon-document',
        roles: ['teacher']
      },
      children: [{
        path: 'list',
        component: () => import('@/views/course/taskInfoList'),
        name: 'list',
        meta: {
          title: '任务列表',
          icon: 'el-icon-tickets',
          roles: ['teacher']
        }
      }, {
        path: 'new',
        component: () => import('@/views/teacherHome/newTask1'),
        name: 'new',
        meta: {
          title: '新建任务',
          icon: 'el-icon-news',
          roles: ['teacher']
        }
      }]
    }, {
      path: 'allGrade',
      component: () => import('@/views/course/gradeInfo'),
      name: 'allGrade',
      meta: {
        title: '所有成绩',
        icon: 'el-icon-tickets',
        roles: ['teacher']
      }
    }, {
      path: 'statAnalysis',
      component: () => import('@/views/teacherHome/courseStatistic'),
      name: 'statAnalysis',
      meta: {
        title: '统计分析',
        icon: 'el-icon-time',
        roles: ['teacher']
      }
    }, {  // 管理员模块
      path: 'userMgr',
      component: () => import('@/views/manager/userMgr/index'),
      name: 'userMgr',
      redirect: 'userMgr/teacher',
      meta: {
        title: '用户管理',
        isGroup: true,
        icon: 'el-icon-setting',
        roles: ['admin']
      },
      children: [{
        path: 'teacher',
        component: () => import('@/views/manager/userMgr/teacher'),
        name: 'teacher',
        meta: {
          title: '教师用户',
          icon: 'el-icon-tickets',
          roles: ['admin']
        }
      }, {
        path: 'student',
        component: () => import('@/views/manager/userMgr/student'),
        name: 'student',
        meta: {
          title: '学生用户',
          icon: 'el-icon-tickets',
          roles: ['admin']
        }
      }]
    }, {
      path: 'courseMgr',
      component: () => import('@/views/manager/courseMgr/index'),
      name: 'courseMgr',
      meta: {
        title: '课程管理',
        icon: 'el-icon-setting',
        roles: ['admin']
      }
    }]
  },
  { name: '', path: '', component: () => import('@/views/home/index'), meta: { roles: ['teacher', 'student']} },
  { name: '', path: '', component: layout, redirect: 'course/userMgr', meta: { roles: ['admin'] } }
]

