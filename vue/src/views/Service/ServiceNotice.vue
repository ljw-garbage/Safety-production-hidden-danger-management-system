<template>
    <div class="container">
        <div class="footer">
            <div class="main">
                <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
                    <van-list
                            v-model="loading"
                            :finished="finished"
                            finished-text="没有更多了"
                            @load="onLoad"
                    >
                        <ul  class="pending-project-list">
                            <li v-for="(item,index) in list.slice(0,10)" :key="index+1" :title="item" class="newItem">
                                <div class="title van-ellipsis" >
                                    <span></span>
                                    {{item.title}}
                                </div>
                                <div class="content van-ellipsis">
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
    </div>

</template>

<script>
    import {computed, reactive, ref} from "vue";
    import {useRouter} from "vue-router";
    import {getOneDetails, showDetails, showImages} from "../../api/api-list";
    import emitter from "../../common/js/mitt";

    export default {
        name: "ServiceNotice",
        setup(){
            const router=useRouter()
            const isTop=computed(()=>{
                return top.value?true:false
            })
            const images=[
                require('../../assets/images/1.jpg'),
                require('../../assets/images/2.jpg'),
                require('../../assets/images/3.jpg'),
                require('../../assets/images/4.jpeg'),
                require('../../assets/images/5.jpeg'),
                require('../../assets/images/6.jpg'),
            ];
            let routerList=reactive({
                list: []
            })
            const list=ref([])
            const iconTexts=ref('')
            const iconUrls=ref([])
            const Path=ref('')
            const menuList=reactive([[{}]])
            const refreshing=ref(false)
            const loading=ref(false)
            const finished=ref(false)
            const top=ref(false)
            const money=ref(100)
            const onImageLoading=async ()=>{

                const res=await showImages()
                console.log(res)
                if(res.code==='200'){
                    routerList.list=res.data
                }
            }
            //页面加载触发
            onImageLoading()
            const onLoad=async()=>{
                const res= await showDetails()
                // console.log(res)
                if(res.code==='200'){
                    list.value=res.data
                    // 加载状态结束
                    loading.value=false
                    // 数据全部加载完成
                    finished.value=true
                }
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
            function onDetail(id){

                getOneDetails(id).then(res=>{
                    // console.log(res.data)
                    DetailData=localStorage.setItem("Detail",JSON.stringify(res.data))
                    //传数据
                    emitter.emit('mittFn',res.data)
                });
                router.push({
                    path: "/details",
                    query:{
                        id:id
                    }
                })
            }
            function getRowId(id){
                if (id == 1) {
                    router.push({path: '/information_reported'})
                } else if (id == 2) {
                    router.push({path: '/feedback'})
                } else if (id == 3) {
                    router.push({path: '/service'})
                } else if (id == 4) {
                    router.push({path: '/guidance'})
                } else if (id == 5) {
                    router.push({path: '/importantNotice'})
                } else if (id == 6) {
                    router.push({path: '/instructions'})
                }
            }

            return{
                images,
                refreshing,
                loading,
                finished,
                onLoad,
                list,
                onRefresh,
                isTop,
                onDetail,
                iconTexts,
                iconUrls,
                Path,
                menuList,
                routerList,
                getRowId,
                onImageLoading,
                DetailData,
                money
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
                font-size: 14px;
                line-height: 14px;
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
