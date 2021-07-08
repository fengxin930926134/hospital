package com.gak.hospital.repository;

import com.gak.hospital.entity.Drug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DrugRepository extends JpaRepository<Drug, Integer> {

    Page<Drug> findDrugByNameLike(String name, Pageable pageable);

    @Transactional
    void deleteDrugByIdIn(List<Integer> ids);

    Drug findDrugById(int id);
}
