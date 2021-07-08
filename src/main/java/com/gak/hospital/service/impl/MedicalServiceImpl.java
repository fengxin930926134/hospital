package com.gak.hospital.service.impl;

import com.gak.hospital.entity.Medical;
import com.gak.hospital.repository.MedicalRepository;
import com.gak.hospital.service.MedicalService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalServiceImpl implements MedicalService {

    private final @NonNull MedicalRepository medicalRepository;

    @Override
    public boolean saveOrUpdate(Medical medical) {
        try {
            medicalRepository.save(medical);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Page<Medical> getMedicalAllByPageAndMedicalName(String medicalName, int pageNumber, int pageSize) {
        if (medicalName == null || medicalName.equals("")) {
            medicalName = "%%";
        } else  {
            medicalName = "%" + medicalName + "%";
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return medicalRepository.findMedicalByMedicalNameLike(medicalName, pageable);
    }

    @Override
    public boolean delMedicalByIds(List<Integer> ids) {
        try {
            medicalRepository.deleteMedicalByMedicalIdIn(ids);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Medical> getAll() {
        return medicalRepository.findAll();
    }
}
