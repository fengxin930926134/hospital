package com.gak.hospital.service.impl;

import com.gak.hospital.entity.Drug;
import com.gak.hospital.entity.Drugstore;
import com.gak.hospital.repository.DrugRepository;
import com.gak.hospital.repository.DrugstoreRepository;
import com.gak.hospital.service.DrugstoreService;
import com.gak.hospital.utils.DateUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrugstoreServiceImpl implements DrugstoreService {

    private final @NonNull DrugstoreRepository drugstoreRepository;
    private final @NonNull DrugRepository drugRepository;

    @Override
    public Page<Drugstore> getDrugstoreAllByPageAndDrugName(String drugName, int pageNumber, int pageSize) {
        if (drugName == null || drugName.equals("")) {
            drugName = "%%";
        } else  {
            drugName = "%" + drugName + "%";
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return drugstoreRepository.findDrugstoreByDrugNameLikeAndNumberNot(drugName, 0, pageable).map(drugstore -> {
            Drug drug = drugRepository.findDrugById(drugstore.getDrugId());
            drug.setManufactureDateName(DateUtils.formatYMD(drug.getManufactureDate()));
            drugstore.setDrug(drug);
            return drugstore;
        });
    }

    @Override
    public boolean saveOrUpdate(Drugstore drugstore) {
        try {
            if (drugstore.getDrug() != null) {
                drugstore.setDrugId(drugstore.getDrug().getId());
                drugstore.setDrugName(drugstore.getDrug().getName());
            }
            Drugstore drugstoreByDrugId = drugstoreRepository.findDrugstoreByDrugId(drugstore.getDrugId());
            //判断更新类型补充数据
            if (drugstoreByDrugId != null && drugstore.getDrug() != null) {
                drugstoreByDrugId.setNumber(drugstoreByDrugId.getNumber() + drugstore.getNumber());
                drugstore = drugstoreByDrugId;
            }
            drugstoreRepository.save(drugstore);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
