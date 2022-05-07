<template>
    <div>
        <h2>Welcome {{account}} 用户 !!! </h2>
        <el-row :gutter="10" style="padding-bottom: 40px">
            <el-col :span="6">
                <el-card style="color: #409EFF">
                    <div><i class="el-icon-user" /> 用户总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold" > {{total}}</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card style="color: #F56C6C">
                    <div><i class="el-icon-sunrise" /> 生产隐患总量</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold" v-model="reports" > {{reports}}</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card style="color: #67C23A">
                    <div><i class="el-icon-files" /> 文件总量</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold" >{{files}}</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card style="color:#E6A23C">
                    <div><i class="el-icon-sunrise-1" /> 警示案例总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold" > {{details}}</div>
                </el-card>
            </el-col>
        </el-row>
        <el-row :gutter="10">
            <el-col :span="12"><div id="main" style="width: 500px;height: 450px"></div></el-col>
            <el-col :span="12"><div id="pie" style="width: 500px;height: 450px"></div></el-col>
        </el-row>

    </div>

</template>

<script>
    import * as echarts from 'echarts'

    export default {
        name: "Welcome",
        data(){
            return{
                account:JSON.parse(localStorage.getItem("userInfo")).account,
                total:0,
                reports:0,
                files:0,
                details:0
            }
        },
        created() {
            this.request.get("/report").then(res=>{
                this.reports=res.data.length
            }).catch(()=>{
                this.$message.warning("请先连接Redis")
            })
            this.request.get("/echarts/file/front/all").then(res=>{
                this.files=res.data.length
            }).catch(()=>{
                this.$message.warning("请先连接Redis")
            })
            this.request.get("/details").then(res=>{
                this.details=res.data.length
            }).catch(()=>{
                this.$message.warning("请先连接Redis")
            })
        },
        mounted() {

            //折线图
            var chartDom = document.getElementById('main');
            var myChart = echarts.init(chartDom);
            var option;

            this.request.get("/echarts/members").then(res=>{
                option.series[0].data=res.data;
                option.series[1].data=res.data;
                //伪造数据
                option.series[2].data=[5,6,7,8]
                option.series[3].data=[5,6,7,8]

                PieOption.series[0].data=[
                    {name:'第一季度',value: res.data[0]},
                    {name:'第二季度',value: res.data[1]},
                    {name:'第三季度',value: res.data[2]},
                    {name:'第四季度',value: res.data[3]}
                ]
                this.total=res.data[0]+res.data[1]+res.data[2]+res.data[3]
                PieOption.series[1].data=[5,6,7,8]
                myChart.setOption(option)
                myPieChart.setOption(PieOption)
            })

            option = {
                title: {
                    text: '各个季度上报隐患数',
                    subtext: '趋势图',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left'
                },
                xAxis: {
                    type: 'category',
                    data: ["第一季度","第二季度","第三季度","第四季度"]
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                    {
                        name:'星巴克公司',
                        data: [],
                        type: 'line'
                    },
                    {
                        name:'星巴克公司',
                        data: [],
                        type: 'bar'
                    },
                    {
                        name:'瑞幸咖啡公司',
                        data: [],
                        type: 'line'
                    },
                    {
                        name:'瑞幸咖啡公司',
                        data: [],
                        type: 'bar'
                    }


                ]
            };

            //柄图
            var PieChartDom = document.getElementById('pie');
            var myPieChart = echarts.init(PieChartDom);
            var PieOption;

            PieOption = {
                title: {
                    text: '各个季度上报隐患数',
                    subtext: '比例图',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left'
                },
                series: [
                    {
                        type: 'pie',
                        radius: '50%',
                        center:['45%','50%'],
                        data: [],
                        label: {
                            normal: {
                                show: true,
                                formatter: '{b}: {c} ({d}%)' //自定义显示格式(b:name, c:value, d:百分比)
                            }
                        },
                        emphasis: {
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    },
                    {
                        type: 'pie',
                        radius: '40%',
                        center: ['80%','75%'],
                        data: [],
                        label: {
                            normal:{
                                show:true,
                                position:'inner', //标签的位置
                                textStyle : {
                                    fontWeight : 300 ,
                                    fontSize : 14,    //文字的字体大小
                                    color:'#fff'
                                },
                                formatter:'{d}%'
                            }
                        },
                        emphasis: {
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            };

        }
    }
</script>

<style scoped>

</style>