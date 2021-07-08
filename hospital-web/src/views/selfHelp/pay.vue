<template>
  <el-card class="box-card">
    <div v-if="display">
      <el-table
        :data="tableData"
        show-summary
        :summary-method="getSummaries"
        style="width: 100%">
        <el-table-column
          prop="visitNumber"
          label="就诊号">
        </el-table-column>
        <el-table-column
          prop="userName"
          label="患者">
        </el-table-column>
        <el-table-column
          prop="projectName"
          sortable
          label="扣费项目">
        </el-table-column>
        <el-table-column
          sortable
          label="单价">
          <template slot-scope="scope">
            {{scope.row.price}} ￥
          </template>
        </el-table-column>
        <el-table-column
          prop="number"
          sortable
          label="数量">
        </el-table-column>
        <el-table-column
          sortable
          label="实际价格">
          <template slot-scope="scope">
            {{scope.row.price * scope.row.number}} ￥
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 20px; display: flex; flex-direction: row-reverse;">
        <el-popconfirm
          confirmButtonText='确认'
          cancelButtonText='取消'
          icon="el-icon-thumb"
          iconColor="red"
          @onConfirm="pay"
          :title="'确认缴纳' + price + '元吗？'"
        >
          <el-button slot="reference">缴费</el-button>
        </el-popconfirm>
      </div>
    </div>
    <div v-else style="text-align: center;">没有未缴费的信息</div>
  </el-card>
</template>

<script>
  export default {
    name: 'Pay',
    data() {
      return {
        display: false,
        tableData: [],
        form: {},
        price: 0,
      }
    },
    created: function() {
      this.getBill()
    },
    methods: {
      getVisitNumber: function() {
        this.$api.visit().then((res) => {
          this.visitNumber = res.data
        }).catch((res) => {
          this.$message.error('访问失败')
        })
      },
      
      getBill() {
        this.$api.getBillAll().then((res) => {
          if (res.data != null && res.data.billInfos != null) {
            this.tableData = res.data.billInfos
            this.form.checklistIds = res.data.checklistIds
            this.form.prescribeIds = res.data.prescribeIds
            this.form.visitNumber = res.data.visitNumber
            this.price = res.data.price
            this.display = true
          } else {
            this.display = false
          }
        }).catch((res) => {
          this.$message.error('获取账单失败')
        })
      },
      getSummaries(param) {
        const { columns, data } = param
        const sums = []
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '总价'
            return
          }
          if (index === 5) {
            sums[index] = this.price + ' ￥'
          }
        })
        return sums
      },

      pay: function() {
        this.$api.payment(this.form).then((res) => {
          if (res.data) {
            this.$message({
              message: '缴费成功',
              type: 'success'
            })
            this.getBill()
          } else {
            this.$message.error('缴费失败')
          }
        }).catch((res) => {
          this.$message.error('缴费失败')
        })
      }
    }
  }
</script>
<style>
</style>
