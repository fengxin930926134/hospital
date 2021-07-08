package com.gak.hospital.service;

import com.gak.hospital.entity.Prescribe;
import org.springframework.data.domain.Page;
import java.util.List;

public interface PrescribeService {
    /**
     * 添加or更新
     * @param prescribe prescribe
     * @return boolean
     */
    boolean addOrUpdate(Prescribe prescribe);

    /**
     * 根据id获取处方
     * @param id id
     * @return Prescribe
     */
    Prescribe getPrescribeById(int id);

    /**
     * 删除
     * @param id id
     * @return boolean
     */
    boolean delPrescribeById(int id);

    /**
     * 更新处方状态
     */
    boolean updateStatusByIds(List<Integer> prescribeIds, int status);

    /**
     * 分页模糊查询
     * @param userName userName
     * @param pageNumber 页
     * @param pageSize 一页多少条
     * @return page
     */
    Page<Prescribe> getPrescribeAllByPageAndUserName(String userName, int pageNumber, int pageSize);
}
