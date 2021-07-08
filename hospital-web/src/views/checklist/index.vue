<template>
  <div class="content">
    <!-- 行内表单查询框 -->
    <el-form :inline="true" :model="form" class="demo-form-inline">
      <el-form-item label="患者名称">
        <el-input v-model="form.userName" placeholder="患者名称" @keyup.enter.native="getChecklistInfo"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="getChecklistInfo">查询</el-button>
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
        prop="visitNumber"
        sortable
        label="就诊号">
      </el-table-column>
      <el-table-column
        prop="userName"
        sortable
        label="患者名">
      </el-table-column>
      <el-table-column
        prop="doctorName"
        sortable
        label="开单医生">
      </el-table-column>
      <el-table-column
        prop="medicalName"
        sortable
        label="医技">
      </el-table-column>
      <el-table-column
        prop="medicalNumber"
        sortable
        label="使用次数">
      </el-table-column>
      <el-table-column
        prop="status"
        sortable
        label="状态">
        <template slot-scope="scope">
          <el-tag
          v-if="scope.row.status == 0"
          size="medium"
          type="danger">
            未缴费
          </el-tag>
          <el-tag
          v-if="scope.row.status == 2"
          size="medium"
          type="info">
            已使用
          </el-tag>
          <el-tag
          v-if="scope.row.status == 1"
          size="medium"
          type="success">
            待使用
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        align="center"
        width="150">
        <template slot-scope="scope">
          <el-popconfirm
            v-if="scope.row.status == 1"
            confirmButtonText='确定'
            cancelButtonText='取消'
            icon="el-icon-info"
            iconColor="red"
            title="确定标记为已使用吗？"
            @onConfirm="handleUse(scope.row.id, scope.row.visitNumber)">
            <el-button
              slot="reference"
              size="mini"
              type="primary">标记使用</el-button>
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
    name: 'Checklist',
    data() {
      return {
        dialogFormVisible: false,
        loading: true,
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
          userName: '',
          pageSize: 10
        },
      }
    },
    created: function() {
      this.getChecklistInfo()
    },
    methods: {
      getChecklistInfo: function() {
        this.loading = true
        this.$api.getChecklistAll(this.form).then((res) => {
          if (res.data !== null) {
            this.tableData = res.data.data
            this.page.total = res.data.totalElements
          }
          this.loading = false
        }).catch((res) => {
          this.loading = false
          this.$message.error('获取检查单信息失败')
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

      handleUse(recordId, visitNumber) {
        const params = {
          "checklistIds": [recordId],
          "visitNumber": visitNumber
        }
        this.use(params)
      },

      //使用
      use: function(params) {
        this.$api.useChecklist(params).then((res) => {
          if (res.data) {
            this.$message({
              message: '修改成功',
              type: 'success'
            })
            this.getChecklistInfo()
          } else {
            this.$message.error('修改失败')
          }
        }).catch((res) => {
          this.$message.error('修改失败')
        })
      },

      //设置每页条数
      handleSizeChange(val) {
        this.form.pageSize = val
        this.getChecklistInfo()
      },

      //跳转第几页
      handleCurrentChange(val) {
        this.form.pageNumber = val
        this.getChecklistInfo()
      },
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
