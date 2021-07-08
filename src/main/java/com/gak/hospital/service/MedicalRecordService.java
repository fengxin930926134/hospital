package com.gak.hospital.service;

import com.gak.hospital.entity.MedicalRecord;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MedicalRecordService {

    /**
     * 添加or更新 管理员
     * @param medicalRecord medicalRecord
     * @return boolean
     */
    boolean saveOrUpdate(MedicalRecord medicalRecord);

    /**
     * 使用数量
     * @param num num
     * @return boolean
     */
    boolean updateHistory(int num, int recordId);

    /**
     * 根据userId获取就诊号
     */
    int getIdByUserId(int userId);

    /**
     * 根据就诊号获取挂号信息 （详情） 非病历
     */
    MedicalRecord getMedicalRecordById(int id);

    /**
     * 分页模糊查询 （详情）非病历
     * @param statusList 状态list
     * @param pageNumber pageNumber
     * @param pageSize pageSize
     * @return page
     */
    Page<MedicalRecord> getMedicalRecordAllByPageAndStatusList(List<Integer> statusList, int pageNumber, int pageSize);

    /**
     * 根据就诊号获取挂号信息 详情）病历
     */
    MedicalRecord getRecordById(int id);

    /**
     * 分页模糊查询 （详情）病历
     * @param pageNumber pageNumber
     * @param pageSize pageSize
     * @return page
     */
    Page<MedicalRecord> getRecordAllByPage(String userName, int pageNumber, int pageSize);

    List<MedicalRecord> getMedicalRecordByPatientId(int patientId);
}
