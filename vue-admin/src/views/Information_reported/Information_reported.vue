<template>
    <div>
        <div style="margin: 10px 0">
            <span style="font-size: 14px;padding-right: 5px">企业名称:</span><el-input style="width: 200px" placeholder="请输入企业名" suffix-icon="el-icon-search" size="mini" v-model="enterpriseName"></el-input>
            <span style="font-size: 14px;padding-left: 5px" >企业类型:</span><el-input style="width: 200px" placeholder="请输入企业类型" suffix-icon="el-icon-more-outline" size="mini" v-model="classification" class="ml-5"></el-input>
            <span style="font-size: 14px;padding-left: 5px" >负责区域:</span><el-input style="width: 200px" placeholder="请输入负责区域" suffix-icon="el-icon-position" size="mini" v-model="territory" class="ml-5"></el-input>
            <el-button class="ml-5" type="primary" size="mini" @click="onSearch">搜索</el-button>
            <el-button class="ml-5" type="warning" size="mini" @click="onReset">重置</el-button>
        </div>
        <div style="margin: 10px 0">
            <el-button type="primary" size="mini" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-button type="danger" size="mini" @click="delBatch()">批量删除 <i class="el-icon-remove-outline"></i></el-button>
            <el-upload
                    class="upload-demo"
                    action="http://localhost:8088/report/import"
                    accept="xlsx"
                    style="display: inline-block; margin: 5px"
                    :show-file-list="false"
                    :on-success="handleExcelSuccessImport"
            >
                <el-button type="primary" size="mini" >导入 <i class="el-icon-bottom"></i></el-button>
            </el-upload>

            <el-button type="primary" size="mini" @click="handleExcelExport">导出 <i class="el-icon-top"></i></el-button>
        </div>
        <el-table  :data="tableData" ref="multipleTable" stripe border :header-cell-class-name="headerBg" @selection-change="handleSelectionChange" size="mini">
            <el-table-column
                    type="selection"
                    width="55">
            </el-table-column>
            <el-table-column prop="id" label="序号" width="60">
            </el-table-column>
            <el-table-column prop="address" label="隐患具体地点">
            </el-table-column>
            <el-table-column prop="classification" label="隐患企业分类" width="100">
            </el-table-column>
            <el-table-column prop="territory" label="隐患区域">
            </el-table-column>
            <el-table-column prop="enterpriseName" label="隐患企业名称">
            </el-table-column>
            <el-table-column prop="contactPerson" label="负责人姓名">
            </el-table-column>
            <el-table-column prop="term" label="整改期限">
            </el-table-column>
            <el-table-column prop="enable" label="启用" >
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.enable" @change="changeEnable(scope.row)" active-color="#13ce66" inactive-color="#ccc"></el-switch>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="上报时间" width="150">
            </el-table-column>
            <el-table-column prop="updateTime" label="更新时间" width="150">
            </el-table-column>
            <el-table-column prop="phone" label="手机号" width="100">
            </el-table-column>
            <el-table-column prop="detail" label="隐患详情" width="150">
            </el-table-column>
            <el-table-column prop="proposal" label="整改建议">
            </el-table-column>
            <el-table-column prop="result" label="整改结果">
            </el-table-column>
            <el-table-column prop="measures" label="整改措施">
            </el-table-column>
            <el-table-column prop="imageUrl" label="图片地址">
            </el-table-column>
            <el-table-column label="操作"  width="280" align="center" >
                <template slot-scope="scope">
                    <el-button type="success" size="mini" @click="showEditDialog(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
                    <el-button type="danger" size="mini" @click="removeUserById(scope.row.id)">删除 <i class="el-icon-remove-outline"></i></el-button>
                    <el-button type="primary" size="mini" @click="feedbackHistory(scope.row.id)">反馈历史 <i class="el-icon-chat-line-square"></i></el-button>
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
                title="隐患信息"
                :visible.sync="isShowAddInfo"
                width="35%"
                @close="addDialogClosed"
        >
            <!--          内容主体区-->
            <el-form :rules="FormRules" v-bind:model="Form"  ref="FormRef" label-width="110px">
                <el-form-item label="隐患具体地点" prop="address">
                    <el-input  v-model="Form.address" ></el-input>
                </el-form-item>
                <el-form-item label="隐患企业分类" prop="classification">
                    <el-input  v-model="Form.classification" ></el-input>
                </el-form-item>
                <el-form-item label="负责区域" prop="territory">
                    <el-input v-model="Form.territory"></el-input>
                </el-form-item>
                <el-form-item label="隐患企业名称" prop="enterpriseName">
                    <el-input v-model="Form.enterpriseName"></el-input>
                </el-form-item>
                <el-form-item label="负责人" prop="contactPerson">
                    <el-input v-model="Form.contactPerson"></el-input>
                </el-form-item>
                <el-form-item label="整改期限" prop="term">
                    <el-input v-model="Form.term"></el-input>
                </el-form-item>
