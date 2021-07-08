package com.gak.hospital.service.impl;

import com.gak.hospital.entity.Drug;
import com.gak.hospital.repository.DrugRepository;
import com.gak.hospital.service.DrugService;
import com.gak.hospital.utils.DateUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DrugServiceImpl implements DrugService {

    private final @NonNull DrugRepository drugRepository;

    @Override
    public boolean saveOrUpdate(Drug drug) {
        try {
            drugRepository.save(drug);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Page<Drug> getDrugAllByPageAndDrugName(String drugName, int pageNumber, int pageSize) {
        if (drugName == null || drugName.equals("")) {
            drugName = "%%";
        } else  {
            drugName = "%" + drugName + "%";
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Drug> page = drugRepository.findDrugByNameLike(drugName, pageable);
        return page.map(drug -> {
            drug.setManufactureDateName(DateUtils.formatYMD(drug.getManufactureDate()));
            return drug;
        });
    }

    @Override
    public boolean delDrugByIds(List<Integer> ids) {
        try {
            drugRepository.deleteDrugByIdIn(ids);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Drug> getAll() {
        return drugRepository.findAll();
    }
}
