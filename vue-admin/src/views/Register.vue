<template>
    <div class="login_container">

        <div class="login_box">
            <!--      头像区域-->
            <div style="margin:20px 0;text-align: center;font-size: 24px"><b>注册</b></div>
            <!--    表单区域  -->
            <el-form ref="LoginFormRef" label-width="80px" class="login_form" :model="LoginForm" :rules="LoginFormRules">
                <el-form-item label="用户名" prop="username">
                    <el-input prefix-icon="el-icon-user"  v-model="LoginForm.username"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input prefix-icon="el-icon-lock"  v-model="LoginForm.password" show-password></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                    <el-input prefix-icon="el-icon-lock"  v-model="LoginForm.confirmPassword" show-password></el-input>
                </el-form-item>
                <el-form class="btns">
                    <el-button type="primary" @click="register">注册</el-button>
                    <el-button type="warning" autocomplete="off" @click="$router.push({path:'/login'})">返回登录</el-button>
                </el-form>
            </el-form>

        </div>



    </div>
</template>

<script>
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
                    ],
                    confirmPassword:[
                        { required: true, message: '请再次输入密码', trigger: 'blur' },
                        { min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur' }
                    ]

                }
            }
        },
        methods:{
            reset(){
                this.$refs.LoginFormRef.resetFields();
            },
            register(){
                this.$refs.LoginFormRef.validate(async (valid)=>{
                    if(!valid)return;
                    if(this.LoginForm.password!==this.LoginForm.confirmPassword){
                        this.$message.error("两次输入的密码不正确");
                        return false;
                    }
                    await this.request.post("/user/register", this.LoginForm).then(res=>{
                        if (res.code!=='200')return this.$message.error(res.msg);
                        this.$message.success('注册成功!')
                    })
                });
            }
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
        height: 400px;
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
        justify-content: right;
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
</style>
