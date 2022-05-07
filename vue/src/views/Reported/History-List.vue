<template>
    <div id="historyList-container">
        <v-header></v-header>
        <div class="van-return">
            <van-nav-bar
                    left-text="返回"
                    left-arrow
                    @click-left="onClickLeft"
            />
        </div>
        <div id="main">
            <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
                <van-list
                        v-model:loading="loading"
                        :finished="finished"
                        finished-text="没有更多了"
                        @load="onLoad"
                >
<!--                    <van-cell v-for="(item,index) in historyList" :key="index" center >-->
<!--                        <template #title>-->
<!--                            <h3 style="margin: 5px 0">{{item.enterpriseName}}</h3>-->
<!--                            <span style="color: #969799">{{item.detail}}</span>-->
<!--                        </template>-->
<!--                        <van-button type="primary" @click="onEdit(index)" size="small">编辑</van-button>-->
<!--                    </van-cell>-->
                    <van-cell v-for="(item,index) in historyList" :key="index"  center style="padding: 0 16px;">
                    <van-swipe-cell>
                        <van-cell style="padding: 0">
                            <template #title>
                                <h3 style="margin: 5px 0">{{item.enterpriseName}}</h3>
                                <span style="color: #969799">{{item.detail}}</span>
                            </template>
                        </van-cell>
                            <template #right >
                                <van-button square type="danger" text="删除" @click="onDelete(index)" />
                                <van-button square type="primary" text="编辑" @click="onEdit(index)" />
                            </template>
                    </van-swipe-cell>
                    </van-cell>
                </van-list>
            </van-pull-refresh>
        </div>
    </div>
</template>

<script>
    import Info_Reported from '../../components/comm-header'
    import {ref,reactive,computed} from 'vue'
    import {useRouter} from "vue-router";
    import {Toast,Dialog,Notify} from 'vant'
    import request from "../../api/axios.config";
    import emitter from "../../common/js/mitt";
    import $store from "../../store/index"
    import {useStore} from "vuex";

    export default {
        name: "History-List",
        components:{
            'v-header':Info_Reported
        },
        setup(){
            const onClickLeft=()=>{
                history.back()
            }
            const store=useStore()
            const router=useRouter()
            const Id=ref(0)
            const onLoad=async ()=>{

                // 请求接口
               await request.get("/report").then(res=>{
                    if (refreshing.value) {
                        historyList.value = [];
                        refreshing.value = false;
                    }
                    if(res.code==='200'){
                        historyList.value=res.data
                        // console.log(historyList.value[id])
                        // 加载状态结束
                        loading.value=false
                        // 数据全部加载完成
                        finished.value=true
                    }
                })
            }
            const onRefresh=()=>{
                // 清空列表数据
                finished.value = false;
                // 重新加载数据
                // 将 loading 设置为 true，表示处于加载状态
                loading.value = true;
                onLoad();
            }
            const historyInfoList=reactive({})
            const History=computed(()=>{return $store.state.History})
            // console.log(History.value)
            $store.dispatch('getHistory')
            // console.log(History.value.data)

            //点击修改按钮
            function onEdit (id){
                // emitter.on('mittFun',(historyInfoList)=>{
                //     console.log(historyInfoList)
                // })
                // console.log(id)
                //传数据
                emitter.emit('mittFun',historyList.value[id])
                localStorage.setItem("historyList",JSON.stringify(historyList.value[id]))
                console.log(historyList.value[id])
                //
                // request.post("/report",historyList.value[id]).then(res=>{
                //     if(!res.data){
                //         Toast.fail("信息添加失败")
                //     }else{
                //         Toast.success("信息添加成功")
                //     }
                // })
                router.push({
                    path: "/addReported",
                    query: {
                        id: id+1
                    }
                })
            }
            //删除按钮
            const onDelete=(id)=>{
                Dialog.confirm({
                    title:'删除',
                    message:'你确定要删除吗'
                }).then(()=>{
                    request.delete('/report/'+historyList.value[id].id).then(res=>{
                        // debugger
                        if(res.code!=='200'){
                            return Toast.fail('删除信息失败!')
                        }else{
                            Toast.success('删除信息成功!')
                        }
                        //刷新数据列表
                        onLoad()
                    })
                }).catch(()=>{
                    Dialog.close()
                })


            }
            const historyList=ref([])
            const loading = ref(false);
            const finished = ref(false);
            const refreshing = ref(false);
            return{
                onClickLeft,
                historyList,
                loading,
                finished,
                refreshing,
                onLoad,
                onRefresh,
                onEdit,
                onDelete,
                historyInfoList,
                History,
                Id
            }
        }

    }
</script>

<style lang="scss" scoped>
    #historyList-container{
        position: relative;
    }
    .van-return{
        padding-top: 2.2rem;
    }
    .van-right{
        top: 19px;
    }
    .van-swipe-cell__right{
        line-height: 39px;
        top: 19px;
    }
</style>
