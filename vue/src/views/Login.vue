<template>
    <div class="login">
         <div class="login-container"></div>
        <van-form class="login-form" v-show="isShowLogin" validate-first @failed="onFailed" @submit="onSubmit" >
            <van-cell-group>
                <van-field
                        v-model="username"
                        name="username"
                        placeholder="请输入用户名"
                        label="用户名:"
                        class="jw-van-cell"
                        :rules="[{ required:true, message: '请输入正确用户名' }]"
                />
            </van-cell-group>
            <van-cell-group>
                <van-field
                        v-model="password"
                        name="password"
                        placeholder="请输入密码"
                        label="密码:"
                        type='password'
                        class="jw-van-cell"
                        :rules="[{ pattern, message: '请输入正确密码' }]"
                />
                <div class="keyboard"></div>
            </van-cell-group>
            <van-cell-group>
                <van-field
                        v-model="Code.inputStr"
                        name="captcha"
                        label="验证码:"
                        class="jw-van-cell"
                        placeholder="请输入验证码"
                        maxlength="4"
                        :rules="[{ validator:asyncValidator, message: '请填写正确验证码' }]"

                >
<!--                        :rules="[{ required:true, message: '请填写正确验证码' }]"-->
<!--                    :rules="[{ validator:asyncValidator, message: '请填写正确验证码' }]"-->
<!--                    <template #button>-->
<!--                        <div class="captcha-show" @click="onRefreshCode()">-->
<!--                            <van-image :src="captcha" alt="验证码" id="captcha"/>-->
<!--                            <input type="hidden" id="reCheckCode" value="${sessionScope.code}">-->
<!--                        </div>-->
<!--                    </template>-->
                    <template #right-icon>
                        <van-image :src="captcha" alt="验证码" id="captcha" @click="onRefreshCode()"/>
                    </template>
<!--                    <div slot="button" >-->
<!--                        <img :src="captcha"  @click="getVerifyCode"/>-->
<!--                    </div>-->
                </van-field>
            </van-cell-group>
            <van-row>
                <van-col :span="8" class="is-remember-password">
                    <!-- <div class="tick"
                     @click="onChecked"
                    > -->
                    <!-- <svg class="icon" width="14" height="14">
                      <use xlink:href="#icon-tick"/>
                    </svg> -->
                    <van-checkbox  v-model="checked"></van-checkbox>
                    <!-- </div> -->
                    <div class="desc">记住密码</div>
                </van-col>
                <van-col :span="16" class="is-remember-password">
                    <div style="color: rgb(25 137 250)" @click="toggleLoginType">手机号登录</div>
                    <div style="padding-left:30px;color: #2ac845" @click="toggleLoginErWeiMa">其他登录</div>
                </van-col>
            </van-row>
            <div style="margin: 16px;">
                <van-button round block type="primary" native-type="submit">登录</van-button>
            </div>
        </van-form>
        <van-form class="login-form" v-show="!isShowLogin" @submit="phoneInfoLogin">
            <van-cell-group>
                <van-field
                        class="jw-van-cell"
                        v-model="phoneInfo.phone"
                        label="手机号"
                        placeholder="请输入手机号"
                        :error-message="errorMobilePhone"
                />
            </van-cell-group>
            <van-cell-group>
                <van-field
                        class="jw-van-cell"
                        v-model="phoneInfo.captcha"
                        center
                        clearable
                        label="验证码"
                        placeholder="请输入验证码"
                        :rules="[{ required: true, message: '请填写验证码' }]"
                >
                    <template #button>
                        <van-button size="small" type="primary" :disabled="disabled" @click="getCode">{{btnText}}</van-button>
                    </template>
                </van-field>
            </van-cell-group>

            <van-row>
                <van-col :span="12" class="is-remember-password">
                    <div style="color: rgb(25 137 250)" @click="toggleLoginType">账号登录</div>
                </van-col>
            </van-row>
            <div style="margin: 16px;">
                <van-button round block type="primary" native-type="submit">
                    登录
                </van-button>
            </div>
        </van-form>

        <div class="privacy-policy-desc">
            <span class="desc">登录即同意</span>
            <span class="privacy-policy-link" @click="onPrivacy">隐私政策</span>
        </div>
    </div>
