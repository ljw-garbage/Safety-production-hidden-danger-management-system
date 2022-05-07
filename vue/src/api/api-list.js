import request from "./axios.config";

/**
 * 登录接口
 */
export const signIn = (data) => {
    return request({
        method: 'POST',
        url: '/user/login',
        data
    })
}
/**
 * 登录--获取验证码
 */

export const getCaptcha = () => {
  return request({
      method:'GET',
      url: '/code/getCaptchaBase64',
  })
}
export const checkCaptcha = (Code) => {
    return request({
        method:'GET',
        url: '/code/checkCaptcha?code='+Code,
    })
}

/**
 * 发送验证码接口
 * @param data
 * @returns {*}
 */
export const verificationCode=()=>{
    return request({
        method:'POST',
        url:'/code',
    })

}

/**
 *  手机接口验证接口
 * @param data
 * @returns {*}
 */
export const phoneinfoLogin=(data)=>{
    return request({
        method:'POST',
        url:'/claimmiddle/api/exclusive/middle/login/user/login/phoneinfo/app',
        data
    })
}
/**
 * 信息上报提交
 * @param data
 * @returns {*}
 */
export const reportSubmit = (data) => {
    return request({
        method: 'POST',
        url: '/report',
        data
    })
}
/**
 * 历史列表反馈
 * @param data
 * @returns {*}
 */
export const queryHistoryDetails = () => {
    return request({
        method: 'POST',
        url: '/feedback',
    })
}
/**
 * 警示案例接口
 *
 */
export const showDetails = () => {
    return request({
        method: 'GET',
        url: '/details',
    })
}
/**
 * 查看详情
 * @param data
 * @returns {*}
 */
export const getOneDetails=(id)=>{
    return request({
        method:'GET',
        url:'/details/'+id,
    })
    // request.get("/details",id)
}
/**
 * 图标按钮接口
 *
 */
export const showImages = () => {
    return request({
        method: 'GET',
        url: '/images',
    })
}
/**
 * 信息上报更新
 * @param data
 * @returns {*}
 */
export const reportUpdate = (data) => {
    return request({
        method: 'POST',
        url: '/report/update',
        data
    })
}