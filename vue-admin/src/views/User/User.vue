<template>
    <div>

        <div style="margin: 10px 0">
            <span style="font-size: 14px;padding-right: 5px">用户:</span><el-input style="width: 200px" placeholder="请输入用户名" suffix-icon="el-icon-search" size="mini" v-model="username"></el-input>
            <span style="font-size: 14px;padding-left: 5px" >账号:</span><el-input style="width: 200px" placeholder="请输入账号名" suffix-icon="el-icon-user" size="mini" v-model="account" class="ml-5"></el-input>
            <span style="font-size: 14px;padding-left: 5px" >负责区域:</span><el-input style="width: 200px" placeholder="请输入负责区域" suffix-icon="el-icon-position" size="mini" v-model="territory" class="ml-5"></el-input>
            <el-button class="ml-5" type="primary" size="mini" @click="onSearch">搜索</el-button>
            <el-button class="ml-5" type="warning" size="mini" @click="onReset">重置</el-button>
        </div>
        <div style="margin: 10px 0">
            <el-button type="primary" size="mini" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-button type="danger" size="mini" @click="delBatch()">批量删除 <i class="el-icon-remove-outline"></i></el-button>
            <el-upload
                    class="upload-demo"
                    action="http://localhost:8088/user/import"
                    accept="xlsx"
                    style="display: inline-block; margin: 5px"
                    :show-file-list="false"
                    :on-success="handleExcelSuccessImport"
                   >
                <el-button type="primary" size="mini" >导入 <i class="el-icon-bottom"></i></el-button>
            </el-upload>

            <el-button type="primary" size="mini" @click="handleExcelExport">导出 <i class="el-icon-top"></i></el-button>
        </div>
        <el-table :data="tableData" ref="multipleTable" stripe border :header-cell-class-name="headerBg" @selection-change="handleSelectionChange" size="mini">
            <el-table-column
                    type="selection"
                    width="55">
            </el-table-column>
            <el-table-column prop="id" label="id" width="60">
            </el-table-column>
            <el-table-column prop="username" label="用户名" width="140">
            </el-table-column>
            <el-table-column prop="account" label="账号名" width="120">
            </el-table-column>
            <el-table-column prop="role" label="角色" width="120">
                <template slot-scope="scope">
                    <el-tag type="primary" v-if="scope.row.role==='ROLE_USER'" size="small">普通用户</el-tag>
                    <el-tag type="warning" v-if="scope.row.role==='ROLE_PERSON'" size="small">采集人</el-tag>
                    <el-tag type="success" v-if="scope.row.role==='ROLE_ADMIN'" size="small">管理员</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="territory" label="负责地区">
            </el-table-column>
            <el-table-column prop="assessTime" label="修改时间">
            </el-table-column>
            <el-table-column prop="phone" label="手机号">
            </el-table-column>
            <el-table-column label="操作"  width="200" align="center">
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
        <!--        新增用户弹窗-->
        <el-dialog
                title="用户信息"
                :visible.sync="isShowAddUser"
                width="35%"
                @close="addDialogClosed"
        >
            <!--          内容主体区-->
            <el-form v-bind:model="addForm" :rules="addFormRules" ref="addFormRef" label-width="70px">
                <el-form-item label="用户名" prop="username">
                    <el-input  v-model="addForm.username" ></el-input>
                </el-form-item>
                <el-form-item label="账号名" prop="account">
                    <el-input  v-model="addForm.account" ></el-input>
                </el-form-item>
                <el-form-item label="角色选择" >
                        <el-select v-model="addForm.role" placeholder="请选择角色" style="width: 100%;">
                            <el-option
                                    v-for="item in roles"
                                    :key="item.name"
                                    :label="item.name"
                                    :value="item.flag">
<!--                                <i :class="item.value">{{item.name}}</i>-->
                            </el-option>
                        </el-select>
                </el-form-item>
                <el-form-item label="负责区域" prop="territory">
                    <el-input v-model="addForm.territory"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input v-model="addForm.phone"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button @click="isShowAddUser=false">取 消</el-button>
            <el-button type="primary" @click="addUser">确 定</el-button>
          </span>
        </el-dialog>
    </div>
</template>

