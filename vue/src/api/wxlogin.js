const wxlogin = (appid, url) => {
    let redirect_uri = encodeURIComponent(url)
    window.location.href = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appid}&redirect_uri=${redirect_uri}&response_type=code&scope=snsapi_userinfo&state=STATUS#wechat_redirect`
}
