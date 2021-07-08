package com.gak.hospital.repository;

import com.gak.hospital.entity.Medical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MedicalRepository extends JpaRepository<Medical, Integer> {

    Page<Medical> findMedicalByMedicalNameLike(String medicalName, Pageable pageable);

    @Transactional
    void deleteMedicalByMedicalIdIn(List<Integer> ids);

    Medical findMedicalByMedicalId(int id);
}
