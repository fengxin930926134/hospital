import request from '@/utils/request.js'

/**
 * api
 */
export default {
  /**
   * test 是否与后端正常交互
   */
  test: () => request.get('/test/index'),

  /**
   * 登录
   * @params {username, password}
   */
  login: (params) => request.post('/login/auth', params),

  /**
   * 退出登录
   */
  logout: () => sessionStorage.removeItem('token'),

  /**
   * 获取用户信息
   */
  getUserInfo: () => request.post('/user/info'),

  /**
   * 更新用户信息
   */
  updateUserInfo: (params) => request.post('/user/update', params),

  /**
   * 修改密码
   * @return {boolean}
   */
  updatePassword: (params) => request.post('/user/updatePassword', params),

  /*****************************************科室管理*****************************************/

  /**
   * 分页模糊查询科室信息
   * pageNumber, deptName, pageSize
   *
   * 无参数 查全部
   */
  getDeptAll: (params) => {
    if (params != null) {
      return request.post('/admin/dept', params)
    } else {
      return request.get('/admin/deptAll')
    }
  },

  /**
   * 删除科室
   * ids: [id, id, ...]
   */
  delDept: (params) => request.post('/admin/deptDel', params),

  /**
   * 保存更改科室 (id有无)
   * dept
   */
  saveDept: (params) => request.post('/admin/deptAddOrUpdate', params),

  /*****************************************用户管理*****************************************/

  /**
   * 分页模糊查询用户信息
   * pageNumber, username, pageSize
   */
  getUserAll: (params) => request.post('/admin/user', params),

  /**
   * 删除用户
   * ids: [id, id, ...]
   */
  delUser: (params) => request.post('/admin/userDel', params),

  /**
   * 保存更改用户 (id有无)
   * user
   */
  saveUser: (params) => request.post('/admin/userAddOrUpdate', params),

  /*****************************************医技管理*****************************************/

  /**
   * 分页模糊查询医技信息
   * pageNumber, medicalName, pageSize
   */
  getMedical: (params) => request.post('/technical/medical', params),

  /**
   * 删除医技
   * ids: [id, id, ...]
   */
  delMedical: (params) => request.post('/technical/medicalDel', params),

  /**
   * 保存更改医技 (id有无)
   * medical
   */
  saveMedical: (params) => request.post('/technical/medicalAddOrUpdate', params),

  /*****************************************药品管理*****************************************/

  /**
   * 分页模糊查询药品信息
   * pageNumber, drugName, pageSize
   *
   * 无参宿查全部
   */
  getDrugAll: (params) => {
    if (params != null) {
      return request.post('/pharmacist/drug', params)
    } else {
      return request.get('/pharmacist/drugAll')
    }
  },

  /**
   * 删除药品
   * ids: [id, id, ...]
   */
  delDrug: (params) => request.post('/pharmacist/drugDel', params),

  /**
   * 保存更改药品 (id有无)
   * drug
   */
  saveDrug: (params) => request.post('/pharmacist/drugAddOrUpdate', params),

  /*****************************************自助挂号*****************************************/

  /**
   * 挂号
   */
  appointment: () => request.get('/user/appointment'),

  /**
   * 获取挂号信息
   */
  visit: () => request.get('/user/visit'),

  /*****************************************挂号信息管理*****************************************/

  /**
   * 分页模糊查询挂号信息
   * pageNumber, 状态, pageSize
   */
  getRecordAll: (params) => request.post('/doctor/record', params),

  /**
   * 根据id获取处方信息
   */
  getPrescribe: (params) => request.post('/doctor/getPrescribe', params),

  /**
   * 根据id获取检查单信息
   */
  getChecklist: (params) => request.post('/doctor/getChecklist', params),

  /**
   * 保存更改检查单 (id有无)
   * checklist
   */
  saveChecklist: (params) => request.post('/doctor/checklistAddOrUpdate', params),

  /**
   * 保存更改处方 (id有无)
   * prescribe
   */
  savePrescribe: (params) => request.post('/doctor/prescribeAddOrUpdate', params),

  /**
   * 删除处方
   * id
   */
  delPrescribe: (params) => request.post('/doctor/prescribeDel', params),

  /**
   * 删除检查单
   * id
   */
  delChecklist: (params) => request.post('/doctor/checklistDel', params),

  /**
   * 获取全部医生
   */
  getDoctorAll: () => request.get('/doctor/doctorAll'),

  /**
   * 获取全部医技
   */
  getMedicalAll: () => request.get('/doctor/medicalAll'),

  /**
   * 保存挂号信息
   * id
   */
  saveMedicalRecord: (params) => request.post('/doctor/recordUpdate', params),

  /*****************************************药房管理*****************************************/

  /**
   * 分页模糊查询药房药品信息
   * pageNumber, drugName, pageSize
   */
  getDrugstoreAll: (params) => request.post('/pharmacist/drugstore', params),

  /**
   * 入库出库 (id有无)
   * Drugstore
   */
  saveDrugstore: (params) => request.post('/pharmacist/drugstoreAddOrUpdate', params),

  /*****************************************自助缴费*****************************************/

  /**
   * 获取账单
   */
  getBillAll: () => request.get('/user/bill'),

  /**
   * 缴费
   */
  payment: (params) => request.post('/user/payment', params),

  /*****************************************处方管理*****************************************/

  /**
   * 分页模糊查询处方信息
   * pageNumber, userName, pageSize
   */
  getPrescribeAll: (params) => request.post('/pharmacist/prescribe', params),

  /**
   * 标记为已使用
   */
  usePrescribe: (params) => request.post('/pharmacist/use', params),

  /*****************************************检查单管理*****************************************/

  /**
   * 分页模糊查询检查单信息
   * pageNumber, userName, pageSize
   */
  getChecklistAll: (params) => request.post('/technical/checklist', params),

  /**
   * 标记为已使用
   */
  useChecklist: (params) => request.post('/technical/use', params),

  /*****************************************病历管理*****************************************/
  /**
   * 分页模糊查询病历信息
   * pageNumber, userName, 病历号即就诊号, pageSize
   */
  getMedicalRecordAll: (params) => request.post('/doctor/medicalRecord', params),
}
