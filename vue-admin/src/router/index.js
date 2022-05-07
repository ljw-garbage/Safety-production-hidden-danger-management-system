import Vue from 'vue'
import VueRouter from 'vue-router'
import Manage from "../views/Manage";
import store from '../store/index'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
Vue.use(VueRouter)
const routes = [
  { path:'/', redirect:'/login'},
  {
    path: '/login',
    name:'Login',
    component: ()=>import('../views/Login'),
    meta:{
      title:'登录',
    }
  },
  {
    path: '/register',
    name:'Register',
    component: ()=>import('../views/Register'),
    meta:{
      title:'注册',
    }
  },
  {
    path: '/404',
    name:'NotFound',
    component: ()=>import('../components/404'),
    meta:{
      title:'404 Not Found',
    }
  },
  // {
  //   path: '/Manage',
  //   component: Manage,
  //   redirect: '/welcome',
  //   children:[
  //     {path:'/welcome',name:'首页', component:()=>import('../views/Welcome'),meta:{title:'首页'}},
  //     {path:'/user',name:'用户管理',component:()=>import('../views/User/User'),meta:{title:'用户管理'}},
  //     {path:'/person',name:'个人信息',component:()=>import('../views/Person'),meta:{title:'个人信息'}},
  //     {path:'/file',name:'文件管理',component:()=>import('../views/File/File'),meta: {title: '文件管理'}},
  //     {path:'/role',name:'角色管理',component:()=>import('../views/Role/Role'),meta: {title: '角色管理'}},
  //     {path:'/menu',name:'菜单管理',component:()=>import('../views/Menu/Menu'),meta: {title: '菜单管理'}},
  //     {path:'/information_reported',name:'信息上报',component:()=>import('../views/Information/Information_reported'),meta: {title: '信息上报'}},
  //   ],
  //
  // }
]
const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})
//重置路由
export const resetRouter=()=>{
  router.matcher=new VueRouter({
    mode:'history',
    base:process.env.BASE_URL,
    routes
  })
}

export const setRoutes=()=>{
  const storeMenus=localStorage.getItem("menus")
  if(storeMenus){

    // setRoutes()
    //获取对象路由数组
    const currentRouteNames=router.getRoutes().map(v=>v.name)
    if (!currentRouteNames.includes('Manage')){
      const ManageRoute={path:'/Manage',name:'Manage',component:()=>import("../views/Manage"),redirect:'/welcome',children:[
          {path:'/person',name:'个人信息',component:()=>import('../views/Person'),meta:{title:'个人信息'}},
          {path:'/editPassword',name:'修改密码',component:()=>import('../views/EditPassword'),meta:{title:'修改密码'}},
          {path:'/news',name:'新闻',component:()=>import('../views/News'),meta:{title:'新闻'}},
          {path:'/VideoDetail',name:'视频详情',component:()=>import('../views/VideoDetail'),meta:{title:'视频详情'}},
        ]}
      const menu=JSON.parse(storeMenus)
      menu.forEach(item=>{
        if(item.path){
          const itemMenu={path:item.path.replace("/Manage",""),name:item.name,
            component:()=>import('../views/'+item.pagePath+'/'+item.pagePath+'.vue'),
            // component: () => import('../views/'+item.pagePath+'.vue'),
            meta:{title:item.name}
          }
          ManageRoute.children.push(itemMenu)
        }else if(item.children.length){
          item.children.forEach(item=>{
            if(item.path) {
              const itemMenu = {
                path: item.path.replace("/Manage",""),
                name: item.name,
                component: () => import('../views/'+item.pagePath+'/'+item.pagePath+'.vue'),
                // component: () => import('../views/'+item.pagePath+'.vue'),
                meta: {title: item.name}
              }
              ManageRoute.children.push(itemMenu)
            }
          })
        }
      })
      //最后要把路由动态加上去
      router.addRoute(ManageRoute)
    }
  }
}
setRoutes()


router.beforeEach((to, from, next) => {
  // 每次切换页面时，调用进度条
  // NProgress.start();
  if (to.path==='/login') return next();
  if(to.path==='/register') return next();
  // // //获取token
  let userInfo=localStorage.getItem("userInfo") ? JSON.parse(localStorage.getItem("userInfo")) : null

  const tokenId=userInfo.token
  localStorage.setItem("tokenId",tokenId)
  if(!tokenId)return next('/login')
  localStorage.setItem("currentPathName",to.name ) //为了在Header组件中去使用
  store.commit("setPath") //触发store 的数据更新

  //未找到路由
  if(!to.matched.length){
    const storeMenus=localStorage.getItem("menus")
    if(storeMenus){
      next("/404")
    }else{
      next("/login")
    }
  }
  next()
  // NProgress.done()
})
export default router
