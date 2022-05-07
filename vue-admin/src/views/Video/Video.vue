<template>
    <div style="padding: 10px">
        <el-card>
            <div v-for="item in videos" :key="item.id" style="margin:10px 0; padding:10px 0; color:#666;border-bottom: 1px dashed #ccc">
                <span class="front" @click="onDetail(item.id)">{{item.name}}</span>
                <span style="float:right; font-size: 12px">文件大小:{{item.size}} kb</span>
            </div>
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "Video",
        data(){
            return{
                videos:[],
            }
        },
        created() {
            this.request.get("/echarts/file/front/all").then(res=>{
                this.videos=res.data.filter(v=>v.type==='mp4')
            })
        },
        methods:{
            onDetail(id){
                this.$router.push({path:'/VideoDetail',query:{id:id}})
            }
        }
    }
</script>

<style scoped>
        .front{
            cursor: pointer;
        }
        .front:hover{
            color: skyblue;
        }
</style>