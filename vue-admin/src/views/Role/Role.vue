<template>
    <div>

        <div style="margin: 10px 0">
            <span style="font-size: 14px;padding-right: 5px">角色名:</span><el-input style="width: 200px" placeholder="请输入角色名" suffix-icon="el-icon-search" size="mini" v-model="name"></el-input>
            <span style="font-size: 14px;padding-left: 5px" >角色描述:</span><el-input style="width: 200px" placeholder="请输入角色描述" suffix-icon="el-icon-user" size="mini" v-model="description" class="ml-5"></el-input>
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
            <el-table-column prop="name" label="角色名" >
            </el-table-column>
            <el-table-column prop="description" label="角色描述" >
            </el-table-column>
            <el-table-column prop="flag" label="唯一标识" >
            </el-table-column>
            <el-table-column label="操作"  width="280" align="center">
                <template slot-scope="scope">
                    <el-button type="success" size="mini" @click="showEditDialog(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
                    <el-button type="danger" size="mini" @click="removeUserById(scope.row.id)">删除 <i class="el-icon-remove-outline"></i></el-button>
                    <el-button type="primary" size="mini" @click="selectMenu(scope.row)">分配菜单 <i class="el-icon-menu"></i></el-button>
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
        <!--        新增弹窗-->
        <el-dialog
                title="角色信息"
                :visible.sync="isShowAddUser"
                width="35%"
                @close="addDialogClosed"
        >
            <!--          内容主体区-->
            <el-form v-bind:model="Form" :rules="FormRules" ref="FormRef" label-width="70px">
                <el-form-item label="名称" prop="name">
                    <el-input  v-model="Form.name" ></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input  v-model="Form.description" ></el-input>
                </el-form-item>
                <el-form-item label="唯一标识" prop="flag">
                    <el-input  v-model="Form.flag" ></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="isShowAddUser=false">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog
                title="分配菜单"
                :visible.sync="isShowMenu"
                width="30%"
                @close="">
            <el-tree
                    :data="MenuData"
                    show-checkbox
                    node-key="id"
                    :props="props"
                    :default-expanded-keys="expanded"
                    :default-checked-keys="checked"
                    ref="tree"
                    >
                 <span class="custom-tree-node" slot-scope="{ node, data }">
                    <span><i :class="data.icon"></i>{{ data.name }}</span>
                 </span>
            </el-tree>
            <span slot="footer" class="dialog-footer">
                <el-button @click="isShowMenu=false">取 消</el-button>
                <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "Role",
        data(){
            return{
                props:{
                    label:'name',
                },
                tableData:[],
                headerBg:'headerBg',
                total:0,
                pageSize:8,
                pageNum:0,
                name:'',
                description:'',
                isShowAddUser:false, //展示新增弹窗
                editDialogVisible:false, //展示修改弹窗
                isShowMenu:false, //展示分配菜单弹窗
                Form:{},
                //添加表单验证规则
                FormRules:{},
                multipleSelection: [],
                MenuData:[],
                expanded:[],
                checked:[],
                roleId:0,
                roleFlag:'',
                ids:[]
            }
        },
        created() {
            this.loading()
        },
        methods:{
            //加载数据
            async loading() {
                await this.request.get("/role/page", {
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        name:this.name,
                        description:this.description
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
                this.description=''
                this.loading()
            },
            handleAdd(){
                this.isShowAddUser=true
                this.Form={}
            },
            //添加用户
            save(){
                    this.request.post("/role",this.Form).then(res=>{
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
                // this.$refs.addFormRef.resetFields()
                this.isShowAddUser=false
            },

            //展示用户编辑对话框
            showEditDialog (row) {
                this.isShowAddUser = true;
                this.Form=row
                // this.editDialogVisible=true
            },
            //展示分配菜单对话框
            selectMenu(role){
                this.isShowMenu=true
                this.roleId=role.id
                this.roleFlag=role.flag

                this.request.get("/menu", {
                    params: {
                        name:this.name,
                    }
                }).then(res => {
                    this.MenuData = res.data;
                    this.expanded=this.MenuData.map(v=>v.id)
                })

                this.request.get("/role/roleMenu/"+this.roleId).then(res => {
                    this.checked=res.data
                    //this.isShowMenu=true
                    // this.request.get("/menu/ids").then(r=>{
                    //     const ids=r.data;
                    //     ids.forEach(id=>{
                    //         if(!this.checked.includes(id)){
                    //             this.$refs.tree.setChecked(id,false)
                    //         }
                    //     })
                    // })
                    this.ids.forEach(id => {
                        if (!this.checked.includes(id)) {
                            // 可能会报错：Uncaught (in promise) TypeError: Cannot read properties of undefined (reading 'setChecked')
                            this.$nextTick(() => {
                                this.$refs.tree.setChecked(id, false)
                            })
                        }
                    })
                    this.isShowMenu = true
                })

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
                await this.request.delete('/role/'+id).then(res=>{
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
                    await this.request.post('/role/del/batch',ids).then(res=>{
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
            saveRoleMenu(){
                this.request.post("/role/roleMenu/"+this.roleId,this.$refs.tree.getCheckedKeys()).then(res=>{
                   if(res.code!=='200'){
                       this.$message.error(res.msg)
                   }else{
                       this.$message.success("绑定成功")
                       this.isShowMenu=false
                       if(this.roleFlag==='ROLE_ADMIN'){
                           this.$store.commit("logout")
                       }
                   }
                })
            }
        }
    }
</script>

<style scoped>

</style>