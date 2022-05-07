import request from "../util/request";
/**
 * 用户分页查询接口
 */
export const findPage = (data) => {
    return request({
        method: 'GET',
        url: 'http://localhost:8088/user/page',
        data
    })
}