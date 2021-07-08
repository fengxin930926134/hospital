package com.gak.hospital.service;

import com.gak.hospital.entity.Drugstore;
import org.springframework.data.domain.Page;

public interface DrugstoreService {
    /**
     * 分页模糊查询
     * @param drugName name
     * @param pageNumber 页
     * @param pageSize 一页多少条
     * @return page
     */
    Page<Drugstore> getDrugstoreAllByPageAndDrugName(String drugName, int pageNumber, int pageSize);

    /**
     * 添加or更新 管理员
     * @param drugstore drugstore
     * @return boolean
     */
    boolean saveOrUpdate(Drugstore drugstore);
}
