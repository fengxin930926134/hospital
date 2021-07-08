package com.gak.hospital.service.impl;

import com.gak.hospital.entity.Drug;
import com.gak.hospital.entity.MedicalRecord;
import com.gak.hospital.entity.Prescribe;
import com.gak.hospital.entity.User;
import com.gak.hospital.repository.DrugRepository;
import com.gak.hospital.repository.MedicalRecordRepository;
import com.gak.hospital.repository.PrescribeRepository;
import com.gak.hospital.repository.UserRepository;
import com.gak.hospital.service.PrescribeService;
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
public class PrescribeServiceImpl implements PrescribeService {

    private final @NonNull PrescribeRepository prescribeRepository;
    private final @NonNull MedicalRecordRepository medicalRecordRepository;
    private final @NonNull UserRepository userRepository;
    private final @NonNull DrugRepository drugRepository;

    @Override
    public boolean addOrUpdate(Prescribe prescribe) {
        try {
            final boolean isAdd = prescribe.getId() == 0;
            if (isAdd) {
                prescribe.setCreateDate(new Date());
            }
            Prescribe save = prescribeRepository.save(prescribe);
            if (isAdd) {
                MedicalRecord medicalRecord = medicalRecordRepository.findMedicalRecordById(save.getVisitNumber());
                final String head = medicalRecord.getPrescribeIds()==null?"":medicalRecord.getPrescribeIds();
                medicalRecord.setPrescribeIds(head + save.getId() + ",");
                medicalRecordRepository.save(medicalRecord);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Prescribe getPrescribeById(int id) {
        return prescribeRepository.findPrescribeById(id);
    }

    @Override
    public boolean delPrescribeById(int id) {
        try {
            Prescribe prescribeById = prescribeRepository.findPrescribeById(id);
            MedicalRecord medicalRecord = medicalRecordRepository.findMedicalRecordById(prescribeById.getVisitNumber());
            StringBuilder sb = new StringBuilder();
            for (String item : medicalRecord.getPrescribeIds().split(",")) {
                if (!item.equals(String.valueOf(id))) {
                    sb.append(item).append(",");
                }
            }
            medicalRecord.setPrescribeIds(sb.toString());
            medicalRecordRepository.save(medicalRecord);
            prescribeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateStatusByIds(List<Integer> prescribeIds, int status) {
        try {
            //1.待使用
            return prescribeRepository.updateStatus(prescribeIds, status) > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Page<Prescribe> getPrescribeAllByPageAndUserName(String userName, int pageNumber, int pageSize) {
        if (userName == null || userName.equals("")) {
            userName = "%%";
        } else  {
            userName = "%" + userName + "%";
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "status");
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return prescribeRepository.findPrescribeByUserNameLike(userName, pageable).map(prescribe -> {
            User user = userRepository.findUserById(prescribe.getDoctorId());
            Drug drug = drugRepository.findDrugById(prescribe.getDrugId());
            prescribe.setDoctorName(user.getName());
            prescribe.setDrugName(drug.getName());
            return prescribe;
        });
    }
}
