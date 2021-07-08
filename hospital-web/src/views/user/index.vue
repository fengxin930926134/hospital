<template>
  <div class="content">
    <el-dialog :before-close="resetForm" :title="dialogTitle" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal="false">

      <el-form label-width="80px" :model="dialogForm" :rules="rules" ref="dialogForm">
        <el-form-item label="账号" prop="username">
          <el-input v-model="dialogForm.username" clearable placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="dialogForm.password" clearable placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="name">
          <el-input v-model="dialogForm.name" clearable placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="性别" >
          <el-radio-group v-model="dialogForm.sex">
            <el-radio :label="1" border>男</el-radio>
            <el-radio :label="2" border>女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="dialogForm.phone" clearable placeholder="请输入手机号" :maxlength="11" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="科室">
          <el-select v-model="dialogForm.deptId" clearable placeholder="请选择科室" clearable filterable style="width: 100%;">
            <el-option
              v-for="item in deptList"
              :key="item.deptId"
              :label="item.deptName"
              :value="item.deptId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="出生日期">
          <el-date-picker type="date" placeholder="请选择出生日期" v-model="dialogForm.birthDate" style="width: 100%;"/>
        </el-form-item>
        <el-form-item label="角色权限">
          <el-select v-model="dialogForm.roleList" multiple placeholder="请选择角色权限" style="width: 100%;" @keyup.enter.native="submit">
            <el-option
              v-for="item in roleList.values()"
              :key="item.key"
              :label="item.name"
              :value="item.key">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">保 存</el-button>
        <el-button @click="resetForm">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 行内表单查询框 -->
    <el-form :inline="true" :model="form" class="demo-form-inline">
      <el-tooltip class="item" effect="dark" content="开启多选" placement="bottom-start" :open-delay="800" :hide-after="2000">
        <el-button v-if="!check" type="primary" @click="checkbox" icon="el-icon-s-operation"></el-button>
      </el-tooltip>
      <template v-if="check">
        <el-popconfirm
          confirmButtonText='确定'
          cancelButtonText='取消'
          icon="el-icon-info"
          iconColor="red"
          title="确定删除吗？"
          @onConfirm="handleDeletes">
          <el-button type="danger" slot="reference">批量删除</el-button>
        </el-popconfirm>
        <el-button type="primary" @click="toggleSelection">取消</el-button>
      </template>
      <el-divider direction="vertical"></el-divider>
      <el-button type="primary" @click="addUser">添加用户</el-button>
      <el-divider direction="vertical"></el-divider>
      <el-form-item label="用户名称">
        <el-input v-model="form.name" placeholder="用户名称" @keyup.enter.native="getUserInfo"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="getUserInfo">查询</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table
      ref="multipleTable"
      v-loading="loading"
      :data="tableData"
      stripe
      class="table"
      border
      :header-cell-style="centerClass"
      :cell-style="centerClass"
      @selection-change="handleSelectionChange"
      style="width: 100%;">
      <el-table-column
        v-if="check"
        type="selection"
        align="center"
        width="55">
      </el-table-column>
      <el-table-column
        type="index"
        label="序号"
        width="80"
        align="center"
        :index="indexMethod">
      </el-table-column>
      <el-table-column
        prop="id"
        sortable
        label="编号">
      </el-table-column>
      <el-table-column
        prop="username"
        sortable
        label="账号">
      </el-table-column>
      <el-table-column
        prop="password"
        sortable
        label="密码">
      </el-table-column>
      <el-table-column
        prop="name"
        sortable
        label="用户名">
      </el-table-column>
      <el-table-column
        prop="sexName"
        sortable
        label="性别">
      </el-table-column>
      <el-table-column
        prop="phone"
        sortable
        label="手机号">
      </el-table-column>
      <el-table-column
        prop="deptName"
        sortable
        label="科室">
      </el-table-column>
      <el-table-column
        prop="birthDateName"
        sortable
        label="出生日期">
      </el-table-column>
      <el-table-column
        sortable
        label="角色权限">
        <template slot-scope="scope">
          <el-tag
          size="medium"
          v-for="(role, index) in scope.row.roleList"
          :set="item = getRole(role)"
          :key="index"
          :type="item.type"
          style="margin-right: 0.3125rem; margin-bottom: 0.3125rem;">
            {{item.name}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        align="center"
        width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            @click="handleEdit(scope.row)">编辑</el-button>

          <el-popconfirm
            confirmButtonText='确定'
            cancelButtonText='取消'
            icon="el-icon-info"
            iconColor="red"
            title="确定删除吗？"
            @onConfirm="handleDelete(scope.row.id)">
            <el-button
              slot="reference"
              size="mini"
              type="danger">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-row class="pagination">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :page-sizes="page.pageSizes"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="page.total">
      </el-pagination>
    </el-row>
  </div>
</template>



<script>
  import { roleEnum } from '../../utils/enum.js'

  export default {
    name: 'User',
    data() {
      return {
        dialogFormVisible: false,
        loading: true,
        //是否多选
        check: false,
        //多选数据
        multipleSelection: [],
        //表格数据
        tableData: [],
        //分页数据
        page: {
          total: 10,
          pageSize: 10,
          pageSizes: [10, 20, 50, 100, 200]
        },
        //查询表单数据
        form: {
          pageNumber: 1,
          name: '',
          pageSize: 10
        },
        rules: {
          password: [
            { required: true, message: '密码不能为空', trigger: 'change' }
          ],
          username: [
            { required: true, message: '账号不能为空', trigger: 'change' }
          ],
          name: [
            { required: true, message: '用户名不能为空', trigger: 'change' }
          ]
        },
        //弹窗表单
        dialogForm: {},
        //弹窗标题
        dialogTitle: '',
        deptList: [],
        //role
        roleList: [],
      }
    },
    computed: {
      // 计算权限显示内容
    },
    created: function() {
      this.getUserInfo()
      this.roleList = roleEnum
    },
    methods: {
      getUserInfo: function() {
        this.loading = true
        this.$api.getUserAll(this.form).then((res) => {
          if (res.data !== null) {
            this.tableData = res.data.data
            this.page.total = res.data.totalElements
          }
          this.loading = false
        }).catch((res) => {
          this.loading = false
          this.$message.error('获取用户信息失败')
        })
      },

      //获取部门
      getDeptInfo: function() {
        if (this.deptList.length <= 0) {
          this.$api.getDeptAll().then((res) => {
            this.$data.deptList = res.data
          }).catch((res) => {
            console.log(res)
            this.$message.error('获取部门信息失败');
          })
        }
      },

      //取消弹窗
      resetForm: function() {
         this.dialogFormVisible = false
         this.dialogForm = {}
         this.$refs['dialogForm'].resetFields();
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

      // 序号
      indexMethod(index) {
        return index + 1;
      },

      // 居中样式
      centerClass () {
        return 'text-align: center;'
      },

      //提交弹窗表单
      submit: function() {
        console.log(this.dialogForm)
        this.$refs.dialogForm.validate((valid) => {
          if (valid) {
            this.$api.saveUser(this.dialogForm).then((res) => {
              if (res.data) {
                this.$message({
                  message: '保存成功',
                  type: 'success'
                })
                //刷新
                this.getUserInfo()
                this.resetForm()
              } else {
                this.$message.error('保存失败')
              }
            }).catch((res) => {
              this.$message.error('保存失败')
            })
          }
        })
      },

      //编辑
      handleEdit(row) {
        this.dialogFormVisible = true
        this.dialogTitle = '编辑'
        this.dialogForm = row
        this.getDeptInfo()
      },

      handleDelete(userId) {
        const params = {
          "ids": [userId]
        }
        this.delelte(params)
      },

      handleDeletes: function() {
        const params = {
          "ids": this.multipleSelection
        }
        this.delelte(params)
      },

      //删除
      delelte: function(params) {
        this.$api.delUser(params).then((res) => {
          if (res.data) {
            this.$message({
              message: '删除成功',
              type: 'success'
            })
            //刷新
            this.getUserInfo()
          } else {
            this.$message.error('删除失败')
          }
        }).catch((res) => {
          this.$message.error('删除失败')
        })
      },

      //设置每页条数
      handleSizeChange(val) {
        this.form.pageSize = val
        this.getUserInfo()
      },

      //跳转第几页
      handleCurrentChange(val) {
        this.form.pageNumber = val
        this.getUserInfo()
      },

      //开启多选框
      checkbox: function() {
        this.check = !this.check
      },

      //添加科室
      addUser: function() {
        this.dialogFormVisible = true
        this.dialogTitle = '添加'
        this.getDeptInfo()
      },

      //取消多选
      toggleSelection() {
        this.$refs.multipleTable.clearSelection()
        this.checkbox()
      },

      //多选事件
      handleSelectionChange(val) {
        var selection = []
        for (let i in val) {
          selection.push(val[i].id)
        }
        this.multipleSelection = selection;
      }
    }
  }
</script>

<style>
  .el-select-dropdown .el-scrollbar .el-scrollbar__wrap{
    overflow: scroll;
  }

  .content {
    margin-top: 1rem;
  }

  .table {
    margin-top: 0.8rem;
  }

  .pagination {
    margin-top: 2rem;
  }
</style>
