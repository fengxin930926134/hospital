package com.gak.hospital.service;

import com.gak.hospital.entity.Dept;
import org.springframework.data.domain.Page;
import java.util.List;

public interface DeptService {

    /**
     * 分页模糊查询
     * @param deptName name
     * @param pageNumber 页
     * @param pageSize 一页多少条
     * @return page
     */
    Page<Dept> getDeptAllByPageAndDeptName(String deptName, int pageNumber, int pageSize);

    /**
     * 添加or更新 （管理员）
     * @param dept dept
     * @return boolean
     */
    boolean addOrUpdateDept(Dept dept);

    /**
     * 根据ids删除 （管理员）
     * @param ids ids
     * @return boolean
     */
    boolean delDeptByIds(List<Integer> ids);

    /**
     * 获取所有部门
     * @return list
     */
    List<Dept> getAll();
}
