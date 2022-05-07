import axios from 'axios'
import { Toast, Dialog } from 'vant'
import 'vant/lib/index.css'
import { GetQueryString } from '../common/js/util'
import router from '../router/index'
const jwt = require('../assets/jwt')
axios.defaults.withCredentials=true;
let taskRowData = GetQueryString('taskRowData')
if (taskRowData) {
    taskRowData = JSON.parse(taskRowData)
    console.log(taskRowData, 'taskRowData')
    window.localStorage.setItem('tokenId', taskRowData.token)
}
const request = axios.create({
    baseURL: 'http://localhost:8088',
    timeout: 300 * 1000,
    headers: { 'Content-Type': 'application/json; charset=utf-8' }
})
// 在request 拦截器显示加载框
// request.interceptors.request.use(config => {
//     Toast.loading({
//         message: '加载中...',
//         forbidClick: true,
//         overlay: true
//     })
//     if ((config.data.code === '000000') || (config.data.code === '200')) {
//         const tokenId = config.headers.authorization
//         window.localStorage.setItem('tokenId', tokenId)
//     }
//
//     // 需要授权的API,必须在请求头使用Authorization
//     config.headers.Authorization = window.localStorage.getItem('tokenId')
//     return config
// }, err => {
//     return Promise.reject(err)
// })
// // 在response 拦截器隐藏加载框
// request.interceptors.response.use(response => {
//         Toast.clear()
//         // return res
//         // if (response.config.url.indexOf('/claimmiddle/api/exclusive/middle') >= 0) {
//         //     let decryptString = response.data ? JSON.stringify(response.data) : ''
//         //     const decryptReg = /<<<.+?>>>/g
//         //     const decryptArr = decryptString.match(decryptReg)
//         //     if (decryptArr) {
//         //         decryptArr.forEach(item => {
//         //             let temp = item.replace('<<<', '')
//         //             temp = temp.replace('>>>', '')
//         //             temp = jwt.decryptAES(temp)
//         //             temp = temp.replace(/"/g, '')
//         //             decryptString = decryptString.replace(item, temp)
//         //         })
//         //     }
//         //     response.data = decryptString ? JSON.parse(decryptString) : response.data
//         // }
//         if (response.data.code === '333333' || response.data.code === '9999') {
//             Dialog.alert({
//                 message: '您当前登录已失效，请重新登录'
//             }).then(() => {
//                 router.push('/login')
//             })
//         }
//         if (response.data.code === 'Z0000000') {
//             Dialog.alert({
//                 message: '接口交互系统失败,请耐心等待'
//             }).then(() => {
//                 router.push('/login')
//             })
//         }
//         if (response.data.code === 'Z0001011') {
//             // console.log(response.data.responseMsg)
//             Dialog.alert({ message: '验证码错误请重试！' })
//         }
//         return response
//     },
//     error => {
//         console.log(error)
//         // 404等问题可以在这里处理
//         // if (error.response && error.response.data.code === '2002' || error.response.data.code === '2001' || error.response.data.code === '9999') {
//         //     Dialog.alert({
//         //         message: '您当前登录已失效，请重新登录'
//         //     }).then(() => {
//         //         router.push('/login')
//         //     })
//         // } else {
//         //     Toast({
//         //         message: '网络或服务器可能出现问题'
//         //     })
//         // }
//     })
// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
        Toast.loading({
        message: '加载中...',
        forbidClick: true,
        overlay: true
    })
    const user = localStorage.getItem("loginInfo") ? JSON.parse(localStorage.getItem("loginInfo")) : null;
    //
    if(user){
        // config.headers.Authorization =user.token;
        config.headers['token'] = user.token;  // 设置请求头
        const tokenId = config.headers['token']
        window.localStorage.setItem('tokenId', tokenId)
    }
    // config.headers['token'] = user.token;  // 设置请求头
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        Toast.clear()
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        if (res.code === '333333' || res.code === '9999') {
            Dialog.alert({
                message: '您当前登录已失效，请重新登录'
            }).then(() => {
                router.push('/login')
            })
        }
        if (res.code === 'Z0000000') {
            Dialog.alert({
                message: '接口交互系统失败,请耐心等待'
            }).then(() => {
                router.push('/login')
            })
        }
        if (res.code === 'Z0001011') {
            // console.log(response.data.responseMsg)
            Dialog.alert({ message: '验证码错误请重试！' })
        }
        if(res.code==='401'){
            Dialog.alert({message:res.msg})
        }
        return res;
    },
    error => {

        // if (error.res && error.res.code === '2002' || error.res.code === '2001' || error.res.code === '9999') {
        //     Dialog.alert({
        //         message: '您当前登录已失效，请重新登录'
        //     }).then(() => {
        //         router.push('/login')
        //     })
        // }
        // else{
        //     Toast({
        //         message: '网络或服务器可能出现问题'
        //     })
        // }
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)
export default request
