package com.gak.hospital.repository;

import com.gak.hospital.entity.Dept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface DeptRepository extends JpaRepository<Dept, Integer> {

    Page<Dept> findDeptByDeptNameLike(String deptName, Pageable pageable);

    @Transactional
    void deleteDeptByDeptIdIn(List<Integer> ids);

    Dept findDeptByDeptId(int deptId);
}