<!--                <el-form-item label="创建时间" prop="createTime">-->
<!--&lt;!&ndash;                    <el-input v-model="Form.createTime"></el-input>&ndash;&gt;-->
<!--                    <el-date-picker-->
<!--                            v-model="Form.createTime"-->
<!--                            type="datetime"-->
<!--                            placeholder="选择日期时间">-->
<!--                    </el-date-picker>-->
<!--                </el-form-item>-->
                <el-form-item label="隐患详情" prop="detail">
                    <el-input v-model="Form.detail"></el-input>
                </el-form-item>
                <el-form-item label="整改建议" prop="proposal">
                    <el-input v-model="Form.proposal"></el-input>
                </el-form-item>
                <el-form-item label="整改结果" prop="result">
                    <el-input v-model="Form.result"></el-input>
                </el-form-item>
                <el-form-item label="整改措施" prop="measures">
                    <el-input v-model="Form.measures"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input v-model="Form.phone"></el-input>
                </el-form-item>
                <el-form-item label="图片地址" prop="imageUrl">
                    <el-input v-model="Form.imageUrl" disabled></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button @click="isShowAddInfo=false">取 消</el-button>
            <el-button type="primary" @click="addInfo">确 定</el-button>
          </span>
        </el-dialog>
        <el-dialog  title="反馈历史"
                    :visible.sync="isShowfeedback"
                    width="35%"
                    @close="DialogClosed">
            <div class="block">
                <div class="radio">
                    排序：
                    <el-radio-group v-model="reverse">
                        <el-radio :label="true">倒序</el-radio>
                        <el-radio :label="false">正序</el-radio>
                    </el-radio-group>
                </div>

                <el-timeline :reverse="reverse" style="padding-top: 10px">
                    <el-timeline-item
                            v-for="(item, index) in activities"
                            :key="index"
                            :timestamp="item.time">
                        <span style="color:#aaaaaa;">{{item.account}} : </span>{{item.content}}

                    </el-timeline-item>
                </el-timeline>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "Information_reported",
        data(){
            return{
                tableData:[],
                Form:{},
                total:0,
                pageSize:8,
                pageNum:0,
                isShowAddInfo:false, //展示新增弹窗
                multipleSelection: [],
                headerBg:'headerBg',
                enterpriseName:'',
                classification:'',
                result:'',
                territory:'',
                imageUrl:'',
                isShowfeedback:false, //展示反馈历史
                reverse:true,
                activities:[],
                //添加表单验证规则
                FormRules:{
                    enterpriseName:[
                        {required:true,message:'请输入企业名称',trigger:'blur'},
                    ],
                },

            }
        },
        created() {
            this.loading()
        },
        methods:{
            //加载数据
            async loading() {
                await this.request.get("/report/page", {
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        enterpriseName:this.enterpriseName,
                        classification:this.classification,
                        result:this.result,
                        territory:this.territory,
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
                this.enterpriseName=''
                this.classification=''
                this.loading()
            },
            handleAdd(){
                this.isShowAddInfo=true
                this.Form={}
            },
            //根据id删除对应的用户id
            async removeUserById(id) {
                const confirmResult=await this.$confirm('此操作将永久删除该信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).catch ( err => err)
                if(confirmResult!=='confirm'){
                    return this.$message.info('已取消删除')
                }
                await this.request.delete('/report/'+id).then(res=>{
                    if(res.code!=='200'){
                        return this.$message.error('删除信息失败!')
                    }else{
                        this.$message.success('删除信息成功!')
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
                await this.request.post('/report/del/batch',ids).then(res=>{
                    if(!res.data){
                        this.$message.error('批量删除信息失败!')

                    }else{
                        this.$message.success('批量删除信息成功!')
                        //刷新数据列表
                        this.loading()
                    }

                })
            },

            //添加信息
            addInfo(){
                this.$refs.FormRef.validate(async valid=>{
                    if(!valid) return
                    this.request.post("/report",this.Form).then(res=>{
                        if(!res.data){
                            this.$message.error('信息添加失败!')
                        }else{
                            this.$message.success('信息添加或修改成功!')
                        }
                        //隐藏添加的对话框
                        this.isShowAddInfo=false
                        //重新添加用户列表
                        this.loading()
                    })
                })
            },
            //添加信息对话框关闭事件
            addDialogClosed(){
                //拿到表单对象,然后重置表单数据
                this.$refs.FormRef.resetFields()
                this.isShowAddInfo=false
            },
                DialogClosed(){
                this.isShowfeedback=false
            },
            //展示用户编辑对话框
            showEditDialog (row) {

                this.isShowAddInfo = true;
                this.Form=row
                // this.editDialogVisible=true
            },
            handleSelectionChange(val){
                console.log(val);
                this.multipleSelection = val;
            },
            //导出
            handleExcelExport(){
                window.open("http://localhost:8088/report/export")
            },
            //导入成功
            handleExcelSuccessImport(){
                this.$message.success('导入信息成功!')
                this.loading();
            },
            //上报状态
            changeEnable(row){
                // console.log(row)
                this.request.post("/report/update",row).then(res=>{
                    if(res.code==='200'){
                        this.$message.success("更新成功")
                    }
                })
            },
            //反馈历史
            feedbackHistory(id){
                this.isShowfeedback=true
                this.request.get("/comment/tree/"+id).then(res=>{
                    this.activities=res.data
                })
            }
        }
    }
</script>

<style scoped>

</style>