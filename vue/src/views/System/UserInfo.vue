<template>
    <div class="user-container">
        <van-nav-bar
                left-text="返回"
                left-arrow
                @click-left="onClickLeft"
                right-text="修改密码"
                right-arrow
                @click-right="onClickRight"
        />
        <div id="main">
            <h3>个人信息</h3>
                <van-form >
                    <van-row type="flex" align="center" class="jw-van-row">
                        <van-col span="5">
                            <span class="van-span">账号名:</span>
                        </van-col>
                        <van-col span="16">
                            <van-field v-model="account" readonly />
                        </van-col>
                    </van-row>
                    <van-row type="flex" align="center" class="jw-van-row">
                        <van-col span="5">
                            <span class="van-span">用户名:</span>
                        </van-col>
                        <van-col span="16">
                            <van-field v-model="username" readonly />
                        </van-col>
                    </van-row>
                    <van-row type="flex" align="center" class="jw-van-row">
                        <van-col span="5">
                            <span class="van-span">负责区域:</span>
                        </van-col>
                        <van-col span="16">
                            <van-field v-model="territory" disabled />
                        </van-col>
                    </van-row>
                    <van-row type="flex" align="center" class="jw-van-row">
                        <van-col span="5">
                            <span class="van-span">修改时间:</span>
                        </van-col>
                        <van-col span="16">
                            <van-field v-model="assessTime" disabled />
                        </van-col>
                    </van-row>
                </van-form>

                <div style="margin: 16px;">
                    <van-button type="primary" block @click="onSubmit">提交</van-button>
                </div>
                <div style="margin: 16px;">
                    <van-button type="primary" block @click="onReturn">退出登录</van-button>
                </div>
        </div>
    </div>
</template>

<script>
    import {useRouter} from 'vue-router'
    import {ref,reactive} from 'vue'
    import {Notify} from 'vant'
    import moment from 'moment'
    import request from "../../api/axios.config";
    export default {
        name: "UserInfo",
        setup(){
            const router=useRouter()
            const username=ref(localStorage.getItem("username"))
            const assessTime=ref(moment(new Date()).format('YYYY-MM-DD h:mm:ss'))

            const loginInfo=ref(JSON.parse(localStorage.getItem("loginInfo")))
            const account=ref(loginInfo.value.account)
            //负责区域
            const territory=ref(loginInfo.value.territory)
            const onClickLeft=()=>history.back()
            const onClickRight=()=> {
                router.push({path: '/edit-password'})
            }
            const onSubmit=()=>{
                request.post("/user",{username:username.value,account:account.value,territory:territory.value}).then(res=>{
                    if(!res.data){
                        this.$message.error('用户添加失败!')
                    }else{
                        // console.log(this.form)
                        localStorage.setItem("loginInfo",JSON.stringify({username:username.value,account:account.value,territory:territory.value}))
                        // this.userInfo=localStorage.setItem("userInfo",JSON.stringify(res.data))
                        location.reload();
                        this.$message.success('用户添加或修改成功!')

                    }
                })
            }


            const onReturn=()=>{
                //调用退出接口
                Notify({
                    type:'success',
                    message:'退出成功'})
               localStorage.clear()
                router.push({path:'/login'})
            }
            //获取用户信息列表接口
           // const getUserInfoList=()=>{
           //      // request.get("/user/username/"+username.value).then(res=>{
           //      //     // console.log(username.value)
           //      //     if(res.code==='200'){
           //      //         console.log(res.data)
           //      //         username.value=res.data.username
           //      //         account.value=res.data.account
           //      //         territory.value=res.data.territory
           //      //     }
           //      // })
           //      request.get("/user/username/"+loginInfo.value.username).then(res=>{
           //          // console.log(res.data)
           //      })
           //  }
           //  getUserInfoList()

            return{
                onClickLeft,
                onClickRight,
                onSubmit,
                onReturn,
                // getUserInfoList,
                username,
                account,
                territory,
                assessTime,
                loginInfo

            }
        }
    }
</script>

<style lang="scss" scoped>
    .user-container{
        background-color: #eee;
        #main{
            text-align: center;
            .jw-van-row{
               padding-top: 10px;
            }
            .van-span{
                color: #1989fa;

            }
        }
    }
</style>
