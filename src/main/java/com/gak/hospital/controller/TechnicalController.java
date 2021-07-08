package com.gak.hospital.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gak.hospital.entity.Medical;
import com.gak.hospital.service.ChecklistService;
import com.gak.hospital.service.MedicalRecordService;
import com.gak.hospital.service.MedicalService;
import com.gak.hospital.utils.ResultUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/technical")
@RequiredArgsConstructor
public class TechnicalController {

    private final @NonNull MedicalService medicalService;
    private final @NonNull ChecklistService checklistService;
    private final @NonNull MedicalRecordService medicalRecordService;

    /*****************************************医技管理*****************************************/
    @PostMapping("/medical")
    public HttpEntity<?> medical(@RequestBody JSONObject object) {
        int pageNumber = object.getIntValue("pageNumber");
        String medicalName = object.getString("medicalName");
        int pageSize = object.getIntValue("pageSize");
        if (pageNumber > 0) {
            return ResponseEntity.ok(ResultUtils.pageToJson(medicalService.getMedicalAllByPageAndMedicalName(medicalName, pageNumber, pageSize)));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/medicalAddOrUpdate")
    public HttpEntity<?> medicalAddOrUpdate(@RequestBody JSONObject object) {
        Medical medical = object.toJavaObject(Medical.class);
        if (medical != null) {
            return ResponseEntity.ok(medicalService.saveOrUpdate(medical));
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/medicalDel")
    public HttpEntity<?> medicalDel(@RequestBody JSONObject object) {
        List<Integer> ids = JSONObject.parseArray(object.getJSONArray("ids").toJSONString(), Integer.class);
        if (ids.size() != 0) {
            return ResponseEntity.ok(medicalService.delMedicalByIds(ids));
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }

    /*****************************************检查单管理*****************************************/
    @PostMapping("/checklist")
    public HttpEntity<?> checklist(@RequestBody JSONObject object) {
        int pageNumber = object.getIntValue("pageNumber");
        String userName = object.getString("userName");
        int pageSize = object.getIntValue("pageSize");
        if (pageNumber > 0) {
            return ResponseEntity.ok(ResultUtils.pageToJson(checklistService.getChecklistAllByPageAndUserName(userName, pageNumber, pageSize)));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/use")
    public HttpEntity<?> use(@RequestBody JSONObject object) {
        int visitNumber = object.getIntValue("visitNumber");
        List<Integer> checklistIds = JSONArray.parseArray(object.get("checklistIds").toString(), Integer.class);
        if (checklistIds.size() != 0 && checklistService.updateStatusByIds(checklistIds, 2)) {
            // 2使用
            return ResponseEntity.ok(medicalRecordService.updateHistory(checklistIds.size(), visitNumber));
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }
}
