package com.gak.hospital.repository;

import com.gak.hospital.entity.Prescribe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PrescribeRepository extends JpaRepository<Prescribe, Integer> {
    Prescribe findPrescribeById(int id);

    @Modifying
    @Transactional
    @Query("update Prescribe p SET " +
            "p.status = :#{#status} " +
            "where p.id in (:#{#ids})")
    int updateStatus(List<Integer> ids, int status);

    Page<Prescribe> findPrescribeByUserNameLike(String userName, Pageable pageable);
}
