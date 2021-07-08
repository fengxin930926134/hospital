package com.gak.hospital.service.impl;

import com.gak.hospital.entity.MedicalRecord;
import com.gak.hospital.entity.User;
import com.gak.hospital.repository.MedicalRecordRepository;
import com.gak.hospital.repository.UserRepository;
import com.gak.hospital.service.MedicalRecordService;
import com.gak.hospital.utils.DateUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final @NonNull MedicalRecordRepository medicalRecordRepository;
    private final @NonNull UserRepository userRepository;

    @Override
    public boolean saveOrUpdate(MedicalRecord medicalRecord) {
        try {
            if (medicalRecord.getId() == 0) {
                //检查是否存在未缴费单子
                List<MedicalRecord> medicalRecordList = medicalRecordRepository.findMedicalRecordByStatusAndHistoryGreaterThanAndPatientId(1, 1, medicalRecord.getPatientId());
                if (medicalRecordList.size() != 0) {
                    return true;
                }
                medicalRecord.setCreateDate(new Date());
            } else {
                //设为已处理
                medicalRecord.setStatus(1);
                //是否病历 增加基础值
                int a = medicalRecord.getChecklistIdsArr() != null?medicalRecord.getChecklistIdsArr().length: 0;
                int b = medicalRecord.getPrescribeIdsArr() != null?medicalRecord.getPrescribeIdsArr().length: 0;
                medicalRecord.setHistory(a + b + 1);
            }
            medicalRecordRepository.save(medicalRecord);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateHistory(int num, int recordId) {
        return medicalRecordRepository.updateHistory(num, recordId) > 0;
    }

    @Override
    public int getIdByUserId(int userId) {
        //检查是否存在未缴费单子
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.findMedicalRecordByStatusAndHistoryGreaterThanAndPatientId(1, 1, userId);
        if (medicalRecordList.size() == 0) {
            //获取未处理
            MedicalRecord medicalRecord = medicalRecordRepository.getFirstMedicalRecordByPatientIdAndStatus(userId, 0);
            if (medicalRecord != null) {
                return medicalRecord.getId();
            }
        }
        return 0;
    }

    @Override
    public MedicalRecord getMedicalRecordById(int id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findMedicalRecordByIdAndHistoryNot(id, 1);
        if (medicalRecord != null) {
            return getUserInfo(medicalRecord);
        }
        return null;
    }

    @Override
    public Page<MedicalRecord> getMedicalRecordAllByPageAndStatusList(List<Integer> statusList, int pageNumber, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "status");
        return medicalRecordRepository.findMedicalRecordByStatusInAndHistoryNot(statusList, 1, PageRequest.of(pageNumber - 1, pageSize, sort)).map(this::getUserInfo);
    }

    @Override
    public MedicalRecord getRecordById(int id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findMedicalRecordByIdAndHistoryEquals(id, 1);
        if (medicalRecord != null) {
            return getUserInfo(medicalRecord);
        }
        return null;
    }

    @Override
    public Page<MedicalRecord> getRecordAllByPage(String userName, int pageNumber, int pageSize) {
        if (userName == null || userName.equals("")) {
            userName = "%%";
        } else  {
            userName = "%" + userName + "%";
        }
        return medicalRecordRepository.findMedicalRecordByUserNamePageable(userName,
                PageRequest.of(pageNumber - 1, pageSize))
                .map(this::getUserInfo);
    }

    @Override
    public List<MedicalRecord> getMedicalRecordByPatientId(int patientId) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findMedicalRecordByPatientId(patientId);
        for (MedicalRecord record: medicalRecords) {
            record.setCreateDateName(DateUtils.formatYMDHMS(record.getCreateDate()));
            if (record.getChecklistIds() != null && !record.getChecklistIds().equals("")) {
                record.setChecklistIdsArr(record.getChecklistIds().split(","));
            }
            if (record.getPrescribeIds() != null && !record.getPrescribeIds().equals("")) {
                record.setPrescribeIdsArr(record.getPrescribeIds().split(","));
            }
        }
        return medicalRecords;
    }

    private MedicalRecord getUserInfo(MedicalRecord medicalRecord) {
        User userById = userRepository.findUserById(medicalRecord.getPatientId());
        medicalRecord.setUserName(userById.getName());
        medicalRecord.setPhone(userById.getPhone());
        medicalRecord.setSexName(userById.getSex() == 1? "男": "女");
        medicalRecord.setCreateDateName(DateUtils.formatYMDHMS(medicalRecord.getCreateDate()));
        if (medicalRecord.getPrescribeIds() != null && !medicalRecord.getPrescribeIds().equals("")) {
            medicalRecord.setPrescribeIdsArr(medicalRecord.getPrescribeIds().split(","));
        }
        if (medicalRecord.getChecklistIds() != null && !medicalRecord.getChecklistIds().equals("")) {
            medicalRecord.setChecklistIdsArr(medicalRecord.getChecklistIds().split(","));
        }
        return medicalRecord;
    }

}
