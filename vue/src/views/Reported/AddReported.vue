<template>
    <div class="Information_reported">
        <v-header/>
        <div class="van-return">
            <van-nav-bar
                    left-text="返回"
                    left-arrow
                    @click-left="onClickLeft"
                    right-text="历史记录"
                    @click-right="onClickRight"
            />
        </div>
        <div id="main">
            <!--            <van-form @submit="onSubmit">-->
            <h5 class="h5">隐患分类</h5>
            <van-cell-group inset>
                <van-cell
                        v-model="hidden"
                        is-link
                        readonly
                        arrow-direction="down"
                        @click="showHidden = true"
                >
                    <span>{{historyInfoList.classification}}</span>
                </van-cell>
            </van-cell-group>

            <van-popup v-model:show="showHidden" round position="bottom">
                <van-picker
                        title="请选择隐患分类"
                        :columns="columns"
                        @cancel="showHidden = false"
                        @confirm="onConfirm"
                />
            </van-popup>
            <h5 class="h5">隐患具体地址</h5>
            <van-cell-group inset>
                <van-field
                        rows="1"
                        v-model="historyInfoList.address"
                        type="textarea"
                        autosize
                />
            </van-cell-group>
            <h5 class="h5">隐患区域</h5>
            <van-cell-group inset>
                <van-field
                        rows="1"
                        v-model="historyInfoList.territory"
                        type="textarea"
                        autosize
                />
            </van-cell-group>
            <h5 class="h5">隐患具体描述</h5>
            <van-cell-group inset>
                <van-field
                        rows="2"
                        v-model="historyInfoList.detail"
                        type="textarea"
                />
            </van-cell-group>
            <h5 class="h5">企业名称</h5>
            <van-cell-group inset>
                <van-field
                        rows="1"
                        v-model="historyInfoList.enterpriseName"
                        type="textarea"
                        autosize
                />
            </van-cell-group>
            <h5 class="h5">企业负责人信息</h5>
            <van-cell-group inset>
                <van-field
                        v-model="historyInfoList.contactPerson"
                        name="姓名"
                        label="姓名"
                        placeholder="请输入姓名"
                        :rules="[{ required: true, message: '请填写姓名' }]"
                />
                <van-field
                        v-model="historyInfoList.phone"
                        name="手机号"
                        label="手机号"
                        placeholder="请输入手机号"
                        :rules="[{ required: true, message: '请填写手机号' }]"
                />
            </van-cell-group>
            <h5 class="h5">整改期限</h5>
            <van-cell-group inset>
                <van-field
                        rows="1"
                        v-model="historyInfoList.term"
                        type="textarea"
                        autosize
                />
            </van-cell-group>
            <h5 class="h5">整改结果</h5>
            <van-cell-group inset>
                <van-field
                        rows="1"
                        v-model="historyInfoList.result"
                        type="textarea"
                        autosize
                />
            </van-cell-group>
            <h5 class="h5">整改措施</h5>
            <van-cell-group inset>
                <van-field
                        rows="1"
                        v-model="historyInfoList.measures"
                        type="textarea"
                        autosize
                />
            </van-cell-group>
            <h5 class="h5">整改建议</h5>
            <van-cell-group inset>
                <van-field
                        rows="2"
                        v-model="historyInfoList.proposal"
                        type="textarea"
                />
            </van-cell-group>
            <h5 class="h5">图片上传</h5>
            <!--                    <van-cell-group inset>-->
            <!--                        <van-field name="uploader" label="图片上传">-->
            <!--                            <template #input>-->
            <!--                                <van-uploader v-model="historyInfoList.imageUrl" />-->
            <!--                            </template>-->
            <!--                        </van-field>-->
            <!--                    </van-cell-group>-->

            <van-cell-group inset>
                <van-field name="uploader" label="图片上传">
                    <template #input>
                        <van-uploader :after-read="onUpload" v-model="imageUrl" :max-size="50*1024" @oversize="onOversize"/>
                        <!--                            <van-button round icon="plus" type="primary" >上传文件</van-button>-->
                    </template>

                </van-field>
            </van-cell-group>


            <div style="margin: 16px;">
                <van-button round block type="primary"   native-type="submit" @click="onSubmit">
                    提交
                </van-button>
            </div>

            <!--            </van-form>-->

        </div>
    </div>
</template>