<script>
    import eventTotal from "../../util/eventTotal";
    export default {
        name: "User",
        data(){
            var checkMobile=(roles,value,cb)=>{
                //验证手机号的正则表达式
                const regMobile = /^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57]|16[6])[0-9]{8}$/
                if(regMobile.test(value)){
                    return cb()
                }
                cb(new Error('请输入合法的手机号'))
            }
            return{
                tableData:[],

                headerBg:'headerBg',
                total:0,
                pageSize:8,
                pageNum:0,
                account: '',
                username:'',
                territory: '',
                isShowAddUser:false, //展示新增弹窗
                editDialogVisible:false, //展示修改弹窗
                addForm:{},
                roles:[],
                //修改用户
                editForm:{},
                //添加表单验证规则
                addFormRules:{
                    username:[
                        {required:true,message:'请输入用户名',trigger:'blur'},
                        {min:3,max:10,message:'用户名长度要在3~10字符之间',trigger:'blur'}
                    ],
                    account:[
                        {required:true,message:'请输入账号名',trigger:'blur'},
                        {min:2,max:15,message:'密码长度要在2~15字符之间',trigger:'blur'}
                    ],
                    phone:[
                        {required:true,message:'请输入手机号',trigger:'blur'},
                        { validator: checkMobile, trigger: 'blur' }
                    ],
                },
                //修改表单的验证规则
                editFormRules:{
                    phone:[
                        {required:true,message:'请输入手机号',trigger:'blur'},
                        { validator: checkMobile, trigger: 'blur' }
                    ],
                },
                multipleSelection: []
            }
        },
        created() {
            this.loading()

        },
        methods:{
            //加载数据
            async loading() {
                await this.request.get("/user/page", {
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        username: this.username,
                        territory: this.territory,
                        account: this.account
                    }
                }).then(res => {
                    this.tableData = res.data.records;
                    this.total = res.data.total
                    console.log(res)
                    eventTotal.$emit("add",this.total)
                })
                this.request.get("/role").then(res=>{
                    if(res.code==='200'){
                        this.roles=res.data
                    }
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
                this.username=''
                this.account=''
                this.territory=''
                this.loading()
            },
            handleAdd(){
                this.isShowAddUser=true
                this.addForm={}
            },
            //添加用户
            addUser(){
                this.$refs.addFormRef.validate(async valid=>{
                    if(!valid) return
                    this.request.post("/user",this.addForm).then(res=>{
                        if(res.code!=='200'){
                            this.$message.error('用户添加失败!')
                        }else{
                            this.$message.success('用户添加或修改成功!')
                        }
                        //隐藏添加的对话框
                        this.isShowAddUser=false
                        //重新添加用户列表
                        this.loading()
                    })
                })
            },
            //添加用户对话框关闭事件
            addDialogClosed(){
                // //拿到表单对象,然后重置表单数据
                this.$refs.addFormRef.resetFields()
                this.isShowAddUser=false
            },

            //展示用户编辑对话框
            showEditDialog (row) {

                this.isShowAddUser = true;
                this.addForm=row
                // this.editDialogVisible=true
            },
            //根据id删除对应的用户id
            async removeUserById(id) {
                const confirmResult=await this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).catch ( err => err)
                if(confirmResult!=='confirm'){
                    return this.$message.info('已取消删除')
                }
                await this.request.delete('/user/'+id).then(res=>{
                    if(res.code!=='200'){
                        return this.$message.error('删除用户失败!')
                    }else{
                        this.$message.success('删除用户成功!')
                    }
                    //刷新数据列表
                    this.loading()
                })

            },
            //批量删除
            async delBatch(){
                    const confirmResult=await this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).catch ( err => err)
                    if(confirmResult!=='confirm'){
                        return this.$message.info('已取消删除')
                    }
                    let ids=this.multipleSelection.map(v=>v.id)
                    await this.request.post('/user/del/batch',ids).then(res=>{
                        if(res.code!=='200'){
                            this.$message.error('批量删除用户失败!')

                        }else{
                            this.$message.success('批量删除用户成功!')
                            //刷新数据列表
                            this.loading()
                        }

                    })
            },
            handleSelectionChange(val){
                console.log(val);
                this.multipleSelection = val;
            },
            //导出
            handleExcelExport(){
                window.open("http://localhost:8088/user/export")
            },
            //导入成功
            handleExcelSuccessImport(){
                this.$message.success('导入用户成功!')
                this.loading();
            },

        }
    }
</script>

<style scoped>

</style>