</template>
<script>
    import { ref,reactive,nextTick } from 'vue'
    import {checkIsMobile} from '../common/js/util'
    import {Toast,Dialog,Notify} from 'vant'
    import {useRouter} from 'vue-router'
    import {signIn, getCaptcha, verificationCode, getSessionCode, checkCaptcha} from "../api/api-list";
    import request from "../api/axios.config";
    import {wxlogin} from "../api/wxlogin";
    const jwt = require('../assets/jwt')
    export default ({
        setup() {
            const router=useRouter()
            let username=ref('admin')
            let password=ref('5tgb@WSX')
            let Code=reactive({
                inputStr:''
            })
            let isShowLogin=ref(true)
            let isRememberPassword=ref(true)
            let checked=ref(true)
            let captcha=ref('')
            let pattern=ref(/^(?=.*\d)(?=.*[a-zA-Z])[\da-zA-Z~!@#$%^&*]{6,18}$/)
            let btnText=ref('发送验证码')
            let disabled=ref(false)
            let timer=ref(null)
            let times=ref(null)
            let errorMobilePhone=ref('')
            //微信登录
            let isShowLoginWx=ref(true)
            let appid=ref('')
            let redirect_uri=ref('')
            let state=ref('1')
            let href=ref('http://localhost:8081/#/login')
            let phoneInfo=reactive({
                phone:'',
                captcha:'',
                branchCode:'',
                userCode:''
            })
            let randomCode=ref(null)
            const localStorageItem = {
                loginInfo: 'loginInfo'
            }
            const loginInfo = localStorage.getItem(localStorageItem.loginInfo) ? JSON.parse(localStorage.getItem(localStorageItem.loginInfo)) : null
            if (loginInfo) {

                const {
                   Username=username.value,Password=password.value
                }=loginInfo
                username.value= jwt.decryptAES(Username)
                password.value= jwt.decryptAES(Password)
            }
            nextTick(() => onRefreshCode())

            const onChecked=()=>{
                checked.value=!checked.value
            }
            function toggleLoginType(){
                isShowLogin.value=!isShowLogin.value
            }
            //二维码登录
            const toggleLoginErWeiMa=()=>{
                // wxlogin('wxde18ffa2f6abc977', 'http://localhost:8081/#/index')
                Dialog.alert({
                    message: '功能暂未开放',
                    theme: 'round-button',
                }).then(() => {
                    router.push({path:'/login'})
                });
            }
            function onFailed(){
                  // this.$toast('请输入后再登录')
                //console.log('1')
                Toast.fail('请输入正确后登录')
            }
            async function onSubmit(){
                const data={
                    username:username.value,
                    password:password.value,
                    captcha:Code.inputStr
                }

                const res=await signIn(data)
                // const result=await checkCaptcha(code.value)
                // if(result.code==='200'){
                //     Toast.clear();
                //     Toast.success('登录成功!')
                //     await router.push({path:'/index'})
                // }else{
                //     Toast.fail({message:result.msg})
                // }
                if(res.code==='200'){
                    // request.get('/code/checkCaptcha?code='+code.value).then(res=>{
                    //     // console.log(res)
                    //     if(res.code==='200'){
                    //         Toast.clear();
                    //         Toast.success('验证成功!')
                    //         router.push({path:'/index'})
                    //     }else{
                    //         Toast.fail({message:res.msg})
                    //     }
                    // })
                    if(checked){
                        localStorage.setItem(localStorageItem.loginInfo, JSON.stringify(res.data))
                        localStorage.setItem("username",username.value)
                    }else{
                        localStorageItem.removeItem(localStorageItem.loginInfo)
                    }

                    Toast.success('登录成功!')
                    await router.push({path: '/index'})

                }else{
                    Notify({ type: 'danger', message: res.msg });
                    Code.inputStr = ''
                    onRefreshCode()
                }
            }
            function onPrivacy(){
                Dialog.alert({
                    message: '功能暂未开放',
                    theme: 'round-button',
                }).then(() => {
                   router.push({path:'/login'})
                });
            }
            function getCode(){
                if(!checkIsMobile(phoneInfo.phone)){
                    errorMobilePhone.value='请输入正确手机号'
                    return;
                }
                countdown()
                verificationCode({
                    sign: requestCodeServer()
                }).then(res => {
                    if (res.code === '2000') {
                        phoneinfo.branchCode = res.data.unitCode
                        phoneinfo.userCode = res.data.userName
                    }
                })
            }
            function phoneInfoLogin(){}
            function requestCodeServer(){
                var message = `m=${phoneinfo.phone}&r=${getRandom(10)}`
                console.log(message)
                var key = 'ZXP123456789CPIC';
                var bytes = crypto.CryptoJS.enc.Utf8.parse(message);
                var hamcSign = crypto.CryptoJS.HmacSHA256(message, key);
                hamcSign.sigBytes += bytes.sigBytes;
                hamcSign.words = hamcSign.words.concat(bytes.words);
                var sign = crypto.CryptoJS.enc.Base64.stringify(hamcSign);
                console.log(sign);
                return sign
            }
            async function onRefreshCode(){
                //  request.get("/code/getCaptchaBase64").then(res=>{
                //     captcha.value = `${res.responseBody}`
                // })
                // request.get("/code/login/getSysManageLoginCode",{responseType:'blob'}).then(res=>{
                //    captcha.value=window.URL.createObjectURL(res)
                // })
                await request.get("/code/getVerify",{responseType:'blob'}).then(res=>{
                    captcha.value=window.URL.createObjectURL(res)
                })
            }
           async function toGetCaptcha() {
                randomCode.value = new Date().getTime()
                const data = {
                //     // userName: username.value ? jwt.encryptAES(username.value) : jwt.encryptAES('1234'),
                    randomCode: randomCode.value
                }
                // console.log(data)
               await request.get("/code/getVerify",{responseType:'blob'}).then(res=>{
                   captcha.value=window.URL.createObjectURL(res)
               })
               // const result=await getCaptcha()
               // captcha.value=`${result.responseBody}`
               // console.log(result.responseBody)
                // console.log(result)
                // base64转图片

            }
            function countdown(){
                var count = 60

                function setTime () {
                    if (count ===0) {
                        clearTimeout(timer)
                        disabled.value = false
                        btnText.value = '发送验证码'
                        count = 60
                        return;
                    } else {
                        disabled.value = true
                        btnText.value = '重新发送(' + count + ')'
                        count--

                    }
                    timer = setTimeout(() => {
                        setTime()
                    }, 1000)
                }

                setTime()
            }
            function getRandom(n){

                    var num = '';
                    for (var i = 0; i < n; i++) {
                        num += Math.floor(Math.random() * 10);
                    }
                    return num;

            }
            // const checkFocus=()=>{
            //     request.get('/code/checkCaptcha?code='+code.value).then(res=>{
            //                 // document.getElementById("reCheckCode").setAttribute("value",res)
            //                 if(res.code==='200'){
            //                     Toast.clear();
            //                     router.push({path:'/index'})
            //                 }else{
            //                     Toast.fail({message:res.msg})
            //                 }
            //   })
            // }
            // 校验函数可以返回 Promise，实现异步校验
            const asyncValidator = (val) =>
                new Promise((resolve) => {
                    // if(!val){
                        Toast.loading('验证中...')
                        // if(code.value!==""){
                        //     request.get('/code/checkCaptcha?code='+code.value).then(res=>{
                        //         // console.log(res)
                        //         if(res.code==='200'){
                        //             Toast.clear();
                        //             router.push({path:'/index'})
                        //             Toast.success('登录成功!')
                        //             clearTimeout(times)
                        //         }else{
                        //             Toast.fail({message:res.msg})
                        //             // code.value = ''
                        //             // times=setTimeout(()=>{
                        //             //     onRefreshCode()
                        //             // },3000)
                        //         }
                        //     })
                        // }
                        // if(code.value!=null){
                        //     resolve(
                        //     request.get('/code/checkCaptcha?code='+code.value).then(res=>{
                        //         if(res.code==='200'){
                        //             Toast.clear();
                        //             router.push({path:'/index'})
                        //             Toast.success('登录成功!')
                        //         }else{
                        //             Toast.fail({message:res.msg})
                        //             code.value = ''
                        //             const times=setTimeout(()=>{
                        //                 onRefreshCode()
                        //             },3000)
                        //         }
                        //     }))
                        // }

                    if(Code.inputStr!==""){
                            request.post('/code/checkVerify',Code).then(res=>{
                                    if(res.code==='200'){
                                        Toast.clear();
                                        Toast.success('验证码正确!')
                                        onSubmit()
                                    }else{
                                        Toast.fail({message:res.msg})
                                    }
                                }
                            )
                    }

                });
            return{
                username,
                password,
                Code,
                isShowLogin,
                isRememberPassword,
                checked,
                captcha,
                pattern,
                onChecked,
                toggleLoginType,
                btnText,
                disabled,
                getCode,
                onFailed,
                onSubmit,
                onPrivacy,
                timer,
                times,
                phoneInfo,
                errorMobilePhone,
                phoneInfoLogin,
                onRefreshCode,
                toGetCaptcha,
                randomCode,
                requestCodeServer,
                getRandom,
                toggleLoginErWeiMa,
                isShowLoginWx,
                // checkFocus,
                asyncValidator,
                // getVerifyCode

            }
        },
    })
</script>
<style lang="scss" >
    .login{
        width: 100%;
        height: 100%;
        background-color: #fff;
        flex-direction: column;
        align-items: center;
        padding-top: 60px;
        display: flex;
    }
    .login-form{
        width: 86%;
        .jw-van-cell{
            align-items: center;
            font-size:15px;
            padding-right: 0;


        }
        .van-field__label{
            width: 58px;
            margin-right: 0!important;
        }

    }

    .login-container{
        width: 100%;
        height: 204px;
        background: url(../assets/logo.png) no-repeat center;
        background-size: 144px 99px;
    }
    .privacy-policy-link {
        margin-left: 6px;
        color: #1989fa;
    }
    .is-remember-password {
        margin-top: 10px;
        display: flex;
        justify-content: flex-start;
        align-items: center;
        font-size: 14px;
        padding-left: 14px;

        .tick {
            display: flex;
            justify-content: center;
            align-items: center;
            width: 20px;
            height: 20px;
            border: 1px solid #e3e3e3;
            border-radius: 50%;
            margin-right: 8px;
            cursor: pointer;

            .icon {
                visibility: hidden;
            }

            &.is-active {
                border: 1px solid #1989fa;

                .icon {
                    visibility: visible;
                }
            }
        }

        .desc {
            margin-left: 4px;
        }
        .captcha-show{
            width: 100px;
        }
    }
</style>
