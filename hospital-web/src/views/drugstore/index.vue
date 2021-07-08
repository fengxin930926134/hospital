<template>
  <div class="content">
    <!-- 入库 -->
    <el-dialog :before-close="resetForm" title="入库" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal="false">

      <el-form label-width="80px" :model="dialogForm">

        <el-form-item label="入库药品">
          <el-select v-model="dialogForm.drug" clearable placeholder="请选择入库药品" clearable filterable style="width: 100%;">
            <el-option
              v-for="item in drugList"
              :key="item.id"
              :label="item.name"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="入库数量">
          <el-input v-model="dialogForm.number" type="number" clearable @keyup.enter.native="submit" placeholder="请输入入库数量"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">保 存</el-button>
        <el-button @click="resetForm">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 编辑 -->
    <el-dialog :before-close="resetForm" title="更新数量" :visible.sync="dialogEditVisible" width="30%" :close-on-click-modal="false">

      <el-form label-width="80px" :model="dialogForm">
        <el-form-item label="入库数量">
          <el-input v-model="dialogForm.number" type="number" clearable @keyup.enter.native="submit" placeholder="请输入入库数量"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">保 存</el-button>
        <el-button @click="resetForm">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 行内表单查询框 -->
    <el-form :inline="true" :model="form" class="demo-form-inline">
      <el-button type="primary" @click="dialogFormVisible = true">入库</el-button>
      <el-divider direction="vertical"></el-divider>
      <el-form-item label="药品名称">
        <el-input v-model="form.drugName" placeholder="药品名称" @keyup.enter.native="getDrugstoreInfo"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="getDrugstoreInfo">查询</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      stripe
      class="table"
      border
      :header-cell-style="centerClass"
      :cell-style="centerClass"
      style="width: 100%;">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="批号">
              <span>{{ props.row.drug.batchNumber }}</span>
            </el-form-item>
            <el-form-item label="单价">
              <span>{{ props.row.drug.price }} ￥</span>
            </el-form-item>
            <el-form-item label="类别">
              <span>{{ props.row.drug.category }}</span>
            </el-form-item>
            <el-form-item label="生产日期">
              <span>{{ props.row.drug.manufactureDateName }}</span>
            </el-form-item>
            <el-form-item label="保质期">
              <span>{{ props.row.drug.dueMonth }} 个月</span>
            </el-form-item>
          </el-form>
        </template>
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
        prop="drugName"
        sortable
        label="药品名">
      </el-table-column>
      <el-table-column
        prop="number"
        sortable
        label="数量">
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
    name: 'Drugstore',
    data() {
      return {
        drugList: [],
        dialogFormVisible: false,
        dialogEditVisible: false,
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
          drugName: '',
          pageSize: 10
        },
        //弹窗表单
        dialogForm: {},
        //弹窗标题
        dialogTitle: '',
      }
    },
    created: function() {
      this.getDrugstoreInfo()
      //获取全部药品
      this.$api.getDrugAll().then((res) => {
        this.$data.drugList = res.data
      }).catch((res) => {
        console.log(res)
        this.$message.error('加载药品信息失败');
      })
    },
    methods: {
      getDrugstoreInfo: function() {
        this.loading = true
        this.$api.getDrugstoreAll(this.form).then((res) => {
          if (res.data !== null) {
            this.tableData = res.data.data
            this.page.total = res.data.totalElements
          }
          this.loading = false
        }).catch((res) => {
          this.loading = false
          this.$message.error('获取药品库存信息失败')
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
         this.dialogEditVisible = false
         this.dialogForm = {}
      },

      //提交弹窗表单
      submit: function() {
        this.$api.saveDrugstore(this.dialogForm).then((res) => {
          if (res.data) {
            this.$message({
              message: '保存成功',
              type: 'success'
            })
            this.getDrugstoreInfo()
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
        this.dialogEditVisible = true
        this.dialogForm = row
        this.dialogForm.drug = null
      },

      //设置每页条数
      handleSizeChange(val) {
        this.form.pageSize = val
        this.getDrugstoreInfo()
      },

      //跳转第几页
      handleCurrentChange(val) {
        this.form.pageNumber = val
        this.getDrugstoreInfo()
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

  /* 扩展数据样式 */
  .demo-table-expand {
      font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