<script>
    import Info_reported from '../../components/comm-header'
    import {ref,reactive,toRefs} from 'vue'
    import {useRouter} from 'vue-router'
    import {Notify,Toast} from 'vant'
    import {isNull} from "../../common/js/util";
    import {reportSubmit,reportUpdate} from "../../api/api-list";
    import request from "../../api/axios.config";
    import emitter from "../../common/js/mitt";
    export default {
        name: "AddReported",
        components:{
            'v-header':Info_reported
        },
        setup(){
            const columns=[
                '废弃资源回收加工业',
                '机械企业',
                '有色企业',
                '冶金企业',
                '药品生产企业',
                '烟花爆竹生产企业',
                '烟花爆竹经营企业',
                '炸药及火工产品制造业'
            ]
            const router=useRouter()
            const onClickLeft = () => history.back();
            const onClickRight=()=>{
                router.push({path:'/history-list'})
            }
            const contactPerson = ref('');
            const showHidden=ref(false);
            // const hiddenResult=ref('')
            const hidden=ref('')
            const imageUrl=ref([{ url: 'https://img.yzcdn.cn/vant/leaf.jpg' }])
            const validate=ref(true)
            const onConfirm = (hidden) => {
                historyInfoList.classification = hidden;
                // console.log(hidden)
                showHidden.value = false;
            };
            const historyList=JSON.parse(localStorage.getItem("historyList"))
            let historyInfoList=reactive({
                id:historyList.id,
                classification:historyList.classification,
                address:historyList.address,
                detail:historyList.detail,
                enterpriseName:historyList.enterpriseName,
                phone:historyList.phone,
                contactPerson:historyList.contactPerson,
                term:historyList.term,
                proposal:historyList.proposal,
                // imageUrl:[],
                territory:historyList.territory,
                result:historyList.result,
                measures:historyList.measures
            })

            // const address=ref('')
            // const reactData=toRefs(historyInfoList)

            const getReported=()=>{
                // emitter.on('mittFun',(res)=> {
                //     // historyInfoList.detail=res.detail
                //     // historyInfoList.enterpriseName=res.enterpriseName
                //     // historyInfoList.classification=res.classification
                //    let Address=res.address
                // })
            }
            getReported()

            const onSubmit=()=>{
                console.log(historyInfoList)
                validate.value=validates()
                // debugger
                if (validate.value) {
                    reportUpdate(historyInfoList).then((res) => {
                        if (res.code!=='200') {
                            Notify({type:'danger',message:res.msg})
                        }else{
                            Toast.success("更新成功")
                            localStorage.removeItem("historyList")
                            router.push({path:"/history-list"});
                        }
                    });
                } else {
                    Notify({type:'danger',message:"请将必填信息填写完整！"});
                }
            }

            const validates=()=> {
                const {
                    Result=historyInfoList.result,
                    Classification=historyInfoList.classification,
                    Address=historyInfoList.address,
                    Territory=historyInfoList.territory,
                    Detail=historyInfoList.detail,
                    EnterpriseName=historyInfoList.enterpriseName,
                    Phone=historyInfoList.phone,
                    ContactPerson=historyInfoList.contactPerson,
                    Term=historyInfoList.term,
                    Proposal=historyInfoList.proposal,
                    // Images=historyInfoList.imageUrl,
                    Measures=historyInfoList.measures
                } = historyInfoList;

                if(isNull(Classification))return false
                if(isNull(Result))return false
                if(isNull(Address))return false
                if(isNull(Territory))return false
                if(isNull(Detail))return false
                if(isNull(EnterpriseName)) return false
                if(isNull(Phone)) return false
                if(isNull(ContactPerson))return false
                if(isNull(Term))return false
                if(isNull(Proposal)) return false
                // if(isNull(Images)) return false
                if(isNull(Measures)) return false
                return true
            }

            const onOversize = (file) => {
                console.log(file);
                Toast('文件大小不能超过 50kb');
            };
            const onUpload=(file)=>{
                // console.log(file)
                const formData=new FormData()
                formData.append("file",file.file)
                request.post("http://localhost:8088/file/upload",formData).then(res=>{
                    if(res.data){
                        Notify({type:'success',message:"上传成功"})
                    }else{
                        Notify({type:'danger',message:"上传失败"})
                    }
                }).catch((err)=>{
                    console.log(err)
                })
            }
            // const Address=ref('');
            return{
                onClickLeft,
                onClickRight,
                contactPerson,
                showHidden,
                onConfirm,
                columns,
                // Classification,
                onSubmit,
                // hiddenResult,
                validates,
                hidden,
                historyInfoList,
                onUpload,
                onOversize,
                imageUrl,
                getReported,
                historyList
                // Address
                // address
                // ...reactData
            }
        }
    }
</script>

<style lang="scss">
    .Information_reported{
        position: relative;
        background-color: #eee;
    }
    .van-return{
        padding-top: 2.2rem;
    }
    #main{
        .h5{
            color: #1989fa;
            font-weight: 700!important;
            margin-left: 16px;
        }
        .h5:before{
            margin-right: 2px;
            color:#ee0a24;
            content: '*';
        }
    }
</style>
