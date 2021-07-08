<template>
  <div style="width: 60%; margin-top: 25px;">
    <el-form ref="form" :model="form" label-width="100px">

      <el-form-item label="用户名">
        <el-input v-model="form.name" clearable placeholder="请输入用户名" :disabled="disabled"></el-input>
      </el-form-item>

      <el-form-item label="性别" >
        <el-radio-group v-model="form.sex" :disabled="disabled">
          <el-radio :label="1" border>男</el-radio>
          <el-radio :label="2" border>女</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="手机号码">
        <el-input v-model="form.phone" clearable placeholder="请输入手机号码" :disabled="disabled" :maxlength="11"></el-input>
      </el-form-item>

      <el-form-item label="科室">
        <el-select v-model="form.deptId" clearable placeholder="请选择科室" :disabled="true" style="width: 100%;">
          <el-option
            v-for="item in deptList"
            :key="item.deptId"
            :label="item.deptName"
            :value="item.deptId">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="出生日期">
        <el-date-picker type="date" placeholder="选择出生日期" :disabled="disabled" v-model="form.birthDate" style="width: 100%;"/>
      </el-form-item>

      <el-form-item label="权限">
        <el-tag
        v-for="(role, index) in form.roleList"
        :set="item = getRole(role)"
        :key="index"
        :type="item.type"
        style="margin-right: 0.5rem;">
          {{item.name}}
        </el-tag>
      </el-form-item>

      <el-form-item>
        <el-button v-if="disabled" type="primary" @click="editOrdisabled">编辑</el-button>
        <el-button v-if="!disabled" type="primary" @click="onSubmit">保存</el-button>
        <el-button :disabled="disabled" @click="editOrdisabled">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import { roleEnum } from '../../utils/enum.js'

  export default {
    name: 'Info',
    data() {
      return {
        disabled: true,
        form: {
          id: 0,
          name: '',
          phone: '',
          birthDate: '',
          role: '',
          deptId: null,
          sex: "1"
        },
        deptList: []
      }
    },
    created: function() {
      this.getUserInfo()
      this.$api.getDeptAll().then((res) => {
        this.$data.deptList = res.data
      }).catch((res) => {
        console.log(res)
        this.$message.error('获取部门信息失败');
      })
    },
    methods: {
      //启用禁用
      editOrdisabled: function() {
        this.disabled = !this.disabled
      },
      //提交表单
      onSubmit: function() {
        this.$api.updateUserInfo(this.form)
        .then((res) => {
          if (res.data) {
            this.$message({
              message: '保存成功',
              type: 'success'
            });
            this.editOrdisabled()
          } else this.$message.error('保存失败')
        }).catch((res) => {
          console.log(res)
          this.$message.error('保存失败')
        })
      },
      //获取权限相关值
      getRole: function (role) {
        // `this` 指向 vm 实例
        for (var i = 0; i < roleEnum.length; i++) {
          if (roleEnum[i].key == role) {
            return roleEnum[i]
          }
        }
      },
      //获取用户信息
      getUserInfo: function() {
        this.$api.getUserInfo().then((res) => {
          this.$data.form = res.data
        }).catch((res) => {
          console.log(res)
          this.$message.error('获取个人信息失败');
        })
      }
    }
  }
</script>

<style>
  .el-select-dropdown .el-scrollbar .el-scrollbar__wrap{
    overflow: scroll;
  }
</style>
