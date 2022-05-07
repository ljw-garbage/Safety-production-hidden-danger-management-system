<template>
    <div id="top">
        <h3 class="header">安全生产隐患信息填报</h3>
        <div class="user" @click="onEditUserInfo">
            <van-icon name="https://b.yzcdn.cn/vant/icon-demo-1126.png" size="35" color="#fff" />
        </div>
    </div>

</template>

<script>
    import {useRouter} from 'vue-router'
    export default {
        name: "comm-header",
        setup(){
            const router=useRouter()
            //修改用户界面
            const onEditUserInfo=()=>{
                router.push({path:'/userInfo'})
            }
            return {
               onEditUserInfo
            }
        }
    }
</script>

<style lang="css" scoped>
    #top{
        position: fixed;
        z-index: 100;
    }
    .header{
        background-color: #1989fa;
        color: #fff;
        padding: 0 0.26667rem;
        line-height: 2.2rem;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        text-align: center;
        font-weight: 400;
        width: 100vw;
        margin: 0 auto;
    }
    .user{
        float: right;
        padding: 0 10px;
        color: #FFFFFF;
        position: absolute;
        top: 0;
        right: 0;
    }
</style>
