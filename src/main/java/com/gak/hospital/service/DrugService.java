package com.gak.hospital.service;

import com.gak.hospital.entity.Drug;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DrugService {

    /**
     * 添加or更新 管理员
     * @param drug drug
     * @return boolean
     */
    boolean saveOrUpdate(Drug drug);

    /**
     * 分页模糊查询
     * @param drugName drugName
     * @param pageNumber 页
     * @param pageSize 一页多少条
     * @return page
     */
    Page<Drug> getDrugAllByPageAndDrugName(String drugName, int pageNumber, int pageSize);

    /**
     * 根据ids删除 （管理员）
     * @param ids ids
     * @return boolean
     */
    boolean delDrugByIds(List<Integer> ids);

    List<Drug> getAll();
}
