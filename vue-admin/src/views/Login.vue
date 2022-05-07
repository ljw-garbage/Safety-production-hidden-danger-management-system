<template>
    <div class="login_container">

        <div class="login_box">
            <!--      头像区域-->
<!--            <div class="avatar_box">-->
<!--                <img src="../assets/logo.png" alt="">-->
<!--            </div>-->
            <h2 class="h3">安全生产隐患后台管理系统</h2>
            <!--    表单区域  -->
            <el-form ref="LoginFormRef" label-width="80px" class="login_form" :model="LoginForm" :rules="LoginFormRules">
                <el-form-item label="用户名" prop="username">
                    <el-input prefix-icon="el-icon-user"  v-model="LoginForm.username"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input prefix-icon="el-icon-lock"  v-model="LoginForm.password" show-password></el-input>
                </el-form-item>
                <el-form class="btns">
                    <el-button type="primary" @click="login" >登录</el-button>
                    <el-button type="warning"  autocomplete="off" @click="$router.push({path:'/register'})">注册</el-button>
                    <el-button type="info" @click="reset">重置</el-button>
                </el-form>
            </el-form>

        </div>



    </div>
</template>

<script>
    import {setRoutes} from "../router";

    export default {
        name: "Login_vue",
        data(){
            return{
                //表单数据绑定事件
                LoginForm:{},
                //这是表单验证规则对象
                LoginFormRules:{
                    username:[
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
                    ],
                    password:[
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur' }
                    ]

                }
            }
        },
        created() {
            // let that = this;
            // document.onkeydown =function(e){
            //     e = window.event || e;
            //     if(that.$route.path=='/login'&&(e.code=='Enter'||e.code=='enter')){//验证在登录界面和按得键是回车键enter
            //         that.submitForm('login');//登录函数
            //     }
            // }
        },
        methods:{
            reset(){
                this.$refs.LoginFormRef.resetFields();
            },
            login(){
                this.$refs.LoginFormRef.validate(async (valid)=>{
                    if(!valid)return;
                    await this.request.post("/user/login", this.LoginForm).then(res=>{
                        if (res.code!=='200')return this.$message.error(res.msg);
                        this.$message.success('登录成功!')
                        // 1.登录成功之后token,保存到客户端sessionStorage
                        //1.1项目中出了登录之外的其他API接口,必须在登录之后才能访问
                        //1.2token 只应在当前网站打开期间生效,所以将token保存在sessionStorage中
                        // window.sessionStorage.setItem("token",res.token);
                        localStorage.setItem("userInfo",JSON.stringify(res.data))
                        localStorage.setItem("menus",JSON.stringify(res.data.menus))
                        setRoutes()
                        //2.通过编程式导航跳转到后台主页,路由地址是/home
                        this.$router.push({path:'/Manage'});
                    })
                });
                // this.$refs.LoginFormRef.validate((valid) => {
                //     if (valid) {  // 表单校验合法
                //         this.request.post("/user/login", this.LoginForm).then(res => {
                //             if(!res) {
                //                 this.$message.error("用户名或密码错误")
                //             } else {
                //                 this.$message.success("登录成功");
                //                 this.$router.push({path:'/Manage'});
                //             }
                //         })
                //     } else {
                //         return false;
                //     }
                // });

            },
            keyDown(e){
                if(e.keyCode===13){
                    this.login()
                }
            }
        },
        mounted() {
            window.addEventListener('keydown',this.keyDown)
        },
        destroyed() {
            window.removeEventListener('keydown',this.keyDown,false)
        }
    }

</script>

<style lang="less" scoped>
    .login_container {
        background-image: linear-gradient(to bottom right,#FC466B, #3F5EFB)!important;
        overflow:hidden;
        height: 100%;
    }

    .login_box {
        height: 380px;
        background-color: #fff;
        width: 400px;
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        border-radius: 3px;
    }

    .avatar_box {
        height: 130px;
        width: 130px;
        border: 1px solid #ccc;
        border-radius: 50%;
        padding: 10px;
        box-shadow: 0 0 10px #ddd;
        position: absolute;
        left: 50%;
        transform: translate(-50%, -50%);
        background-color: #fff;

        img {
            width: 100%;
            height: 100%;
            border-radius: 50%;
            background-color: #eee;
        }
    }
    .btns{
        display: flex;
        justify-content: center;

    }
    .login_form{
        position: absolute;
        bottom: 30%;
        width: 100%;
        padding: 0 20px;
        box-sizing: border-box;

    }
    .login_box{
        border-radius: 10px;
    }
    .h3{
        text-align: center;

    }
</style>
