<template>
    <div class="container">
        <div class="main">
            <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
                <van-list
                        v-model="loading"
                        :finished="finished"
                        @load="onLoad"
                >
                    <ul  class="pending-project-list">
                        <li v-for="(item,index) in list" :key="index" :title="item" class="newItem">
                            <div class="title">
                                <span></span>
                                {{item.title}}
                            </div>
                            <div class="content van-multi-ellipsis--l2 ">
                                {{item.content}}
                            </div>
                            <div class="ft">
                                <div class="date">
                                    <van-tag plain type="danger" v-if="index==0?!isTop:isTop">置顶</van-tag>
                                    <span>{{item.date}}</span>
                                    <span class="detail" @click="onDetail(index+1)">查看详情></span>
                                </div>

                            </div>
                        </li>
                    </ul>
                </van-list>
            </van-pull-refresh>
        </div>
    </div>
</template>

<script>
    import {computed, ref} from "vue";
    import {useRouter,useRoute} from "vue-router";
    import {getOneDetails} from "../../api/api-list";
    import emitter from "../../common/js/mitt";
    import request from "../../api/axios.config";

    export default {
        name: "ImportNotice",
        setup(){
            const isTop=computed(()=>{
                return top.value?true:false
            })
            const route=useRoute()
            const router=useRouter()
            const id=ref(route.query.id)
            const list=ref({})
            const refreshing=ref(false)
            const loading=ref(false)
            const finished=ref(false)
            const onLoad=()=>{
                // setTimeout(() => {
                //     list.value.push(list.value.length);
                //
                //     // 加载状态结束
                //     loading.value = false;
                //
                //     // 数据全部加载完成
                //     if (list.value.length =1) {
                //         finished.value = true;
                //     }
                // }, 1000);
                request.get("/import-notice").then(res=>{
                    // console.log(res.data)
                    list.value=res.data
                })
                loading.value=false
                finished.value=true
            }
            const onRefresh=()=>{
                // 清空列表数据
                finished.value = false;
                // 重新加载数据
                // 将 loading 设置为 true，表示处于加载状态
                loading.value = true;
                onLoad();
            }
            let DetailData=ref('')
            let str=ref('')
            function onDetail(id){
                // getOneDetails(id).then(res=>{
                //     // console.log(res.data)
                //     DetailData=localStorage.setItem("Detail",JSON.stringify(res.data))
                //     //传数据
                //     emitter.emit('mittFn',res.data)
                // });
                // str.value=JSON.stringify(list.value).replace(/\[|]/g,"")

                // listData=localStorage.setItem("list",JSON.stringify(str.value))
                //
                // emitter.emit('mittFn',listData)
                router.push({
                    path: "/importantNotice-detail",
                    query: {
                        id: id
                    }
                })
            }
            return{
                onDetail,
                isTop,
                list,
                loading,
                finished,
                refreshing,
                onLoad,
                onRefresh,
                DetailData,
                str,
                id
            }
        }
    }
</script>

<style lang="scss" scoped>
    .container{
        padding-top: 10px;
        background-color: #f5f5f9;
        .main{
            padding: 0 13px;
            .van-cell__title {
                color: #d9001b;
                font-size: 16px;
            }
            .detail{
                color: #8093be;
                font-size: .32rem;
                font-weight: 700;
                float: right;
            }
            .date{
                font-size: .32rem;
                line-height: .42667rem;
                font-family: Source Han Sans CN;
                color: #999;
            }
            .newItem{
                letter-spacing: .02667rem;
                padding: 10px 10px 0;
                border-bottom: 0.02667rem solid #dfdfe2;
            }
            .title{
                font-size: 17px;
                font-weight: 500;
                color: #333;
                height: 20px;
                line-height: 20px;
                width: 100%;
                white-space: nowrap;
                margin-bottom: 5px;
                text-overflow: ellipsis;
                overflow: hidden;
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
            .ft{
                height: 18px;
                padding-left: 9px;
                margin-bottom: 10px;
            }
        }
    }
</style>
