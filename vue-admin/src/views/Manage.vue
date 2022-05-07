<template>
  <el-container style="min-height:100vh;">
    <el-aside :width="sideWidth+'px'" style="box-shadow: 2px 0 6px rgba(0 21 41 0.35); overflow-x:hidden;">
      <v-Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" ></v-Aside>
    </el-aside>

    <el-container>
      <el-header style="border-bottom: 1px solid #ccc ;text-align: left; font-size: 18px;">
        <v-header  :collapse-btn-class="collapseBtnClass"  @asideCollapse="collapse" :userInfo="userInfo"></v-header>
      </el-header>

      <el-main>
<!--        当前页面的子路由在 router-view 里面显示-->
       <router-view @refreshUser="getUser()"></router-view>
      </el-main>

      <!--        修改用户弹窗-->
<!--      <el-dialog title="修改用户" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed" >-->
<!--        <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="70px">-->
<!--          <el-form-item label="用户名">-->
<!--            <el-input v-model="editForm.username" :disabled="true"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item label="账号名" prop="account">-->
<!--            &lt;!&ndash;              prop:用于指定校验规则&ndash;&gt;-->
<!--            <el-input v-model="editForm.account"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item label="负责区域" prop="territory">-->
<!--            <el-input v-model="editForm.territory"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item label="手机号" prop="phone">-->
<!--            <el-input v-model="editForm.phone"></el-input>-->
<!--          </el-form-item>-->
<!--        </el-form>-->
<!--        <span slot="footer" class="dialog-footer">-->
<!--            <el-button @click="editDialogVisible = false">取 消</el-button>-->
<!--            <el-button type="primary"  @click="editUserInfo">确 定</el-button>-->
<!--          </span>-->
<!--      </el-dialog>-->
    </el-container>
  </el-container>
</template>

<script>

  // import request from "../util/request";
  import {findPage} from "../api/api-list";
  import Aside from "../components/Aside";
  import Header from "../components/Header";

  export default {
    name: 'Home',
    components:{
      'v-Aside':Aside,
      'v-header':Header,
    },
    data(){
      return {
        tableData: [],
        collapseBtnClass:'el-icon-s-fold',
        isCollapse:false,
        sideWidth:200,
        logoTextShow:true,
        userInfo:{}
      }
    },
      created() {
        this.getUser()
      },
      methods:{
      collapse(){
        if(!this.isCollapse){ //收缩
          this.isCollapse=!this.isCollapse
          this.collapseBtnClass='el-icon-s-unfold'
          this.sideWidth=60
          this.logoTextShow=false
        }else{ //展开
          this.isCollapse=false;
          this.collapseBtnClass='el-icon-s-fold'
          this.sideWidth=200
          this.logoTextShow=true
        }
      },
      //监听修改用户对话框关闭事件
      editDialogClosed(){
        //拿到表单对象,然后重置表单数据
        this.$refs.editFormRef.resetFields()
      },
      //修改用户提交事件
      editUserInfo(){
        //预校验
        this.$refs.editFormRef.validate(
            //返回的promise
            async valid=>{
             if(!valid)return
            //发起修改用户信息数据请求
            await this.request.post('/user'+this.editForm.id,{username:this.editForm.username,account:this.editForm.account,phone:this.editForm.phone,territory:this.editForm.territory}).then(res=>{
            if(res){
            return this.$message.error('更新用户信息失败!')
            }
           //关闭对话框
           this.loading()
           //提示用户修改成功信息
           this.$message.success('更新用户信息成功')
         })
        })
      },
      getUser(){
          let username=localStorage.getItem("userInfo")?JSON.parse(localStorage.getItem("userInfo")).username:{}
          this.request.get("/user/username/"+username).then(res=>{
              this.userInfo=res.data
          })
      }
    }
  }
</script>

<style>
  .headerBg{
    background: #eee!important;
  }
  .el-main{
    padding-bottom: 0;
  }
</style>
