import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'
import 'element-ui/lib/theme-chalk/index.css';
import './assets/css/global.css'
import request from './util/request'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
Vue.config.productionTip = false
Vue.prototype.request=request
NProgress.configure({
  easing: 'ease',  // 动画方式
  speed: 500,  // 递增进度条的速度
  showSpinner: false, // 是否显示加载ico
  trickleSpeed: 200, // 自动递增间隔
  minimum: 0.3 // 初始化时的最小百分比
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
