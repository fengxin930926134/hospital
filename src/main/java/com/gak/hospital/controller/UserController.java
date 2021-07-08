package com.gak.hospital.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gak.hospital.entity.MedicalRecord;
import com.gak.hospital.entity.User;
import com.gak.hospital.filter.AuthTokenFilter;
import com.gak.hospital.service.ChecklistService;
import com.gak.hospital.service.MedicalRecordService;
import com.gak.hospital.service.PrescribeService;
import com.gak.hospital.service.UserService;
import com.gak.hospital.utils.ResultUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final @NonNull UserService userService;
    private final @NonNull MedicalRecordService medicalRecordService;
    private final @NonNull ChecklistService checklistService;
    private final @NonNull PrescribeService prescribeService;
    /*****************************************基本信息管理*****************************************/
    @PostMapping("/info")
    public HttpEntity<?> info(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(AuthTokenFilter.AUTH_TOKEN_HEADER_NAME);
        return ResponseEntity.ok(userService.getUserByToken(token));
    }

    @PostMapping("/update")
    public HttpEntity<?> update(@NonNull @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUserByUser(user));
    }

    @PostMapping("/updatePassword")
    public HttpEntity<?> updatePassword(@NonNull @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUserPassword(user));
    }

    /*****************************************自助挂号*****************************************/

    //挂号 成功返回就诊号
    @GetMapping("/appointment")
    public HttpEntity<?> appointment(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(AuthTokenFilter.AUTH_TOKEN_HEADER_NAME);
        User userByToken = userService.getUserByToken(token);
        if (userByToken != null) {
            MedicalRecord medicalRecord = new MedicalRecord();
            medicalRecord.setPatientId(userByToken.getId());
            if (medicalRecordService.saveOrUpdate(medicalRecord)) {
                return ResponseEntity.ok(medicalRecordService.getIdByUserId(userByToken.getId()));
            }
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * 检查是否已挂号 返回就诊号
     */
    @GetMapping("/visit")
    public HttpEntity<?> visit(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(AuthTokenFilter.AUTH_TOKEN_HEADER_NAME);
        User userByToken = userService.getUserByToken(token);
        if (userByToken != null) {
            return ResponseEntity.ok(medicalRecordService.getIdByUserId(userByToken.getId()));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /*****************************************自助缴费*****************************************/

    //获取账单
    @GetMapping("/bill")
    public HttpEntity<?> bill(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(AuthTokenFilter.AUTH_TOKEN_HEADER_NAME);
        User userByToken = userService.getUserByToken(token);
        if (userByToken != null) {
            return ResponseEntity.ok(userService.getBill(userByToken).toJSONString());
        }
        return ResponseEntity.badRequest().build();
    }

    //付钱过后回调这个接口即可刷新状态
    @PostMapping("/payment")
    public HttpEntity<?> payment(@NonNull @RequestBody JSONObject object) {
        List<Integer> checklistIds = JSONArray.parseArray(object.get("checklistIds").toString(), Integer.class);
        List<Integer> prescribeIds = JSONArray.parseArray(object.get("prescribeIds").toString(), Integer.class);
        if (checklistIds.size() != 0) {
            if (!checklistService.updateStatusByIds(checklistIds, 1)) {
                return ResponseEntity.badRequest().build();
            }
        }
        if (prescribeIds.size() != 0) {
            if (!prescribeService.updateStatusByIds(prescribeIds, 1)) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok(true);
    }

    /*****************************************挂号记录*****************************************/

    @GetMapping("/record")
    public HttpEntity<?> record(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(AuthTokenFilter.AUTH_TOKEN_HEADER_NAME);
        User userByToken = userService.getUserByToken(token);
        if (userByToken != null) {
            return ResponseEntity.ok(userService.getBill(userByToken).toJSONString());
        }
        return ResponseEntity.badRequest().build();
    }
}
