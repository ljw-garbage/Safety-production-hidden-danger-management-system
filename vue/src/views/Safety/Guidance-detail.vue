<template>
    <div class="container">
        <div class="title">
            <h3 style="margin: 0;padding-bottom: 10px;">{{title}}</h3>
            <div class="date" style="color: #888;font-size: 13px">{{date}}</div>
        </div>

        <div class="content" style="margin-top: 10px;color: #404040;">
            <div>
                <p style="color: #404040;font-size:16px;line-height: 25px;word-break: break-all;">{{content}}</p>
            </div>
        </div>

    </div>
</template>

<script>
    import {ref} from "vue";
    import {useRoute} from "vue-router";
    import request from "../../api/axios.config";
    import {Notify} from "vant";

    export default {
        name: "Guidance-detail",
        setup(){
            const title=ref('')
            const content=ref('')
            const date=ref('')

            const route=useRoute()
            const id=ref(route.query.id)
            // console.log(id.value)
            const getDetail=()=>{
                request.get("/guidance/"+id.value).then(res=>{
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