<template>
  <body id="poster">
    <el-form class="login-container" label-position="left" label-width="0px">
      <h3 class="login_title">用户登录</h3>
      <el-form-item>
        <el-input type="text" clearable v-model="loginForm.username" auto-complete="off" placeholder="账号/手机号"></el-input>
      </el-form-item>

      <el-form-item>
        <el-input show-password v-model="loginForm.password" @keyup.enter.native="login" auto-complete="off" placeholder="密码"></el-input>
      </el-form-item>

      <el-form-item style="width: 100%">
        <el-button type="primary" style="width: 100%;border: none" v-on:click="login" :loading="loading">登录</el-button>
      </el-form-item>

      <el-form-item style="width: 100%">
        <el-button type="primary" style="width: 100%;border: none" v-on:click="">注册</el-button>
      </el-form-item>
    </el-form>
  </body>
</template>

<script >
  export default {
    name: "Login",
    data() {
      return {
        loading : false,
        loginForm: {
            username: '',
            password: ''
        },
        responseResult: []
      }
    },
    methods: {
      login: function() {
        // 验证
        if (this.loginForm.username == '' || this.loginForm.username == '') {
          this.$message({
            message: '账号密码不能为空',
            type: 'warning'
          })
          return
        }
        // 请求
        this.loading = true
        this.$api.login(this.loginForm).then((res) => {
          this.loading = false
          if (!res.data) {
            this.$message.error('账号或密码错误');
          } else {
            this.$message({
              message: '登录成功',
              type: 'success'
            })
            sessionStorage.setItem('token', res.data.token)
            sessionStorage.setItem('name', res.data.name)
            setTimeout(() => {
              //进入主页
              this.$router.push('/home')
            }, 500)
          }
        }).catch((res) => {
          this.$message.error('访问失败');
          console.log(res)
        })
      }
    }
  }
</script>

<style less='scss'>
  #poster {
    background:url("../assets/bg.jpg") no-repeat;
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }
  body{
    margin: 0px;
    padding: 0;
  }

  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 200px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 10px #bdbaba;
  }

  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }
</style>