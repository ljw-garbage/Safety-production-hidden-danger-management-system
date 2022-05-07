<template>
    <el-card style="width: 500px;">

        <el-form v-bind:model="form"  label-width="70px" size="small">
            <el-upload
                    class="avatar-uploader"
                    action="http://localhost:8088/file/upload"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess">
                <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <el-form-item label="用户名" prop="username">
                <el-input  v-model="form.username" placeholder="请输入用户名" disabled></el-input>
            </el-form-item>
            <el-form-item label="账号名" prop="account">
                <el-input  v-model="form.account" placeholder="请输入账号名"></el-input>
            </el-form-item>
            <el-form-item label="角色" prop="role">
                <el-input  v-model="form.role" placeholder="请输入角色" disabled></el-input>
            </el-form-item>
            <el-form-item label="负责区域" prop="territory">
                <el-input v-model="form.territory" placeholder="请输入负责区域" type="textarea"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
            </el-form-item>
            <el-form-item label="头像地址" prop="avatarUrl">
                <el-input v-model="form.avatarUrl" placeholder="请输入头像地址" type="textarea"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="save">确 定</el-button>
            </el-form-item>
        </el-form>

    </el-card>
</template>

<script>
    export default {
        name: "Person",
        data(){
            return{
               form:{},
                userInfo:localStorage.getItem("userInfo")?JSON.parse(localStorage.getItem("userInfo")):{}
            }
        },
        created() {
            this.getUser().then(res=>{
                this.form=res
            })
        },
        methods:{
          async  getUser(){
               return  (await this.request.get("/user/username/"+this.userInfo.username)).data
              // const res=await this.request.get("/user/username/"+this.userInfo.username)
              // if(res.code==='200'){
              //     this.form=res.data
              // }
            },

            save(){
                    this.request.post("/user",this.form).then(res=>{
                        if(res.code!=='200'){
                            this.$message.error('用户添加失败!')
                        }else{
                            this.$emit("refreshUser")
                            //更新浏览器里的信息
                            this.getUser().then(res=>{
                               res.token= JSON.parse(localStorage.getItem("userInfo")).token
                                localStorage.setItem("userInfo",JSON.stringify(res))
                            })
                            // this.form=res
                            // let tokenId=localStorage.getItem("userInfo").token
                            // localStorage.setItem("userInfo",JSON.stringify(tokenId))
                            // this.userInfo=localStorage.setItem("userInfo",JSON.stringify(res.data))
                            // window.location.reload()
                            this.$message.success('用户添加或修改成功!')

                        }
                })
            },
            handleAvatarSuccess(res) {
                this.form.avatarUrl = res
            }
        }
    }
</script>

<style scoped>
    .avatar-uploader{
        text-align:center;
        padding-bottom: 10px;
    }
    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 138px;
        height: 138px;
        line-height: 138px;
        text-align: center;
    }
    .avatar {
        width: 138px;
        height: 138px;
        display: block;
    }
</style>