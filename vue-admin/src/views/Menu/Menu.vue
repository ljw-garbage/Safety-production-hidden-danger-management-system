<template>
    <div>

        <div style="margin: 10px 0">
            <span style="font-size: 14px;padding-right: 5px">名称:</span><el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" size="mini" v-model="name"></el-input>
            <el-button class="ml-5" type="primary" size="mini" @click="onSearch">搜索</el-button>
            <el-button class="ml-5" type="warning" size="mini" @click="onReset">重置</el-button>
        </div>
        <div style="margin: 10px 0">
            <el-button type="primary" size="mini" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-button type="danger" size="mini" @click="delBatch()">批量删除 <i class="el-icon-remove-outline"></i></el-button>
        </div>
        <el-table :data="tableData" ref="multipleTable" stripe border :header-cell-class-name="headerBg"
                  row-key="id" default-expand-all
                  @selection-change="handleSelectionChange" size="mini">
            <el-table-column
                    type="selection"
                    width="55">
            </el-table-column>
            <el-table-column prop="id" label="ID" width="80">
            </el-table-column>
            <el-table-column prop="name" label="名称" >
            </el-table-column>
            <el-table-column prop="path" label="路径" >
            </el-table-column>
            <el-table-column prop="pagePath" label="页面路径" >
            </el-table-column>
            <el-table-column  label="图标" class-name="fontSize18" align="center" label-class-name="fontSize12">
                <template slot-scope="scope">
                    <i :class="scope.row.icon" />
                </template>
            </el-table-column>
            <el-table-column prop="sortNum" label="顺序" ></el-table-column>
            <el-table-column prop="description" label="描述" >
            </el-table-column>
            <el-table-column label="操作"  width="300" align="center">
                <template slot-scope="scope">
                    <el-button type="success" size="mini" @click="showEditDialog(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
                    <el-button type="primary" size="mini" @click="handleAddChild(scope.row.id)" v-if="!scope.row.pid&&!scope.row.path">新增子菜单 <i class="el-icon-circle-plus
"></i></el-button>
                    <el-button type="danger" size="mini" @click="removeUserById(scope.row.id)">删除 <i class="el-icon-remove-outline"></i></el-button>
                </template>
            </el-table-column>
        </el-table>
<!--        <div style="padding: 10px 0">-->
<!--            <el-pagination-->
<!--                    @size-change="handleSizeChange"-->
<!--                    @current-change="handleCurrentChange"-->
<!--                    :page-sizes="[2, 4, 8, 10]"-->
<!--                    :page-size="pageSize"-->
<!--                    layout="total, sizes, prev, pager, next, jumper"-->
<!--                    :total="total">-->
<!--            </el-pagination>-->
<!--        </div>-->
        <!--        新增菜单弹窗-->
        <el-dialog
                title="菜单信息"
                :visible.sync="isShowAddUser"
                width="35%"
                @close="addDialogClosed"
        >
            <!--          内容主体区-->
            <el-form v-bind:model="Form" :rules="FormRules" ref="FormRef" label-width="70px">
                <el-form-item label="名称" prop="name">
                    <el-input  v-model="Form.name" ></el-input>
                </el-form-item>
                <el-form-item label="路径" prop="path">
                    <el-input  v-model="Form.path" ></el-input>
                </el-form-item>
                <el-form-item label="图标">
                    <template slot-scope="scope">
                        <el-select v-model="Form.icon" placeholder="请选择" style="width: 100%;">
                            <el-option
                                    v-for="item in options"
                                    :key="item.name"
                                    :label="item.name"
                                    :value="item.value">
                                <i :class="item.value">{{item.name}}</i>
                            </el-option>
                        </el-select>
                    </template>
                </el-form-item>
                <el-form-item label="顺序">
                    <el-input v-model="Form.sortNum" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input  v-model="Form.description" autocomplete="off" ></el-input>
                </el-form-item>
                <el-form-item label="页面路径" prop="pagePath">
                    <el-input  v-model="Form.pagePath" ></el-input>
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
        name: "Menu",
        data(){
            return{
                tableData:[],
                headerBg:'headerBg',
                name:'',
                description:'',
                total:0,
                pageSize:8,
                pageNum:0,
                isShowAddUser:false, //展示新增弹窗
                editDialogVisible:false, //展示修改弹窗
                Form:{},
                //添加表单验证规则
                FormRules:{},
                multipleSelection: [],
                //存放图标
                options:[]
            }
        },
        created() {
            this.loading()
        },
        methods:{
            //加载数据
            async loading() {
                await this.request.get("/menu", {
                    params:{name:this.name}
                }).then(res => {
                    this.tableData = res.data
                    // console.log(this.tableData)
                })
                // this.request.get("/menu/page",{
                //     params: {
                //         pageNum: this.pageNum,
                //         pageSize: this.pageSize,
                //         name: this.name,
                //     }
                // }).then(res=>{
                //     this.total=res.data.total
                //     this.tableData=res.data.records
                // })
            },
            //搜索按钮
            onSearch(){
                this.request.get("/menu/page",{
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        name: this.name,
                    }
                }).then(res=>{
                    this.total=res.data.total
                    this.tableData=res.data.records
                })
            },
            //重置按钮
            onReset(){
                this.name=''
                this.description=''
                this.loading()
            },
            getIcon(){
                this.request.get("/menu/icons", {
                }).then(res => {
                    this.options = res.data
                })
            },
            handleAdd(){
                this.isShowAddUser=true
                this.Form={}
                this.getIcon()
            },
            handleAddChild(pid){
                this.isShowAddUser=true
                this.Form={}
                this.getIcon()
                if(pid){
                    this.Form.pid=pid
                }
            },
            //添加用户
            save(){
                this.request.post("/menu",this.Form).then(res=>{
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
            //添加用户对话框关闭事件
            addDialogClosed(){
                // //拿到表单对象,然后重置表单数据
                // this.$refs.FormRef.resetFields()
                this.isShowAddUser=false
            },

            //展示用户编辑对话框
            showEditDialog (row) {
                this.isShowAddUser = true;
                this.Form=row
                this.getIcon()
            },
            //根据id删除对应的用户id
            async removeUserById(id) {
                const confirmResult=await this.$confirm('此操作将永久删除, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).catch ( err => err)
                if(confirmResult!=='confirm'){
                    return this.$message.info('已取消删除')
                }
                await this.request.delete('/menu/'+id).then(res=>{
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
                const confirmResult=await this.$confirm('此操作将永久删除, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).catch ( err => err)
                if(confirmResult!=='confirm'){
                    return this.$message.info('已取消删除')
                }
                let ids=this.multipleSelection.map(v=>v.id)
                await this.request.post('/menu/del/batch',ids).then(res=>{
                    if(res.code!=='200'){
                        this.$message.error('批量删除失败!')

                    }else{
                        this.$message.success('批量删除成功!')
                        //刷新数据列表
                        this.loading()
                    }

                })
            },
            handleSelectionChange(val){
                console.log(val);
                this.multipleSelection = val;
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
        }
    }
</script>

<style >
    .fontSize18{
        font-size: 18px;
    }
    .fontSize12{
        font-size: 12px;
    }
</style>