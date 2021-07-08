package com.gak.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.gak.hospital.entity.Checklist;
import com.gak.hospital.entity.MedicalRecord;
import com.gak.hospital.entity.Prescribe;
import com.gak.hospital.service.*;
import com.gak.hospital.utils.ResultUtils;
import com.gak.hospital.utils.RoleUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final @NonNull MedicalRecordService medicalRecordService;
    private final @NonNull ChecklistService checklistService;
    private final @NonNull PrescribeService prescribeService;
    private final @NonNull UserService userService;
    private final @NonNull MedicalService medicalService;


    /*****************************************挂号信息管理*****************************************/
    @PostMapping("/record")
    public HttpEntity<?> record(@RequestBody JSONObject object) {
        int pageNumber = object.getIntValue("pageNumber");
        int visitNumber = object.getIntValue("visitNumber");
        List<Integer> statusList = JSONObject.parseArray(object.getString("statusList"), Integer.class);
        int pageSize = object.getIntValue("pageSize");
        if (pageNumber > 0) {
            if (visitNumber == 0) {
                return ResponseEntity.ok(ResultUtils.pageToJson(medicalRecordService.getMedicalRecordAllByPageAndStatusList(statusList, pageNumber, pageSize)));
            } else  {
                return ResponseEntity.ok(ResultUtils.entityToPageJson(medicalRecordService.getMedicalRecordById(visitNumber)));
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取全部医生
     * @return list
     */
    @GetMapping("/doctorAll")
    public HttpEntity<?> doctorAll() {
        return ResponseEntity.ok(userService.getAllByRole(RoleUtils.ROLE_DOCTOR));
    }

    /**
     * 获取全部医技
     * @return list
     */
    @GetMapping("/medicalAll")
    public HttpEntity<?> medicalAll() {
        return ResponseEntity.ok(medicalService.getAll());
    }

    /**
     * 根据id获取处方
     */
    @PostMapping("/getPrescribe")
    public HttpEntity<?> getPrescribe(@RequestBody JSONObject object) {
        return ResponseEntity.ok(prescribeService.getPrescribeById(object.getIntValue("id")));
    }

    /**
     * 根据id获取检查单
     */
    @PostMapping("/getChecklist")
    public HttpEntity<?> getChecklist(@RequestBody JSONObject object) {
        return ResponseEntity.ok(checklistService.getChecklistById(object.getIntValue("id")));
    }

    /**
     * 添加更新检查单
     * 同时保存到挂号信息里
     */
    @PostMapping("/checklistAddOrUpdate")
    public HttpEntity<?> checklistAddOrUpdate(@RequestBody JSONObject object) {
        Checklist checklist = object.toJavaObject(Checklist.class);
        if (checklist != null && checklist.getVisitNumber() != 0 && checklistService.addOrUpdate(checklist)) {
            return ResponseEntity.ok(medicalRecordService.getMedicalRecordById(checklist.getVisitNumber()));
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * 添加更新处方
     * 同时保存到挂号信息里
     */
    @PostMapping("/prescribeAddOrUpdate")
    public HttpEntity<?> prescribeAddOrUpdate(@RequestBody JSONObject object) {
        Prescribe prescribe = object.toJavaObject(Prescribe.class);
        if (prescribe != null && prescribe.getVisitNumber() != 0 && prescribeService.addOrUpdate(prescribe)) {
            return ResponseEntity.ok(medicalRecordService.getMedicalRecordById(prescribe.getVisitNumber()));
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * 删除检查单
     * 同时保存到挂号信息里
     */
    @PostMapping("/checklistDel")
    public HttpEntity<?> checklistDel(@RequestBody JSONObject object) {
        return ResponseEntity.ok(checklistService.delChecklistById(object.getIntValue("id")));
    }

    /**
     * 删除处方
     * 同时保存到挂号信息里
     */
    @PostMapping("/prescribeDel")
    public HttpEntity<?> prescribeDel(@RequestBody JSONObject object) {
        return ResponseEntity.ok(prescribeService.delPrescribeById(object.getIntValue("id")));
    }

    /**
     * 更新挂号信息
     * @param object form
     */
    @PostMapping("/recordUpdate")
    public HttpEntity<?> recordUpdate(@RequestBody JSONObject object) {
        MedicalRecord medicalRecord = object.toJavaObject(MedicalRecord.class);
        if (medicalRecord != null && medicalRecord.getId() != 0) {
            return ResponseEntity.ok(medicalRecordService.saveOrUpdate(medicalRecord));
        }
        return ResponseEntity.badRequest().build();
    }

    /*****************************************病历管理*****************************************/
    @PostMapping("/medicalRecord")
    public HttpEntity<?> medicalRecord(@RequestBody JSONObject object) {
        int pageNumber = object.getIntValue("pageNumber");
        String userName = object.getString("userName");
        int pageSize = object.getIntValue("pageSize");
        int visitNumber = object.getIntValue("visitNumber");
        if (pageNumber > 0) {
            if (visitNumber == 0) {
                return ResponseEntity.ok(ResultUtils.pageToJson(medicalRecordService.getRecordAllByPage(userName, pageNumber, pageSize)));
            } else  {
                return ResponseEntity.ok(ResultUtils.entityToPageJson(medicalRecordService.getRecordById(visitNumber)));
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
