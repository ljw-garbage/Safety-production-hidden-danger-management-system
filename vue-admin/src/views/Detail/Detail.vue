<template>
    <div>
        <div style="margin: 10px 0">
            <span style="font-size: 14px;padding-right: 5px">标题:</span><el-input style="width: 200px" placeholder="请输入标题名" suffix-icon="el-icon-search" size="mini" v-model="title"></el-input>
            <el-button class="ml-5" type="primary" size="mini" @click="onSearch">搜索</el-button>
            <el-button class="ml-5" type="warning" size="mini" @click="onReset">重置</el-button>
        </div>
        <div style="margin: 10px 0">
            <el-button type="primary" size="mini" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-button type="danger" size="mini" @click="delBatch()">批量删除 <i class="el-icon-remove-outline"></i></el-button>
        </div>
        <el-table :data="tableData" ref="multipleTable" stripe border :header-cell-class-name="headerBg" @selection-change="handleSelectionChange" size="mini">
            <el-table-column
                    type="selection"
                    width="55">
            </el-table-column>
            <el-table-column prop="id" label="id" width="60">
            </el-table-column>
            <el-table-column prop="title" label="标题" width="280px" align="center">
            </el-table-column>
            <el-table-column prop="content" label="内容" align="center">
            </el-table-column>
            <el-table-column prop="date" label="日期" width="140px" align="center">
            </el-table-column>
            <el-table-column label="操作"  width="200px"  align="center">
                <template slot-scope="scope">
                    <el-button type="success" size="mini" @click="showEditDialog(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
                    <el-button type="danger" size="mini" @click="removeUserById(scope.row.id)">删除 <i class="el-icon-remove-outline"></i></el-button>
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
        <el-dialog
                title="案例详情"
                :visible.sync="isShowAddUser"
                width="35%"
                @close="addDialogClosed"
        >
            <!--          内容主体区-->
            <el-form v-bind:model="Form" :rules="FormRules" ref="FormRef" label-width="70px">
                <el-form-item label="标题" prop="title">
                    <el-input  v-model="Form.title" ></el-input>
                </el-form-item>
                <el-form-item label="内容" prop="content" >
                    <el-input type="textarea" autosize v-model="Form.content" ></el-input>
                </el-form-item>
                <el-form-item label="日期" prop="date">
                    <el-input  v-model="Form.date" disabled></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button @click="isShowAddUser=false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </span>
        </el-dialog>
    </div>

</template>

<script>
    export default {
        name: "Detail",
        data(){
            return{
                tableData:[],
                title:'',
                multipleSelection:[],
                isShowAddUser:false, //展示新增弹窗
                editDialogVisible:false, //展示修改弹窗
                headerBg:'headerBg',
                pageSize:8,
                pageNum:0,
                total:0,
                Form:{},
                //添加表单验证规则
                FormRules:{},
            }
        },
        created() {
            this.loading()
        },
        methods:{
            //加载数据
            async loading() {
                await this.request.get("/details/page", {
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        title: this.title,
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
                this.title=''
                this.loading()
            },
            handleAdd(){
                this.isShowAddUser=true
                this.Form={}
            },
            //添加用户
            save(){
                this.request.post("/details",this.Form).then(res=>{
                    if(res.code!=='200'){
                        this.$message.error('添加失败!')
                    }else{
                        this.$message.success('添加或修改成功!')
                    }
                    //隐藏添加的对话框
                    this.isShowAddUser=false
                    //重新添加用户列表
                    this.loading()
                })
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
                await this.request.delete('/details/'+id).then(res=>{
                    if(res.code!=='200'){
                        return this.$message.error('删除失败!')
                    }else{
                        this.$message.success('删除成功!')
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
                    await this.request.post('/details/del/batch',ids).then(res=>{
                        if(res.code!=='200'){
                            this.$message.error('批量删除文件失败!')
                        }else{
                            this.$message.success('批量删除文件成功!')
                            //刷新数据列表
                            this.loading()
                        }
                 })
            },
            //展示用户编辑对话框
            showEditDialog (row) {
                this.isShowAddUser = true;
                this.Form=row
            },
            handleSelectionChange(val){
                // console.log(val);
                this.multipleSelection = val;
                console.log(this.multipleSelection)
            },
            handleFileUploadSuccess(res){
                console.log(res)
                this.loading()
            },
            //添加用户对话框关闭事件
            addDialogClosed(){
                // //拿到表单对象,然后重置表单数据
                // this.$refs.FormRef.resetFields()
                this.isShowAddUser=false
            },
        }
    }
</script>

<style lang="less" scoped>
    .el-textarea__inner{
        min-height: 300px!important;
    }
</style>