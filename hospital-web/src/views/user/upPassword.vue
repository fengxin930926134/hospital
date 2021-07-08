<template>
  <div style="width: 60%; margin-top: 25px;">
    <el-form :model="form" :rules="rules" ref="passwordForm" label-width="100px">

      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" clearable placeholder="请输入用户名或手机号"></el-input>
      </el-form-item>

      <el-form-item label="旧密码" prop="password">
        <el-input v-model="form.password" show-password placeholder="请输入旧密码"></el-input>
      </el-form-item>

      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" show-password placeholder="请输入新密码"></el-input>
      </el-form-item>

      <el-form-item label="确认新密码" prop="rePassword">
        <el-input v-model="form.rePassword" show-password placeholder="请再次输入新密码"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">保存</el-button>
        <el-button @click="resetForm('passwordForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    name: 'UpPassword',
    data() {
      var validateRePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入新密码'));
        } else if (value !== this.form.newPassword) {
          callback(new Error('两次输入的密码不一致'));
        } else if (this.form.password === this.form.newPassword) {
          callback(new Error('新密码和旧密码相同'));
        } else {
          callback();
        }
      };
      return {
        form: {
          password: '',
          newPassword: '',
          rePassword: '',
          username: ''
        },
        rules: {
          password: [
            { required: true, message: '请输入旧密码', trigger: 'change' }
          ],
          newPassword: [
            { required: true, message: '请输入新密码', trigger: 'change' }
          ],
          //trigger验证方式 change提交后验证
          rePassword: [
            { required: true, validator: validateRePass, trigger: 'change' }
          ],
          username: [
            { required: true, message: '请输入账号或手机号', trigger: 'change' }
          ]
        }
      }
    },
    methods: {
      //提交表单
      onSubmit: function() {
        //验证
        this.$refs.passwordForm.validate((valid) => {
          if(valid) {
            this.$api.updatePassword(this.form).then((res) => {
              if (res.data) {
                this.$message({
                  message: '保存成功',
                  type: 'success'
                });
                this.resetForm('passwordForm')
              } else {
                this.$message.error('用户或密码输入错误');
              }
            }).catch((res) => {
              console.log(res)
              this.$message.error('保存失败');
            })
          }
        })
      },
      //对整个表单进行重置，将所有字段值重置为初始值并移除校验结果
      resetForm: function(formName) {
        this.$refs[formName].resetFields();
      }
    }
  }
</script>

<style>
</style>
