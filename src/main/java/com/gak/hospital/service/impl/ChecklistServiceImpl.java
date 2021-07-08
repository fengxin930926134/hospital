package com.gak.hospital.service.impl;

import com.gak.hospital.entity.Checklist;
import com.gak.hospital.entity.Medical;
import com.gak.hospital.entity.MedicalRecord;
import com.gak.hospital.entity.User;
import com.gak.hospital.repository.ChecklistRepository;
import com.gak.hospital.repository.MedicalRecordRepository;
import com.gak.hospital.repository.MedicalRepository;
import com.gak.hospital.repository.UserRepository;
import com.gak.hospital.service.ChecklistService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService {

    private final @NonNull ChecklistRepository checklistRepository;
    private final @NonNull MedicalRecordRepository medicalRecordRepository;
    private final @NonNull UserRepository userRepository;
    private final @NonNull MedicalRepository medicalRepository;

    @Override
    public boolean addOrUpdate(Checklist checklist) {
        try {
            final boolean isAdd = checklist.getId() == 0;
            if (isAdd) {
                checklist.setCreateDate(new Date());
            }
            Checklist save = checklistRepository.save(checklist);
            //添加
            if (isAdd) {
                MedicalRecord medicalRecord = medicalRecordRepository.findMedicalRecordById(checklist.getVisitNumber());
                final String head = medicalRecord.getChecklistIds()==null?"":medicalRecord.getChecklistIds();
                medicalRecord.setChecklistIds(head + save.getId() + ",");
                medicalRecordRepository.save(medicalRecord);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Checklist getChecklistById(int id) {
        return checklistRepository.findChecklistById(id);
    }

    @Override
    public boolean delChecklistById(int id) {
        try {
            Checklist checklistById = checklistRepository.findChecklistById(id);
            MedicalRecord medicalRecord = medicalRecordRepository.findMedicalRecordById(checklistById.getVisitNumber());
            StringBuilder sb = new StringBuilder();
            for (String item : medicalRecord.getChecklistIds().split(",")) {
                if (!item.equals(String.valueOf(id))) {
                    sb.append(item).append(",");
                }
            }
            medicalRecord.setChecklistIds(sb.toString());
            medicalRecordRepository.save(medicalRecord);
            checklistRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateStatusByIds(List<Integer> checklistIds, int status) {
        try {
            //1.待使用
            return checklistRepository.updateStatus(checklistIds, status) > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Page<Checklist> getChecklistAllByPageAndUserName(String userName, int pageNumber, int pageSize) {
        if (userName == null || userName.equals("")) {
            userName = "%%";
        } else  {
            userName = "%" + userName + "%";
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.ASC, "status"));
        return checklistRepository.findChecklistByUserNameLike(userName, pageable).map(checklist -> {
            User user = userRepository.findUserById(checklist.getDoctorId());
            Medical medical = medicalRepository.findMedicalByMedicalId(checklist.getMedicalId());
            checklist.setDoctorName(user.getName());
            checklist.setMedicalName(medical.getMedicalName());
            return checklist;
        });
    }
}
