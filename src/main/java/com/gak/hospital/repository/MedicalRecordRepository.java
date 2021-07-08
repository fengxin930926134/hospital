package com.gak.hospital.repository;

import com.gak.hospital.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {

    MedicalRecord getFirstMedicalRecordByPatientIdAndStatus(int patientId, int status);

    Page<MedicalRecord> findMedicalRecordByStatusInAndHistoryNot(List<Integer> statusList, int history, Pageable pageable);

    MedicalRecord findMedicalRecordByIdAndHistoryNot(int id, int history);

    MedicalRecord findMedicalRecordByIdAndHistoryEquals(int id, int history);

    MedicalRecord findMedicalRecordById(int id);

    List<MedicalRecord> findMedicalRecordByStatusAndHistoryGreaterThanAndPatientId(int status, int history, int patientId);

    MedicalRecord getFirstMedicalRecordByPatientIdAndStatusAndHistoryGreaterThan(int patientId, int status, int history);

    @Modifying
    @Transactional
    @Query("update MedicalRecord m SET " +
            "m.history = m.history - :#{#num} " +
            "where m.id = :#{#id} and m.history - :#{#num} >= 1")
    int updateHistory(int num, int id);

    @Query(value = "select m " +
            "from User u LEFT JOIN MedicalRecord m ON u.id = m.patientId " +
            "where u.name like :#{#userName} AND m.history = 1")
    Page<MedicalRecord> findMedicalRecordByUserNamePageable(String userName, Pageable pageable);

    List<MedicalRecord> findMedicalRecordByPatientId(int patientId);
}
