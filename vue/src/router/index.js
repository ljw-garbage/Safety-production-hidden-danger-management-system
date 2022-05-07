import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '../views/Login.vue'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
const routes = [
  {
    path:'/',
    redirect:'/index'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta:{
      title:"登录"
    }
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path:'/index',
    name:'index',
    component:()=>import('../views/System/index.vue'),
    meta:{
      title:"首页"
    }
  },
  {
    //信息上报
    path:'/information_reported',
    name:'information_reported',
    component: ()=>import('../views/Reported/Information_reported'),
    meta:{
      title:'信息上报',
      requireAuth:true
    }
  },
  {
    //填写上报信息
    path: '/addReported',
    name:'addReported',
    component: ()=>import('../views/Reported/AddReported'),
    meta:{
      title:'填写上报信息'
    }
  },
  {
    //整改反馈
    path:'/feedback',
    name:'feedback',
    component: ()=>import('../views/Feedback/feedback'),
    meta:{
      title:'整改反馈',
      requireAuth: true
    }
  },
  {
    //警示案例
    path:'/service',
    name:'service',
    component: ()=>import('../views/Service/ServiceNotice'),
    meta:{
      title:'警示案例'
    }
  },
  {
    //安全通报
    path: '/guidance',
    name:'guidance',
    component: ()=>import('../views/Safety/Guidance'),
    meta:{
      title:'安全通报'
    }
  },
  {
    //安全通报详情
    path: '/guidance-detail',
    name:'guidanceDetail',
    component: ()=>import('../views/Safety/Guidance-detail'),
    meta:{
      title:'安全通报详情'
    }
  },
  {
    //重要内容
    path:'/importantNotice',
    name:'importantNotice',
    component: ()=>import('../views/Important/ImportNotice'),
    meta:{
      title:'重要内容'
    }
  },
  {
    //重要内容
    path:'/importantNotice-detail',
    name:'importantNotice-detail',
    component: ()=>import('../views/Important/ImportNotice-detail'),
    meta:{
      title:'重要内容详情'
    }
  },
  {
    //使用帮助
    path:'/instructions',
    name:'instructions',
    component: ()=>import('../views/Instructions/Instruction-help'),
    meta:{
      title:'使用帮助'
    }
  },
  {
    //使用帮助
    path:'/instructions-help-detail',
    name:'instructions-help-detail',
    component: ()=>import('../views/Instructions/Instructions-help-detail'),
    meta:{
      title:'使用帮助详情'
    }
  },
  {
    //进行反馈
    path: '/fill-feedback',
    name:'fill-feedback',
    component: ()=>import('../views/Feedback/Fill-feedback'),
    meta:{
      title:'进行反馈'
    }
  },
  {
    //反馈历史
    path: '/feedbackHistory',
    name:'feedbackHistory',
    component: ()=>import('../views/Feedback/FeedbackHistory'),
    meta:{
      title:'反馈历史'
    }
  },
  {
    //查看详情
    path:'/details',
    name:'details',
    component: ()=>import('../views/System/Details'),
    props:true,
    meta:{
      title:'查看详情'
    }

  },
  {
    //历史列表
    path: '/history-list',
    name:'history-list',
    component: ()=>import('../views/Reported/History-List'),
    meta: {
      title:'历史列表'
    }
  },
  {
    //用户信息详情页
    path: '/userInfo',
    name:'userInfo',
    component: ()=>import('../views/System/UserInfo'),
    meta: {
      title: '用户信息',
      requireAuth: true
    }
  },
  {
    //修改密码页
    path: '/edit-password',
    name:'editPassword',
    component: ()=>import('../views/System/EditPassword'),
    meta: {
      title: '修改密码',
      requireAuth: true
    }
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})
//路由守卫
router.beforeEach((to,from,next)=>{
  // 每次切换页面时，调用进度条
  NProgress.start();
  if (to.meta.requireAuth) {
    //获取token
    const tokenId = window.localStorage.getItem('tokenId')
    // const Detail=window.localStorage.getItem("Detail")?JSON.parse(localStorage.getItem("Detail")) : null
    // if (!tokenId) return next('/login')
    if(!tokenId) return next('/login')
  }else{
    document.title = to.meta.title

  }
  next()
})

router.afterEach(()=>{
  NProgress.done()
})
export default router


