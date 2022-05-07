<template>
    <div id="container" style="padding-top: 0;">
        <div id="top">
            <h3 class="header">安全生产隐患信息反馈</h3>
            <div class="user" @click="onEditUserInfo">
                <van-icon name="https://b.yzcdn.cn/vant/icon-demo-1126.png" size="35" color="#fff" />
            </div>
        </div>
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
                    <van-cell v-for="item in historyList" :key="item.id"  center>
                        <template #title>
                            <h3 style="margin: 5px 0">{{item.enterpriseName}}</h3>
                            <span style="color: #969799">{{item.detail}}</span>
                        </template>
                        <van-button type="primary" @click="onFeedback(item.id)" size="small">反馈</van-button>
                    </van-cell>
                </van-list>
            </van-pull-refresh>
        </div>
        <van-dialog v-model:show="isShowFeedback" title="反馈内容" show-cancel-button :before-close="onFeedbackContent">
            <van-field
                    required
                    label="反馈内容"
                    rows="2"
                    v-model="feedbackContent.content"
                    autosize
                    type="textarea"
                    placeholder="请输入反馈"
            />
        </van-dialog>
    </div>
</template>

<script>
    import {reactive, ref} from 'vue'
    import {useRoute, useRouter} from 'vue-router'
    import request from "../../api/axios.config";
    import {Toast} from "vant";

    export default {
        name: "feedback",

        setup(){
            const router=useRouter()
            const onEditUserInfo=()=>{
                router.push({path:'/userInfo'})
            }
            const onClickLeft = () => history.back();
            const onLoad=()=>{
                // 请求接口
                request.get("/report").then(res=>{
                    if(res.code==='200'){
                        historyList.value=res.data
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
            function onFeedbackContent(action){
                if (action==='confirm'&&feedbackContent.content!==""){
                    onFeedback(feedbackContent.reportId)
                    isShowFeedback.value=false
                    feedbackContent.content=''
                }else if (action==='cancel'){
                    isShowFeedback.value=false
                    feedbackContent.content=''
                }
            }
            const onFeedback=(id)=>{
                isShowFeedback.value=true
                feedbackContent.reportId=id
                // console.log(feedbackContent)
                if(feedbackContent.content!==''){
                    request.post('/comment',feedbackContent).then(res=>{
                        if(res.code === "200"){
                            Toast.success('反馈成功!')
                        }else{
                            Toast.fail('反馈失败!')
                        }
                    })
                }
            }
            const historyList=ref([])
            const loading = ref(false);
            const finished = ref(false);
            const refreshing = ref(false);
            const isShowFeedback=ref(false);
            const feedbackContent=reactive({content:'',reportId:''})
            return{
                onEditUserInfo,
                onClickLeft,
                onLoad,
                historyList,
                loading,
                finished,
                refreshing,
                onRefresh,
                isShowFeedback,
                onFeedback,
                feedbackContent,
                onFeedbackContent,

            }
        }
    }
</script>

<style lang="scss" scoped>
    #container{
        position: relative;
        padding-top: 0!important;
        .van-return{
            padding-top: 2.2rem;
        }
    }
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
