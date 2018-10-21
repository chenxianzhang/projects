import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'// progress bar style
import { getToken } from '@/utils/auth' // getToken from cookie

NProgress.configure({ showSpinner: false })// NProgress Configuration

const whiteList = ['/login/logIn', '/login/register']// no redirect whitelist

function hasPermission(roles, permissionRoles) {
  if (roles.indexOf('admin') >= 0) return true // admin permission passed directly
  if (!permissionRoles) return true
  return roles.some(role => permissionRoles.indexOf(role) >= 0)
}

router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar
  if (getToken()) {
    // 存在token信息
    if (to.path === '/login/logIn') {
      // 如果当前是跳转登录页面，则可直接进入首页  开发时暂时注释
     // next({ path: '/' });
     // NProgress.done()

        next();   // 开发时使用
        NProgress.done();
    } else {
      if (store.getters.roles.length === 0) {
        store.dispatch('getUser').then(response => {
          // const roles = response.data.roles;
          // 根据角色动态生成可访问的路由表
          store.dispatch('generateRoutes', { loginRole: store.getters.loginRole }).then(() => { // 根据roles权限生成可访问的路由表
            router.addRoutes(store.getters.addRouters) // 动态添加可访问路由表
            next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
          })
        }).catch(err => {
          Message.error(err || '权限验证失败，请重新登录！');
          store.dispatch('logOut').then(() => {
            next({ path: '/' });
          });
        });
      } else {
        // 没有动态改变权限的需求可直接next() 删除下方权限判断 ↓
         if (hasPermission(store.getters.roles, to.meta.roles)) {
            next();
          } else {
              // TODO 增加401页面
             next({ path: '/401', replace: true, query: { noGoBack: true }})
          }
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) { // 在免登录白名单，直接进入
        next()
      } else {
        next('/login') // 否则全部重定向到登录页
        NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
      }
  }

})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
