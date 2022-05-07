<template>
    <div class="container">
        <div class="title">
            <h3 style="margin: 0;padding-bottom: 10px;">{{title}}</h3>
            <div class="date" style="color: #888;font-size: 13px">{{date}}</div>
        </div>

        <div class="content" style="margin-top: 10px;color: #404040;">
            <div>
                <p style="color: #404040;font-size:16px;line-height: 25px;word-break: break-all;">
                    {{content}}
                </p>
                <div v-if="id==1" style="background-color:#f5f5f9;">
                    <van-image width="300px" height="500px" src="http://localhost:8088/file/5ca40b0901f5483da03bcd652e94cd14.png">
                        <template v-slot:error>加载失败</template>
                    </van-image>
                    <p style="color: #404040;font-size:16px;line-height: 25px;word-break: break-all;">(2) 在进入 "信息上报"操作时,系统会测验用户是否登录。如果的登录已过期,或者没有的登录。用户需要重新登录。</p>
                    <van-image width="300px" height="500px" src="http://localhost:8088/file/93c214ec7b9b4728af89b30b5050f51b.png">
                        <template v-slot:error>加载失败</template>
                    </van-image>
                    <p style="color: #404040;font-size:16px;line-height: 25px;word-break: break-all;">
                       (3)登录成功后,直接进入数据上报页面
                    </p>
                    <van-image  src="http://localhost:8088/file/18844e8d11494adcbd31e278c86da6ba.png">
                        <template v-slot:error>加载失败</template>
                    </van-image>
                </div>
                <div v-if="id==2" style="background-color:#f5f5f9;">
                    <van-image width="300px" height="500px" src="http://localhost:8088/file/5ca40b0901f5483da03bcd652e94cd14.png">
                        <template v-slot:error>加载失败</template>
                    </van-image>
                    <p style="color: #404040;font-size:16px;line-height: 25px;word-break: break-all;">(2) 在进入 "信息上报"操作时,系统会测验用户是否登录。如果的登录已过期,或者没有的登录。用户需要重新登录。</p>
                    <van-image width="300px" height="500px" src="http://localhost:8088/file/93c214ec7b9b4728af89b30b5050f51b.png">
                        <template v-slot:error>加载失败</template>
                    </van-image>
                    <p style="color: #404040;font-size:16px;line-height: 25px;word-break: break-all;">
                        (3)登录成功后,直接点击历史记录
                    </p>
                    <van-image width="300px" height="500px" src="http://localhost:8088/file/92d607a6efc24810a42f370ca6e35fd7.png">
                        <template v-slot:error>加载失败</template>
                    </van-image>
                    <p style="color: #404040;font-size:16px;line-height: 25px;word-break: break-all;">
                        (4)在每条记录位置手指向左滑动,点击编辑按钮
                    </p>
                    <van-image width="300px" height="500px" src="http://localhost:8088/file/40d5a7e2b5874253b6b8b549cab0e82a.png">
                        <template v-slot:error>加载失败</template>
                    </van-image>
                </div>
                <div v-if="id==3" style="background-color:#f5f5f9;">
                    <van-image width="300px" height="500px" src="http://localhost:8088/file/afc7cc12cfe346258d56dd98647b467a.png">
                        <template v-slot:error>加载失败</template>
                    </van-image>
                </div>
                <div v-if="id==4" style="background-color:#f5f5f9;">
                    <van-image width="300px" height="500px" src="http://localhost:8088/file/92d607a6efc24810a42f370ca6e35fd7.png">
                        <template v-slot:error>加载失败</template>
                    </van-image>
                </div>
                <div v-if="id==5" style="background-color:#f5f5f9;">
                    <van-image width="300px" height="500px" src="http://localhost:8088/file/88109b0cfc554bf6a154e4ae6abd3040.png">
                        <template v-slot:error>加载失败</template>
                    </van-image>
                    <van-image width="300px" height="500px" src="http://localhost:8088/file/2a8d8b480e974d0cbeb314ce86c16876.png">
                        <template v-slot:error>加载失败</template>
                    </van-image>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
    import {ref} from "vue";
    import {useRoute} from "vue-router";
    import emitter from "../../common/js/mitt";
    import {Notify} from "vant";
    import request from "../../api/axios.config";
    export default {
        name: "Instructions-help-detail",
        setup(){
            const title=ref('')
            const content=ref('')
            const date=ref('')

            const route=useRoute()
            const id=ref(route.query.id)
            // console.log(id.value)
            const getDetail=()=>{
                //兄弟之间传参
                // emitter.on("mittFn",(res)=>{
                //     if(res){
                //         // debugger
                //         title.value=res.title
                //         if(res.title==null){
                //             Notify({ type: 'danger', message:'数据加载失败' });
                //         }
                //         content.value=res.content
                //         date.value=res.date
                //         id.value=res.id
                //     }else{
                //         Notify({ type: 'danger', message:'数据加载失败请重新登录' });
                //         // router.push({path: '/login'})
                //     }
                //
                // })
                request.get("/instructions-help/"+id.value).then(res=>{
                        if(res.data){
                            // debugger
                            title.value=res.data.title
                            if(res.data.title==null){
                                Notify({ type: 'danger', message:'数据加载失败' });
                            }
                            content.value=res.data.content
                            date.value=res.data.date
                        }else{
                            Notify({ type: 'danger', message:'数据加载失败请重新登录' });
                            // router.push({path: '/login'})
                        }
                })
            }
            getDetail()
            return{
                getDetail,
                // Data,
                title,
                content,
                date,
                id,
            }
        }
    }
</script>

<style lang="less" scoped>
    .container{
        background-color: #f5f5f9;
        height: 100vh;
    .date{
        font-size: .32rem;
        line-height: .42667rem;
        font-family: Source Han Sans CN;
        color: #888;
    }
    .title{
        font-weight: 400;
        color: #333;
        padding: 20px 5px 0 5px;
    }
    .title span{
        display: inline-block;
        position: relative;
        top: 0;
        margin-right: 0.08rem;
        border-radius: 50%;
        width: 0.5rem;
        height: 0.5rem;
        background-color: #4773d7;
        margin-bottom: 5px;
    }
    .content{
        color: #666;
        margin-bottom: 5px;
        padding-left:5px ;
        font-size: 14px;
    }

    }
</style>