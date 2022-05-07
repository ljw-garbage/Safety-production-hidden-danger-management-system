<template>
    <div style="color: #666;font-size: 14px;">
        <div style="padding-bottom: 20px">
            <h2>欢迎你！{{ userInfo.account }}</h2>
        </div>
        <el-card>
            安全生产隐患管理信息系统(毕业设计)🐶都不用
            <el-divider />
            我祝您 ! 虎虎生威，虎年大吉。有花不完的钱,拿不完的offer,永远不用加班。
        </el-card>
        <div style="height: 1px; background: #ddd; margin: 20px 0"/>

        <el-row :gutter="30">
            <el-col :span="12">
                <div style="padding: 20px 0; font-size: 20px">项目描述</div>
                <div>
                    安全生产隐患排查与整治管理信息系统主要实现安全生产隐患信息的采集登记，包括安全生产隐患的基本信息，以及相关视频及图片资料。安全生产隐患信息的分析统计、安全隐患信息的整改情况。 系统分为信息采集端和县级管理端两个部分。信息采集员负责安全生产隐患信息的采集、建议的整改措施、检查时间、整治期限。以及后期整治结果的跟进。 县级管理端负责安生生产隐患信息的统计分析，包括安全生产隐患情况，整治情况。可以按类别进行分别统计和分析。 安全生产隐患排查与整治管理信息信息系统的建设以相关安全生产管理工作规范为指导，以提高安全监管效率和优化服务为目标，依托计算机网络技术、软件技术、通信技术，建立统一的技术基础平台，实现安全管理的数据统一存储和管理。为使信息系统能够满足社旗县应急管理局开展安全监管工作的需要，保证系统的灵活性、适应性、可靠性和可扩展性，实现隐患信息的交换与共享。
                </div>
                <div class="m-10"><el-button type="danger" size="mini"><i class="el-icon-coin"></i> 免费开源</el-button></div>
                <div class="m-10"><el-button type="primary" size="mini"><i class="el-icon-s-custom"></i> QQ:2718714181</el-button></div>
                <div class="m-10">
                    <el-button type="primary" size="mini"><i class="el-icon-link"></i> <a style="color: #fff" href="https://gitee.com/lijiawei0216/vc">Gitee源码</a></el-button>
                </div>
            </el-col>
            <el-col :span="12">
                <div style="padding: 20px 0; font-size: 20px">技术栈</div>
                <el-row>
                    <el-col :span="12" style="line-height: 30px">
                        <div><b>后端</b></div>
                        <div>SpringBoot2</div>
                        <div>Hutool</div>
                        <div>Poi</div>
                        <div>Lombok</div>
                        <div>Mybatis/Mybatis-plus</div>
                    </el-col>
                    <el-col :span="12" style="line-height: 30px">
                        <b>前端</b>
                        <div>Vue2</div>
                        <div>Vue-Router</div>
                        <div>VueX</div>
                        <div>ElementUI</div>
                        <div>Axios</div>
                    </el-col>
                </el-row>
            </el-col>
        </el-row>

    </div>
</template>

<script>
    export default {
        name: "Welcome",
        data() {
            return {
                userInfo: localStorage.getItem("userInfo") ? JSON.parse(localStorage.getItem("userInfo")) : {}
            }
        }
    }
</script>

<style>
    .m-10 {
        margin: 10px 0;
    }
</style>
