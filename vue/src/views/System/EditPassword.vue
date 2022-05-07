<template>
    <div id="edit-password-container">
        <van-nav-bar
                left-text="返回"
                left-arrow
                @click-left="onClickLeft"
        />
        <div id="main">
            <h3>修改密码</h3>
            <van-form @submit="onSubmit">
                <van-cell-group inset>
                    <van-field
                            v-model="loginInfo.password"
                            type="password"
                            name="原密码"
                            label="原密码"
                            placeholder="请输入原来密码"
                            :rules="[{ required: true, message: '请填写原来密码' }]"
                    />

                    <van-cell center>
                        <van-field
                                v-model="loginInfo.newPassword"
                                class="van-field-new"
                                label="新密码"
                                name="新密码"
                                placeholder="请输入新密码"
                                :type="passwordType"
                                :rules="[{ required: true, message: '请填写新密码' }]"
                        >
                        <template #right-icon>
                            <van-switch v-model="checked"  @click="onIsShowPassword" size="24" />
                        </template>
                        </van-field>
                    </van-cell>

                        <van-field
                                v-model="loginInfo.confirmPassword"
                                type="password"
                                name="确认新密码"
                                label="确认新密码"
                                placeholder="请输入新密码"
                                :rules="[{ required: true, message: '请确认新密码' }]"
                        />
                </van-cell-group>
                <div style="margin: 16px;">
                    <van-button round block type="primary" native-type="submit">
                        确认
                    </van-button>
                </div>
            </van-form>
        </div>
    </div>
</template>

<script>
    import {ref,reactive,nextTick,onMounted} from 'vue'
    import request from "../../api/axios.config";
    import {Toast} from "vant";
    import {useRouter} from "vue-router";
    export default ({
        name: "EditPassword",
        setup(){
            const onClickLeft=()=>history.back()
            const router=useRouter()

            const onSubmit=()=>{
                if (loginInfo.newPassword !== loginInfo.confirmPassword) {
                    Toast.fail("2次输入的新密码不相同")
                    return false
                }
                request.post("/user/password",loginInfo).then(res=>{
                     if(res.code==='200'){
                         router.push({path:'/login'})
                         Toast.success('修改成功!')
                         localStorage.clear()
                     }else{
                         Toast.fail(res.msg)
                     }
                })
                console.log(loginInfo)
            }
            const loginInfo=reactive({
                username:localStorage.getItem("username"),
                password:'5tgb@WSX',
                newPassword:'5TGB2wsx',
                confirmPassword:'5TGB2wsx',
            })
            const passwordType=ref('password')
            const onIsShowPassword=()=>{
                if(checked){
                    passwordType.value = passwordType.value === 'password' ? 'text' : 'password'
                }
            }
            const checked=ref(false)
            return{
                onClickLeft,
                loginInfo,
                onSubmit,
                checked,
                onIsShowPassword,
                passwordType,
            }
        }
    })
</script>

<style lang="scss" scoped>
    #edit-password-container{
        background-color: #eee;
        text-align: center;
    }
    #main{
        .van-field-new{
            padding-left: 0;
        }
    }
</style>
