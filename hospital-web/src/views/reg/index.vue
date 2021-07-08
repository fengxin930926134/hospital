<template>
  <div class="content">
    <!-- 编辑 -->
    <el-dialog :before-close="resetForm" title="诊断" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal="false">
      <!-- 处方内弹窗 -->
      <el-dialog
        width="30%"
        title="处方"
        :visible.sync="prescribeVisible"
        append-to-body>
        <el-form label-width="70px" :model="prescribeForm">
          <el-form-item label="就诊号">
            <el-input v-model="prescribeForm.visitNumber" disabled></el-input>
          </el-form-item>
          <el-form-item label="患者名">
            <el-input v-model="prescribeForm.userName" disabled></el-input>
          </el-form-item>
          <el-form-item label="就诊医生">
            <el-select v-model="prescribeForm.doctorId" clearable placeholder="请选择就诊医生" filterable style="width: 100%;">
              <el-option
                v-for="item in doctorList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="药品">
            <el-select v-model="prescribeForm.drugId" clearable placeholder="请选择药品" filterable style="width: 100%;">
              <el-option
                v-for="item in drugList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="药品数量">
            <el-input v-model="prescribeForm.drugNumber" type="number" clearable @keyup.enter.native="submitPrescribe" placeholder="请输入药品数量"></el-input>
          </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitPrescribe">保 存</el-button>
          <el-button @click="prescribeVisible = false">取 消</el-button>
        </div>
      </el-dialog>
      <!-- 检查单内弹窗 -->
      <el-dialog
        width="30%"
        title="检查单"
        :visible.sync="checklistVisible"
        append-to-body>
        <el-form label-width="70px" :model="checklistForm">
          <el-form-item label="就诊号">
            <el-input v-model="checklistForm.visitNumber" disabled></el-input>
          </el-form-item>
          <el-form-item label="患者名">
            <el-input v-model="checklistForm.userName" disabled></el-input>
          </el-form-item>
          <el-form-item label="就诊医生">
            <el-select v-model="checklistForm.doctorId" clearable placeholder="请选择就诊医生" filterable style="width: 100%;">
              <el-option
                v-for="item in doctorList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="医技">
            <el-select v-model="checklistForm.medicalId" clearable placeholder="请选择医技" filterable style="width: 100%;">
              <el-option
                v-for="item in medicalList"
                :key="item.medicalId"
                :label="item.medicalName"
                :value="item.medicalId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="使用次数">
            <el-input v-model="checklistForm.medicalNumber" type="number" clearable @keyup.enter.native="submitChecklist" placeholder="请输入使用次数"></el-input>
          </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitChecklist">保 存</el-button>
          <el-button @click="checklistVisible = false">取 消</el-button>
        </div>
      </el-dialog>
      <!-- 内容 -->
      <el-form label-width="70px" :model="dialogForm">
        <div class="dialog-content">
          <span style="width: 4.375rem; margin-left: 0.4rem;">就诊号</span><el-input v-model="dialogForm.id" disabled style="width: auto;"></el-input>
          <span style="width: 70px; margin-left: 1.25rem;">患者名</span><el-input v-model="dialogForm.userName" disabled style="width:auto;"></el-input>
        </div>
        <el-form-item label="医疗备注">
          <el-input type="textarea" autosize v-model="dialogForm.remark" style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="处方">
          <el-tag
            class="tag-click"
            type="success"
            @click="openPrescribeEdit(item)"
            v-for="item in dialogForm.prescribeIdsArr"
            :key="item"
            @close="closePrescribe(item)"
            style="margin-right: 0.625rem;"
            closable>
            处方{{item}}
          </el-tag>
          <el-button @click="openPrescribeEdit()" size="small" icon="el-icon-plus" circle></el-button>
        </el-form-item>
        <el-form-item label="检查单">
          <el-tag
            class="tag-click"
            type="warning"
            @click="openChecklistEdit(item)"
            v-for="item in dialogForm.checklistIdsArr"
            :key="item"
            @close="closeChecklist(item)"
            style="margin-right: 0.625rem;"
            closable>
            检查单{{item}}
          </el-tag>
          <el-button @click="openChecklistEdit()" size="small" icon="el-icon-plus" circle></el-button>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">保 存</el-button>
        <el-button @click="resetForm">取 消</el-button>
      </div>
    </el-dialog>
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
        <el-input v-model="form.visitNumber" placeholder="请输入就诊号" @keyup.enter.native="getRecordInfo"></el-input>
      </el-form-item>
      <!-- 查询状态 -->
      <el-form-item label="状态">
        <el-select v-model="form.statusList" multiple placeholder="请选择状态" @keyup.enter.native="getRecordInfo">
          <el-option
            v-for="item in statusList"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
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
      <el-table-column
        fixed="right"
        sortable
        label="状态">
        <template slot-scope="scope">
          <el-tag size="medium" v-if="scope.row.status == 0">未处理</el-tag>
          <el-tag size="medium" v-else type="success">已处理</el-tag>
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
            @click="handleEdit(scope.row)">诊断</el-button>
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
    name: 'Reg',
    data() {
      return {
        //状态
        statusList: [
          {
            label: '未处理',
            value: 0,
          },
          {
            label: '已处理',
            value: 1,
          },
        ],
        //下拉框数据
        doctorList: [],
        drugList: [],
        medicalList: [],
        //内弹窗
        prescribeVisible: false,
        checklistVisible: false,
        //弹窗
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
          visitNumber: null,
          statusList: [0, 1],
          pageSize: 10
        },
        //弹窗表单
        dialogForm: {},
        //处方表单
        prescribeForm: {},
        //检查单
        checklistForm: {},
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
        this.$api.getRecordAll(this.form).then((res) => {
          if (res.data !== null) {
            this.tableData = res.data.data
            this.page.total = res.data.totalElements
          }
          this.loading = false
        }).catch((res) => {
          this.loading = false
          this.$message.error('获取挂号信息失败')
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

      //取消弹窗
      resetForm: function() {
         this.dialogFormVisible = false
         this.dialogForm = {}
      },

      //提交弹窗表单
      submit: function() {
        // 验证
        if ((this.dialogForm.prescribeIds == '' && this.dialogForm.checklistIds == '') ||
        (this.dialogForm.prescribeIds == null && this.dialogForm.checklistIds == null)) {
          this.$message({
            message: '还未开处方或检查单',
            type: 'warning'
          })
          return
        }
        this.$api.saveMedicalRecord(this.dialogForm)
        .then((res) => {
          if (res.data) {
            this.$message({
              message: '保存成功',
              type: 'success'
            })
            this.dialogFormVisible = false
            this.getRecordInfo()
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
        this.dialogForm = row
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
      /*******************处方*****************/
      // 打开处方弹窗进行编辑
      openPrescribeEdit: function(id) {
        if (id != null) {
          this.getPrescribeInfo(id)
        } else {
          this.prescribeForm = {}
          this.prescribeForm.visitNumber = this.dialogForm.id
          this.prescribeForm.userName = this.dialogForm.userName
          this.prescribeVisible = true
        }
      },
      // 根据id获取处方信息
      getPrescribeInfo: function(id) {
        this.$api.getPrescribe({
          'id': id
        }).then((res) => {
          if (res.data) {
            this.prescribeForm = res.data
            this.prescribeVisible = true
          } else {
            this.$message.error('获取处方信息失败')
          }
        }).catch((res) => {
          this.$message.error('获取处方信息失败')
        })
      },
      //保存处方
      submitPrescribe: function() {
        if (this.prescribeForm.doctorId == 0 || this.prescribeForm.doctorId == null) {
          this.$message.error('医生不能为空')
          return
        }
        if (this.prescribeForm.drugId == 0 || this.prescribeForm.drugId == null) {
          this.$message.error('药品不能为空')
          return
        }
        if (this.prescribeForm.drugNumber == 0 || this.prescribeForm.drugNumber == null) {
          this.$message.error('药品数量不能为空')
          return
        }
        this.$api.savePrescribe(this.prescribeForm).then((res) => {
          if (res.data) {
            this.$message({
              message: '保存成功',
              type: 'success'
            })
            this.dialogForm.prescribeIdsArr = res.data.prescribeIdsArr
            this.dialogForm.prescribeIds = res.data.prescribeIds
            this.prescribeVisible = false
          } else {
            this.$message.error('保存失败')
          }
        }).catch((res) => {
          this.$message.error('保存失败')
        })
      },
      //删除处方
      closePrescribe: function(item) {
        this.$api.delPrescribe({
          'id': item
        }).then((res) => {
          if (res.data) {
            this.$message({
              message: '删除成功',
              type: 'success'
            })
            this.dialogForm.prescribeIdsArr.splice(this.dialogForm.prescribeIdsArr.indexOf(item), 1);
          } else {
            this.$message.error('删除失败')
          }
        }).catch((res) => {
          this.$message.error('删除失败')
        })
      },
      /*******************检查单*****************/
      // 打开检查单弹窗进行编辑
      openChecklistEdit: function(id) {
        if (id != null) {
          this.getChecklistInfo(id)
        } else {
          this.checklistForm = {}
          this.checklistForm.visitNumber = this.dialogForm.id
          this.checklistForm.userName = this.dialogForm.userName
          this.checklistVisible = true
        }
      },
      // 根据id获取检查单信息
      getChecklistInfo: function(id) {
        this.$api.getChecklist({
          'id': id
        }).then((res) => {
          if (res.data) {
            this.checklistForm = res.data
            this.checklistVisible = true
          } else {
            this.$message.error('获取检查单信息失败')
          }
        }).catch((res) => {
          this.$message.error('获取检查单信息失败')
        })
      },
      //保存检查单
      submitChecklist: function() {
        if (this.checklistForm.doctorId == 0 || this.checklistForm.doctorId == null) {
          this.$message.error('医生不能为空')
          return
        }
        if (this.checklistForm.medicalId == 0 || this.checklistForm.medicalId == null) {
          this.$message.error('医技不能为空')
          return
        }
        if (this.checklistForm.medicalNumber == 0 || this.checklistForm.medicalNumber == null) {
          this.$message.error('医技次数不能为空')
          return
        }
        this.$api.saveChecklist(this.checklistForm).then((res) => {
          if (res.data) {
            this.$message({
              message: '保存成功',
              type: 'success'
            })
            this.dialogForm.checklistIdsArr = res.data.checklistIdsArr
            this.dialogForm.checklistIds = res.data.checklistIds
            this.checklistVisible = false
          } else {
            this.$message.error('保存失败')
          }
        }).catch((res) => {
          this.$message.error('保存失败')
        })
      },
      //删除检查单
      closeChecklist: function(item) {
        this.$api.delChecklist({
          'id': item
        }).then((res) => {
          if (res.data) {
            this.$message({
              message: '删除成功',
              type: 'success'
            })
            this.dialogForm.checklistIdsArr.splice(this.dialogForm.checklistIdsArr.indexOf(item), 1);
          } else {
            this.$message.error('删除失败')
          }
        }).catch((res) => {
          this.$message.error('删除失败')
        })
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
