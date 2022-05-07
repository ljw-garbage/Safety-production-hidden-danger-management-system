<template>
    <div class="container">
        <div class="main">
            <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
            <van-list
                    v-model="loading"
                    :finished="finished"
                    finished-text="没有更多了"
                    @load="onLoad"
            >
            <ul  class="pending-project-list">
                <li v-for="(item,index) in list" :key="index" :title="item" class="newItem">
                    <div class="title">
                        <span></span>
                        {{item.title}}
                    </div>
                    <div class="content">
                        {{item.title}}
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
    import {ref,reactive} from 'vue'
    import {useRouter} from 'vue-router'
    import {computed} from "vue";
    import {getOneDetails} from "../../api/api-list";
    import emitter from "../../common/js/mitt";
    import request from "../../api/axios.config";
    export default {
        name: "Instruction-help",
        setup(){
            const isTop=computed(()=>{
                return top.value?true:false
            })
            const router=useRouter()
            const list=ref({})
            const refreshing=ref(false)
            const loading=ref(false)
            const finished=ref(false)
            const id=ref(0)
            let DetailData=ref('')
            const onLoad=()=>{
                request.get("/instructions-help").then(res=>{
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
            function onDetail(id){
                // getOneDetails(id).then(res=>{
                //     // console.log(res.data)
                //     // DetailData=localStorage.setItem("Detail",JSON.stringify(res.data))
                //     // //传数据
                //     // emitter.emit('mittFn',res.data)
                //     // console.log(res.data)
                // });
                // request.get("/instructions-help/"+id).then(res=>{
                //    DetailData=localStorage.setItem("Detail",JSON.stringify(res.data))
                //     emitter.emit('mittFn',res.data)
                // })
                router.push({
                    path: "/instructions-help-detail",
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
                id,
                onLoad,
                onRefresh
            }
        }
    }
</script>

<style lang="scss" scoped>
       .container{
           padding-top: 10px;
           background-color: #f5f5f9;
           height: 100vh;
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
                   font-size: 16px;
                   font-weight: 500;
                   color: #333;
                   height: 20px;
                   line-height: 20px;
                   width: 100%;
                   white-space: nowrap;
                    margin-bottom: 5px;
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
                   white-space: nowrap;
               }
               .ft{
                   height: 18px;
                   padding-left: 9px;
                   margin-bottom: 10px;
               }
           }
       }
</style>
