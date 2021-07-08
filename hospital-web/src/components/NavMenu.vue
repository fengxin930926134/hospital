<template>
  <div class="div-w">
    <el-menu :default-active="activeIndex" router class="el-menu-vertical-demo"  :collapse="isCollapse"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409EFF">
      <el-menu-item index="/home">
        <i class="el-icon-s-home"></i>
        <span slot="title">首页</span>
      </el-menu-item>
      <el-menu-item index="/info">
        <i class="el-icon-user"></i>
        <span slot="title">个人信息</span>
      </el-menu-item>
      <el-submenu index="/self">
        <template slot="title">
          <i class="el-icon-position"></i>
          <span>自助系统</span>
        </template>
        <el-menu-item index="/appointment">门诊挂号</el-menu-item>
        <el-menu-item index="/pay">自助缴费</el-menu-item>
        <el-menu-item index="/record">挂号记录</el-menu-item>
      </el-submenu>
      <el-menu-item index="/reg">
        <i class="el-icon-edit-outline"></i>
        <span slot="title">挂号信息</span>
      </el-menu-item>
      <el-menu-item index="/prescribe">
        <i class="el-icon-document-checked"></i>
        <span slot="title">处方管理</span>
      </el-menu-item>
      <el-menu-item index="/checklist">
        <i class="el-icon-document-checked"></i>
        <span slot="title">检查单管理</span>
      </el-menu-item>
      <el-menu-item index="/history">
        <i class="el-icon-s-order"></i>
        <span slot="title">查看病历</span>
      </el-menu-item>
      <el-menu-item index="/drugstore">
        <i class="el-icon-first-aid-kit"></i>
        <span slot="title">药房管理</span>
      </el-menu-item>
      <el-menu-item index="/drug">
        <i class="el-icon-lollipop"></i>
        <span slot="title">药品管理</span>
      </el-menu-item>
      <el-menu-item index="/dept">
        <i class="el-icon-postcard"></i>
        <span slot="title">科室管理</span>
      </el-menu-item>
      <el-menu-item index="/medical">
        <i class="el-icon-document-add"></i>
        <span slot="title">医技管理</span>
      </el-menu-item>
      <el-menu-item index="/user">
        <i class="el-icon-user-solid"></i>
        <span slot="title">用户管理</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 202px;
  min-height: 400px;
}

.div-w {
  height: 100%; background-color: #304156; width: 99%;
  overflow-x: hidden;
}

.el-scrollbar .el-scrollbar__wrap {overflow-x: hidden;}
</style>

<script>
  export default {
    name: 'NavMenu',
    props: {
      isCollapse: {
        default: false
      }
    },
    data () {
      return {
        activeIndex: '/home'
      }
    },
    // 通过监听器解决  导航菜单在页面刷新时会回到默认项
    watch: {
      '$route' (to, from) {
        // 监听路由的变化  如果路由发生改变则当前tab栏默认值也相应改变
        this.activeIndex = to.path
        console.log(to.path)
      }
    },
    created: function() {
      this.activeIndex = this.$route.path
      console.log(this.$route.path)
    },
    methods: {
    }
  }
</script>
