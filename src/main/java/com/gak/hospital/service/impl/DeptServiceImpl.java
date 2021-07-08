package com.gak.hospital.service.impl;

import com.gak.hospital.entity.Dept;
import com.gak.hospital.repository.DeptRepository;
import com.gak.hospital.service.DeptService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptServiceImpl implements DeptService {

    private final @NonNull DeptRepository deptRepository;

    @Override
    public Page<Dept> getDeptAllByPageAndDeptName(String deptName, int pageNumber, int pageSize) {
        if (deptName == null || deptName.equals("")) {
            deptName = "%%";
        } else  {
            deptName = "%" + deptName + "%";
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return deptRepository.findDeptByDeptNameLike(deptName, pageable);
    }

    @Override
    public boolean addOrUpdateDept(Dept dept) {
        try {
            deptRepository.save(dept);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delDeptByIds(List<Integer> ids) {
        try {
            deptRepository.deleteDeptByDeptIdIn(ids);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Dept> getAll() {
        return deptRepository.findAll();
    }
}
