<template>
    <div class="container">
        <div class="title">
            <h3 style="margin: 0;padding-bottom: 10px;">{{title}}</h3>
            <div class="date" style="color: #888;font-size: 13px">{{date}}</div>
        </div>

        <div class="content" style="margin-top: 10px;color: #404040;">
            <div v-if="id===1">
                <video style="width: 365px; height: 400px" src="https://qnfile.henan.gov.cn/video/20210602/82c67bff-9d91-45e9-a7d5-4aef49c6ac50.mp4"  controls ></video>
            </div>
            <div v-if="id!==1"><p style="color: #404040;font-size:16px;line-height: 25px;word-break: break-all;">{{content}}</p></div>
        </div>

    </div>
</template>

<script>
    import {ref,reactive,watch,nextTick,toRefs,onUnmounted,onMounted} from 'vue'
    import emitter from "../../common/js/mitt";
    import {Notify} from "vant";
    import {useRouter,useRoute} from "vue-router";
    import request from "../../api/axios.config";
    export default ({
        name: "Details",
        // props:{id:Number},
        setup(){
            const title=ref('')
            const content=ref('')
            const date=ref('')

            const route=useRoute()
            const id=ref(0)
            // console.log(id.value)
            const getDetail=()=>{
                //兄弟之间传参
                emitter.on("mittFn",(res)=>{
                    if(res){
                        // debugger
                        title.value=res.title
                        if(res.title==null){
                            Notify({ type: 'danger', message:'数据加载失败' });
                        }
                        content.value=res.content
                        date.value=res.date
                        id.value=res.id
                    }else{
                        Notify({ type: 'danger', message:'数据加载失败请重新登录' });
                        // router.push({path: '/login'})
                    }

                })
                // request.get("/details/"+id.value).then(res=>{
                //     console.log(res)
                //         // if(res){
                //         //     // debugger
                //         //     title.value=res.title
                //         //     if(res.title==null){
                //         //         Notify({ type: 'danger', message:'数据加载失败' });
                //         //     }
                //         //     content.value=res.content
                //         //     date.value=res.date
                //         //     id.value=res.id
                //         // }else{
                //         //     Notify({ type: 'danger', message:'数据加载失败请重新登录' });
                //         //     // router.push({path: '/login'})
                //         // }
                // })
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
    })
</script>

<style lang="scss" scoped>
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
