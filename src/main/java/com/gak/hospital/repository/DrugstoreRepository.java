package com.gak.hospital.repository;

import com.gak.hospital.entity.Drugstore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugstoreRepository extends JpaRepository<Drugstore, Integer> {
    Page<Drugstore> findDrugstoreByDrugNameLikeAndNumberNot(String drugName, int number, Pageable pageable);

    Drugstore findDrugstoreByDrugId(int drugId);
}
