package com.gak.hospital.repository;

import com.gak.hospital.entity.Checklist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChecklistRepository extends JpaRepository<Checklist, Integer> {
    Checklist findChecklistById(int id);

    @Modifying
    @Transactional
    @Query("update Checklist c SET " +
            "c.status = :#{#status} " +
            "where c.id in (:#{#ids})")
    int updateStatus(List<Integer> ids, int status);

    Page<Checklist> findChecklistByUserNameLike(String userName, Pageable pageable);
}
