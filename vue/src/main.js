import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Vant from 'vant'
import 'vant/lib/index.css'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'


// createApp(App).use(store).use(router).mount('#app')
const app=createApp(App)
app.use(store).use(router).use(Vant)
document.addEventListener('deviceready',function(){
	app.mount('#app')
},false)
NProgress.configure({
	easing: 'ease',  // 动画方式
	speed: 500,  // 递增进度条的速度
	showSpinner: false, // 是否显示加载ico
	trickleSpeed: 200, // 自动递增间隔
	minimum: 0.3 // 初始化时的最小百分比
})
