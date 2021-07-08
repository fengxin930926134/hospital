<template>
  <div class="content">
    <el-dialog :before-close="resetForm" :title="dialogTitle" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal="false">

      <el-form label-width="80px" :model="dialogForm">
        <el-form-item label="科室名称">
          <el-input v-model="dialogForm.deptName" clearable @keyup.enter.native="submit" placeholder="请输入科室名称"></el-input>
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
      <el-button type="primary" @click="addDept">添加科室</el-button>
      <el-divider direction="vertical"></el-divider>
      <el-form-item label="科室名称">
        <el-input v-model="form.deptName" placeholder="科室名称" @keyup.enter.native="getDeptInfo"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="getDeptInfo">查询</el-button>
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
        prop="deptId"
        sortable
        label="编号">
      </el-table-column>
      <el-table-column
        prop="deptName"
        sortable
        label="科室名">
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
            @onConfirm="handleDelete(scope.row.deptId)">
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
  export default {
    name: 'Dept',
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
          deptName: '',
          pageSize: 10
        },
        //弹窗表单
        dialogForm: {},
        //弹窗标题
        dialogTitle: '',
      }
    },
    created: function() {
      this.getDeptInfo()
    },
    methods: {
      getDeptInfo: function() {
        this.loading = true
        this.$api.getDeptAll(this.form).then((res) => {
          if (res.data !== null) {
            this.tableData = res.data.data
            this.page.total = res.data.totalElements
          }
          this.loading = false
        }).catch((res) => {
          this.loading = false
          this.$message.error('获取部门信息失败')
        })
      },

      // 序号
      indexMethod(index) {
        return index + 1;
      },

      // 居中样式
      centerClass () {
        return 'text-align: center;'
      },

      //取消弹窗
      resetForm: function() {
         this.dialogFormVisible = false
         this.dialogForm = {}
      },

      //提交弹窗表单
      submit: function() {
        this.$api.saveDept(this.dialogForm).then((res) => {
          if (res.data) {
            this.$message({
              message: '保存成功',
              type: 'success'
            })
            this.getDeptInfo()
            this.resetForm()
          } else {
            this.$message.error('保存失败')
          }
        }).catch((res) => {
          this.$message.error('保存失败')
        })
      },

      //编辑
      handleEdit(row) {
        this.dialogFormVisible = true
        this.dialogTitle = '编辑'
        this.dialogForm = row
      },

      handleDelete(deptId) {
        const params = {
          "ids": [deptId]
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
        this.$api.delDept(params).then((res) => {
          if (res.data) {
            this.$message({
              message: '删除成功',
              type: 'success'
            })
            this.getDeptInfo()
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
        this.getDeptInfo()
      },

      //跳转第几页
      handleCurrentChange(val) {
        this.form.pageNumber = val
        this.getDeptInfo()
      },

      //开启多选框
      checkbox: function() {
        this.check = !this.check
      },

      //添加科室
      addDept: function() {
        this.dialogFormVisible = true
        this.dialogTitle = '添加'
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
          selection.push(val[i].deptId)
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
