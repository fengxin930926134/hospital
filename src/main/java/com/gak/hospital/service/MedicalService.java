package com.gak.hospital.service;

import com.gak.hospital.entity.Medical;
import org.springframework.data.domain.Page;
import java.util.List;

public interface MedicalService {

    /**
     * 添加or更新 管理员
     * @param medical medical
     * @return boolean
     */
    boolean saveOrUpdate(Medical medical);

    /**
     * 分页模糊查询
     * @param medicalName medicalName
     * @param pageNumber 页
     * @param pageSize 一页多少条
     * @return page
     */
    Page<Medical> getMedicalAllByPageAndMedicalName(String medicalName, int pageNumber, int pageSize);

    /**
     * 根据ids删除 （管理员）
     * @param ids ids
     * @return boolean
     */
    boolean delMedicalByIds(List<Integer> ids);

    List<Medical> getAll();
}
