package com.gak.hospital.service;

import com.gak.hospital.entity.Checklist;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChecklistService {
    /**
     * 添加or更新
     * @param checklist checklist
     * @return boolean
     */
    boolean addOrUpdate(Checklist checklist);

    /**
     * 根据id获取检查单
     * @param id id
     * @return Checklist
     */
    Checklist getChecklistById(int id);

    /**
     * 删除
     * @param id id
     * @return boolean
     */
    boolean delChecklistById(int id);

    /**
     * 更新状态
     */
    boolean updateStatusByIds(List<Integer> checklistIds, int status);

    /**
     * 分页模糊查询
     * @param userName userName
     * @param pageNumber 页
     * @param pageSize 一页多少条
     * @return page
     */
    Page<Checklist> getChecklistAllByPageAndUserName(String userName, int pageNumber, int pageSize);
}
