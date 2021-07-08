package com.gak.hospital.repository;

import com.gak.hospital.entity.BillInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BillInfoRepository extends JpaRepository<BillInfo, Integer> {

    @Query("SELECT new com.gak.hospital.entity.BillInfo(p, d) " +
            "FROM Prescribe p LEFT JOIN Drug d ON p.drugId = d.id " +
            "WHERE p.status = :#{#status} AND p.id in (:#{#ids})")
    List<BillInfo> findPrescribeByIdInAndStatus(List<Integer> ids, int status);

    @Query("SELECT new com.gak.hospital.entity.BillInfo(c, m) " +
            "FROM Checklist c LEFT JOIN Medical m ON c.medicalId = m.medicalId " +
            "WHERE c.status = :#{#status} AND c.id in (:#{#ids})")
    List<BillInfo> findChecklistByIdInAndStatus(List<Integer> ids, int status);
}
