package com.gak.hospital.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gak.hospital.entity.Drug;
import com.gak.hospital.entity.Drugstore;
import com.gak.hospital.service.DrugService;
import com.gak.hospital.service.DrugstoreService;
import com.gak.hospital.service.MedicalRecordService;
import com.gak.hospital.service.PrescribeService;
import com.gak.hospital.utils.ResultUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pharmacist")
@RequiredArgsConstructor
public class PharmacistController {

    private final @NonNull DrugstoreService drugstoreService;
    private final @NonNull DrugService drugService;
    private final @NonNull PrescribeService prescribeService;
    private final @NonNull MedicalRecordService medicalRecordService;

    /*****************************************药房管理*****************************************/
    @PostMapping("/drugstore")
    public HttpEntity<?> drugstore(@RequestBody JSONObject object) {
        int pageNumber = object.getIntValue("pageNumber");
        String drugName = object.getString("drugName");
        int pageSize = object.getIntValue("pageSize");
        if (pageNumber > 0) {
            return ResponseEntity.ok(ResultUtils.pageToJson(drugstoreService.getDrugstoreAllByPageAndDrugName(drugName, pageNumber, pageSize)));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/drugstoreAddOrUpdate")
    public HttpEntity<?> drugstoreAddOrUpdate(@RequestBody JSONObject object) {
        Drugstore drugstore = object.toJavaObject(Drugstore.class);
        if (drugstore != null) {
            return ResponseEntity.ok(drugstoreService.saveOrUpdate(drugstore));
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/drugAll")
    public HttpEntity<?> drugAll() {
        return ResponseEntity.ok(drugService.getAll());
    }

    /*****************************************药品管理*****************************************/
    @PostMapping("/drug")
    public HttpEntity<?> drug(@RequestBody JSONObject object) {
        int pageNumber = object.getIntValue("pageNumber");
        String drugName = object.getString("drugName");
        int pageSize = object.getIntValue("pageSize");
        if (pageNumber > 0) {
            return ResponseEntity.ok(ResultUtils.pageToJson(drugService.getDrugAllByPageAndDrugName(drugName, pageNumber, pageSize)));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/drugAddOrUpdate")
    public HttpEntity<?> drugAddOrUpdate(@RequestBody JSONObject object) {
        Drug drug = object.toJavaObject(Drug.class);
        if (drug != null) {
            return ResponseEntity.ok(drugService.saveOrUpdate(drug));
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/drugDel")
    public HttpEntity<?> drugDel(@RequestBody JSONObject object) {
        List<Integer> ids = JSONObject.parseArray(object.getJSONArray("ids").toJSONString(), Integer.class);
        if (ids.size() != 0) {
            return ResponseEntity.ok(drugService.delDrugByIds(ids));
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }

    /*****************************************处方管理*****************************************/
    @PostMapping("/prescribe")
    public HttpEntity<?> prescribe(@RequestBody JSONObject object) {
        int pageNumber = object.getIntValue("pageNumber");
        String userName = object.getString("userName");
        int pageSize = object.getIntValue("pageSize");
        if (pageNumber > 0) {
            return ResponseEntity.ok(ResultUtils.pageToJson(prescribeService.getPrescribeAllByPageAndUserName(userName, pageNumber, pageSize)));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 一次只能使用一个就诊号
     */
    @PostMapping("/use")
    public HttpEntity<?> use(@RequestBody JSONObject object) {
        int visitNumber = object.getIntValue("visitNumber");
        List<Integer> prescribeIds = JSONArray.parseArray(object.get("prescribeIds").toString(), Integer.class);
        if (prescribeIds.size() != 0 && prescribeService.updateStatusByIds(prescribeIds, 2)) {
            // 2使用
            return ResponseEntity.ok(medicalRecordService.updateHistory(prescribeIds.size(), visitNumber));
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }
}
