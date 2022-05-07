<template>
    <div>
        <div style="margin: 10px 0">
            <span style="font-size: 14px;padding-right: 5px">文件:</span><el-input style="width: 200px" placeholder="请输入文件名" suffix-icon="el-icon-search" size="mini" v-model="name"></el-input>
            <el-button class="ml-5" type="primary" size="mini" @click="onSearch">搜索</el-button>
            <el-button class="ml-5" type="warning" size="mini" @click="onReset">重置</el-button>
        </div>
        <div style="margin: 10px 0">
            <el-upload
                    class="upload-demo"
                    action="http://localhost:8088/file/upload"
                    style="display: inline-block; margin: 5px"
                    :show-file-list="false"
                    :on-success="handleFileUploadSuccess"
            >
                <el-button type="primary" size="mini" >上传文件  <i class="el-icon-top"></i></el-button>

            </el-upload>
            <el-button type="danger" size="mini" @click="delBatch()">批量删除 <i class="el-icon-remove-outline"></i></el-button>
        </div>
        <el-table :data="tableData" ref="multipleTable" stripe border :header-cell-class-name="headerBg" @selection-change="handleSelectionChange" size="mini">
            <el-table-column
                    type="selection"
                    width="55">
            </el-table-column>
            <el-table-column prop="id" label="id" width="60">
            </el-table-column>
            <el-table-column prop="name" label="文件名" >
            </el-table-column>
            <el-table-column prop="type" label="文件类型" >
            </el-table-column>
            <el-table-column prop="size" label="文件大小(kb)">
            </el-table-column>
            <el-table-column label="预览" width="150">
                <template slot-scope="scope">
                    <el-button type="primary" @click="preview(scope.row.url)" size="mini">预览</el-button>
                </template>
            </el-table-column>
            <el-table-column prop="enable" label="启用" width="80">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.enable" @change="changeEnable(scope.row)" active-color="#13ce66" inactive-color="#ccc"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="操作"   align="center">
                <template slot-scope="scope">
                    <el-button type="danger" size="mini" @click="removeUserById(scope.row.id)">删除 <i class="el-icon-remove-outline"></i></el-button>
                    <el-button type="primary" size="mini" @click="download(scope.row.url)">下载<i class="el-icon-download"></i></el-button>
                </template>
            </el-table-column>
        </el-table>
        <div style="padding: 10px 0">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :page-sizes="[2, 4, 8, 10]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>
    </div>

</template>

<script>
    export default {
        name: "File",
        data(){
            return{
                tableData:[],
                name:'',
                multipleSelection:[],
                headerBg:'headerBg',
                pageSize:8,
                pageNum:0,
                total:0
            }
        },
        created() {
            this.loading()
        },
        methods:{
            //加载数据
            async loading() {
                await this.request.get("/file/page", {
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        name: this.name,
                    }
                }).then(res => {
                    this.tableData = res.data.records;
                    this.total = res.data.total
                    console.log(res)
                })
            },
            //pageSize 改变时触发
            handleSizeChange(pageSize){
                this.pageSize=pageSize;
                this.loading()
            },
            //currentPage 改变时触发
            handleCurrentChange(pageNum){
                this.pageNum=pageNum;
                this.loading()
            },
            //搜索按钮
            onSearch(){
                this.loading()
            },
            //重置按钮
            onReset(){
                this.name=''
                this.loading()
            },
            handleAdd(){
                this.isShowAddUser=true
                this.addForm={}
            },
            //根据id删除对应的用户id
            async removeUserById(id) {
                const confirmResult=await this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).catch ( err => err)
                if(confirmResult!=='confirm'){
                    return this.$message.info('已取消删除')
                }
                await this.request.delete('/file/'+id).then(res=>{
                    if(res.code!=='200'){
                        return this.$message.error('删除文件失败!')
                    }else{
                        this.$message.success('删除文件成功!')
                    }
                    //刷新数据列表
                    this.loading()
                })

            },
            //批量删除
            async delBatch(){
                let ids=this.multipleSelection.map(v=>v.id)
                    const confirmResult=await this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).catch ( err => err)
                    if(confirmResult!=='confirm'){
                        return this.$message.info('已取消删除')
                    }
                    await this.request.post('/file/del/batch',ids).then(res=>{
                        if(res.code!=='200'){
                            this.$message.error('批量删除文件失败!')
                        }else{
                            this.$message.success('批量删除文件成功!')
                            //刷新数据列表
                            this.loading()
                        }
                 })
            },
            handleSelectionChange(val){
                // console.log(val);
                this.multipleSelection = val;
                console.log(this.multipleSelection)
            },
            handleFileUploadSuccess(res){
                this.$message.success("上传成功")
                this.loading()
            },
            //下载事件
            download(url){
                window.open(url)
            },
            changeEnable(row){
                this.request.post("/file/update",row).then(res=>{
                    if(res.code==='200'){
                        this.$message.success("更新成功")
                    }
                })
            },
            preview(url) {
                window.open('https://file.keking.cn/onlinePreview?url=' + encodeURIComponent(window.btoa((url))))
            },
        }
    }
</script>

<style scoped>

</style>