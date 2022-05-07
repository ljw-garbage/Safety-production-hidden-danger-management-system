<template>
    <div  style=" display: flex; line-height: 60px; " class="pointer"  >
        <div style="flex: 1; ">
            <span :class="collapseBtnClass" style="cursor: pointer; font-size: 18px" @click="collapse"></span>
            <el-breadcrumb separator="/" style="display: inline-block; margin-left: 5px">
                <el-breadcrumb-item :to="{ path: '/Welcome' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>{{currentPathName}}</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-dropdown  style="cursor: pointer">
            <div style="display: inline-block">
<!--                <img :src="userInfo.avatarUrl" alt="">-->
                <el-avatar :src="userInfo.avatarUrl" size="small" style="position:relative; top: 6px;right: 5px" alt=""></el-avatar>
                <span>{{userInfo.account}}</span>
                <i class="el-icon-arrow-down" style="margin-right: 15px"></i>
            </div>

            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item style="font-size: 14px; " >
                    <router-link to="/person" style="text-decoration: none" class="hover">个人信息</router-link>
                </el-dropdown-item>
                <el-dropdown-item style="font-size: 14px;" >
                    <router-link to="/editPassword" style="text-decoration: none;">修改密码</router-link>
                </el-dropdown-item>
                <span style="text-decoration: none" @click="logout">
                     <el-dropdown-item style="font-size: 14px; " >
                        退出
                     </el-dropdown-item>
                </span>

            </el-dropdown-menu>
        </el-dropdown>
    </div>

</template>

<script>
    import {resetRouter} from "../router";

    export default {
        name: "Header",
        data(){
          return{
              // userInfo:localStorage.getItem("userInfo")?JSON.parse(localStorage.getItem("userInfo")):{}
          }
        },
        props:{
            collapseBtnClass:String,
            userInfo:Object
        },
        computed:{
          currentPathName(){
              return this.$store.state.currentPathName; //需要监听的数据
          }
        },
        watch:{
            currentPathName(newValue){
                console.log(newValue)
            }
        },
        methods:{
            collapse(){
                this.$emit('asideCollapse')
            },
            logout(){
                localStorage.clear();
                this.$message.success("安全退出成功")
                this.$router.push({path:"/login"})
                //重置路由
                resetRouter()

            }
        }
    }
</script>

<style scoped>
    .pointer:before{
        cursor: pointer;
    }
    a{
        color: #606266;
    }
    a:hover{
        color: #66b1ff;
    }
</style>