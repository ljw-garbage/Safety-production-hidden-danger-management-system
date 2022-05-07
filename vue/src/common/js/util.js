const checkIsMobile = function (value) {
    let flag = true
    if(!(/^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57]|16[6])[0-9]{8}$/.test(value))){
        flag = false
    }
    return flag
}
export function GetQueryString (name) {
    const href = window.location.href
    if (href.indexOf('?') > -1) {
        var search = href.split('?')[1]
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)')
        var r = search.substr(0).match(reg)
        if (r != null) return unescape(r[2]); return null
    }
}
export function isNull (val) {
    var flag = false
    if (val === null || val === undefined || val === '') {
        flag = true
    }
    return flag
}
export {checkIsMobile}
