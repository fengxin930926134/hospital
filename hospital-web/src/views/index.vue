<template>
  <el-container style="height: 100%;">
    <!-- 左侧 -->
    <el-aside width="auto" style="background-color: #545c64; ">
      <NavMenu :isCollapse="isCollapse"/>
    </el-aside>
    <!-- 头部 -->
    <el-container style="height: 100vh;">
      <el-scrollbar style="height: 100%; width: 100%;">
        <el-header style="height: 50px; padding: 0px;">
          <!-- 收缩按钮 -->
          <div v-if="isCollapse" class="i-button" @click="collapse"><i class="el-icon-s-unfold"/></div>
          <div v-else class="i-button" @click="collapse"><i class="el-icon-s-fold" /></div>
          <!-- 面包屑 -->
          <el-breadcrumb separator="/" style="float: left;" class="header-item">
            <el-breadcrumb-item v-for="item in levelList" :key="item.path" :to="item.path">{{item.meta.name}}</el-breadcrumb-item>
          </el-breadcrumb>
          <!-- 右方设置 -->
          <el-dropdown class="header-item" :show-timeout="100" @command="handleCommand">
            <span class="el-dropdown-link">
              <i class="el-icon-setting" style="margin-right: 15px"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="1">个人信息</el-dropdown-item>
              <el-dropdown-item command="2">修改密码</el-dropdown-item>
              <el-dropdown-item command="3">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <span style="margin-right: 20px;" class="header-item">
              {{ name }}
          </span>
        </el-header>
        <!-- 主要 -->
        <el-main>
          <router-view/>
        </el-main>
      </el-scrollbar>
    </el-container>
  </el-container>
</template>

<script>
  import { NavMenu } from '@/components'

  export default {
    name: 'index',
    data() {
      return {
        isCollapse: false,
        levelList: [],
        name: ''
      }
    },
    watch: {
      '$route': 'getBreadcrumb'
    },
    //在实例创建完成后被立即调用
    created() {
      this.getBreadcrumb()
      this.name = sessionStorage.getItem('name')
    },
    methods: {
      //收缩菜单
      collapse: function() {
        this.isCollapse = !this.isCollapse
      },
      //设置按钮事件
      handleCommand: function(command) {
        switch(command) {
          case '1':{
            this.$router.push("/info")
            break
          }
          case '2':{
            this.$router.push("/upPassword")
            break
          }
          //退出登录
          case '3': {
            this.$api.logout()
            this.$router.push("/login")
            break
          }
        }
      },
      getBreadcrumb() {
        //过滤数据
        let matched = this.$route.matched.filter(item => item.name)
        const first = matched[0];
        if (first && first.name.trim().toLocaleLowerCase() !== 'home') {
            matched = [{ path: '/home', meta: { name: '首页' }}].concat(matched)
        }
        //导航数据
        this.levelList = matched
      }
    },
    components: {
      NavMenu
    }
  }
</script>

<style>
  * {
    margin: 0;
    padding: 0;
  }

  .el-header {
    background-color: white;
    color: #333;
    border: solid 1px #DDDDDD;
    border-top: 0;
    border-left: 0;
    border-right: 0;
    box-shadow: 0px 0px 8px gray;
    }

  .i-button {
    width: 50px;
    height: 50px;
    font-size: 25px;
    text-align: center;
    display: flex;
    justify-content: center;
    flex-direction: column;
    float: left;
    margin-right: 20px;
  }

  .header-item {
    height: 50px;
    float: right;
    display: flex;
    align-items: center;
  }

  .i-button:hover {
    background: #f3f3f3;
  }

  .el-aside {
    color: #333;
  }
</style>
