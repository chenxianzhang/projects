// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './permission' // permission control
import './icons' // icon
import store from './store'
import VueResource from 'vue-resource'

import '@/styles/index.scss' // global css
import '@/utils/extension.js'
import Msg from '@/utils/message'

Vue.config.productionTip = false

Vue.use(Element)
Vue.use(VueResource)
Vue.use(Msg)

// Vue.prototype.getGlobalConfig = function () {
//   this.$http.get("./static/global.config.json").then((result) => {
//     console.log('+++++++++++++++++++++++++')
//     //也可以用sessionStorage存储
//     Vue.prototype.BASE_API = result.body.BASE_API
//     // this.BASE_API = result.body.BASE_API
//   }).catch((error) => {
//     console.log(error)
//   })
// }

// axios.get('./static/global.config.js').then(response => {
//   debugger
//   let config = eval(response.data)
//   Vue.prototype.BASE_API = config.BASE_API
// })

// 将全局变量挂载到vue上
Vue.prototype.global = window.global


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})