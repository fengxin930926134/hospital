<template>
  <div class="content">
    <!-- 显示处方或者检查单弹窗 -->
    <el-dialog title="详情" :visible.sync="visibleInfo" width="30%">
       <el-form label-width="70px">
         <el-form-item label="就诊号">
           <el-input v-model="dialogInfo.id" disabled></el-input>
         </el-form-item>
         <el-form-item label="患者名">
           <el-input v-model="dialogInfo.userName" disabled></el-input>
         </el-form-item>
         <el-form-item label="就诊医生">
           <el-select v-model="dialogInfo.doctorId" disabled style="width: 100%;">
             <el-option
               v-for="item in doctorList"
               :key="item.id"
               :label="item.name"
               :value="item.id">
             </el-option>
           </el-select>
         </el-form-item>
         <el-form-item label="医技" v-if="dialogInfoType">
           <el-select v-model="dialogInfo.typeId" value-key="medicalId" disabled style="width: 100%;">
             <el-option
               v-for="item in medicalList"
               :key="item.medicalId"
               :label="item.medicalName"
               :value="item.medicalId">
             </el-option>
           </el-select>
         </el-form-item>
         <el-form-item label="药品" v-else>
           <el-select v-model="dialogInfo.typeId" value-key="id" disabled style="width: 100%;">
             <el-option
               v-for="item in drugList"
               :key="item.id"
               :label="item.name"
               :value="item.id">
             </el-option>
           </el-select>
         </el-form-item>
         <el-form-item :label="dialogInfoType?'使用次数':'药品数量'">
           <el-input v-model="dialogInfo.number" disabled></el-input>
         </el-form-item>
       </el-form>
    </el-dialog>
    <!-- 行内表单查询框 -->
    <el-form :inline="true" :model="form" class="demo-form-inline">
      <!-- 查询订单号 -->
      <el-form-item label="就诊号">
        <el-input v-model="form.visitNumber" placeholder="请输入就诊号"></el-input>
      </el-form-item>
      <!-- 查询姓名 -->
      <el-form-item label="患者姓名">
        <el-input v-model="form.userName" placeholder="请输入患者姓名" @keyup.enter.native="getRecordInfo"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="getRecordInfo">查询</el-button>
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
            <el-form-item label="备注">
              <span style="width: 100%;">{{ props.row.remark }}</span>
            </el-form-item>
            <el-form-item label="处方">
              <el-button round plain
                size="small"
                type="success"
                v-for="item in props.row.prescribeIdsArr"
                :key="item"
                @click="showDialogInfo(props.row, false, item)">
                处方{{item}}
              </el-button>
            </el-form-item>
            <el-form-item label="检查单">
              <el-button round plain
                size="small"
                type="warning"
                v-for="item in props.row.checklistIdsArr"
                :key="item"
                @click="showDialogInfo(props.row, true, item)">
                检查单{{item}}
              </el-button>
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
        prop="userName"
        sortable
        label="患者名">
      </el-table-column>
      <el-table-column
        prop="sexName"
        sortable
        label="性别">
      </el-table-column>
      <el-table-column
        prop="phone"
        sortable
        label="手机号码">
      </el-table-column>
      <el-table-column
        prop="createDateName"
        sortable
        label="挂号日期">
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
    name: 'Reg',
    data() {
      return {
        //下拉框数据
        doctorList: [],
        drugList: [],
        medicalList: [],
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
          visitNumber: null,
          statusList: [0, 1],
          pageSize: 10
        },
        //数据展示
        dialogInfo: {},
        //数据展示类型 true检查单
        dialogInfoType: true,
        //数据展示弹窗是否显示
        visibleInfo: false,
      }
    },
    created: function() {
      this.getRecordInfo()
      //获取药品
      this.getDrugInfo()
      //获取医生
      this.getDoctorInfo()
      //医技
      this.getMedicalInfo()
    },
    methods: {
      getRecordInfo: function() {
        this.loading = true
        this.$api.getMedicalRecordAll(this.form).then((res) => {
          if (res.data !== null) {
            this.tableData = res.data.data
            this.page.total = res.data.totalElements
          }
          this.loading = false
        }).catch((res) => {
          this.loading = false
          this.$message.error('获取病历信息失败')
        })
      },

      /**
       * 查看检查单或者处方信息
       */
      showDialogInfo: function(row, type, id) {
        this.dialogInfoType = type
        if (type) {
          this.$api.getChecklist({
            'id': id
          }).then((res) => {
            if(res.data) {
              this.dialogInfo.id = res.data.visitNumber
              this.dialogInfo.userName = res.data.userName
              this.dialogInfo.doctorId = res.data.doctorId
              this.dialogInfo.typeId = type? res.data.medicalId: res.data.drugId
              this.dialogInfo.number = type? res.data.medicalNumber: res.data.drugNumber
              this.visibleInfo = true
            }
          }).catch((res) => {
            this.$message.error('获取检查单信息失败')
          })
        } else {
          this.$api.getPrescribe({
            'id': id
          }).then((res) => {
            if(res.data) {
              this.dialogInfo.id = res.data.visitNumber
              this.dialogInfo.userName = res.data.userName
              this.dialogInfo.doctorId = res.data.doctorId
              this.dialogInfo.typeId = type? res.data.medicalId: res.data.drugId
              this.dialogInfo.number = type? res.data.medicalNumber: res.data.drugNumber
              this.visibleInfo = true
            }
          }).catch((res) => {
            this.$message.error('获取处方信息失败')
          })
        }
      },

      //获取药品
      async getDrugInfo() {
      　this.$api.getDrugAll().then((res) => {
          if (res.data) {
            this.drugList = res.data
          }
        }).catch((res) => {
          this.$message.error('获取医生信息失败')
        })
      },

      //获取医技
      async getMedicalInfo() {
      　this.$api.getMedicalAll().then((res) => {
          if (res.data) {
            this.medicalList = res.data
          }
        }).catch((res) => {
          this.$message.error('获取医技信息失败')
        })
      },

      //获取医生
      async getDoctorInfo() {
        this.$api.getDoctorAll().then((res) => {
          if (res.data) {
            this.doctorList = res.data
          }
        }).catch((res) => {
          this.$message.error('获取药品信息失败')
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

      //设置每页条数
      handleSizeChange(val) {
        this.form.pageSize = val
        this.getRecordInfo()
      },

      //跳转第几页
      handleCurrentChange(val) {
        this.form.pageNumber = val
        this.getRecordInfo()
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
    width: 100%;
  }

  .dialog-content {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-around;
    margin-bottom: 20px;
  }

  .tag-click:active {
    background-color: #000000;
    //透明度
    opacity: 0.3;
  }

  .tag-click:hover {
    background-color: white;
    //透明度
    opacity: 0.8;
  }
</style>
