package com.gak.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.gak.hospital.entity.Dept;
import com.gak.hospital.entity.User;
import com.gak.hospital.service.DeptService;
import com.gak.hospital.service.MedicalRecordService;
import com.gak.hospital.service.UserService;
import com.gak.hospital.utils.ResultUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final @NonNull DeptService deptService;
    private final @NonNull UserService userService;
    private final @NonNull MedicalRecordService medicalRecordService;

    /*****************************************部门管理*****************************************/
    @PostMapping("/dept")
    public HttpEntity<?> dept(@RequestBody JSONObject object) {
        int pageNumber = object.getIntValue("pageNumber");
        String deptName = object.getString("deptName");
        int pageSize = object.getIntValue("pageSize");
        if (pageNumber > 0) {
            return ResponseEntity.ok(ResultUtils.pageToJson(deptService.getDeptAllByPageAndDeptName(deptName, pageNumber, pageSize)));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/deptAddOrUpdate")
    public HttpEntity<?> deptAddOrUpdate(@RequestBody JSONObject object) {
        Dept dept = object.toJavaObject(Dept.class);
        if (dept != null) {
            return ResponseEntity.ok(deptService.addOrUpdateDept(dept));
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/deptDel")
    public HttpEntity<?> deptDel(@RequestBody JSONObject object) {
        List<Integer> ids = JSONObject.parseArray(object.getJSONArray("ids").toJSONString(), Integer.class);
        if (ids.size() != 0) {
            return ResponseEntity.ok(deptService.delDeptByIds(ids));
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/deptAll")
    public HttpEntity<?> deptAll() {
        return ResponseEntity.ok(deptService.getAll());
    }

    /*****************************************用户管理*****************************************/
    @PostMapping("/user")
    public HttpEntity<?> user(@RequestBody JSONObject object) {
        int pageNumber = object.getIntValue("pageNumber");
        String name = object.getString("name");
        int pageSize = object.getIntValue("pageSize");
        if (pageNumber > 0) {
            return ResponseEntity.ok(ResultUtils.pageToJson(userService.getUserAllByPageAndName(name, pageNumber, pageSize)));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/userAddOrUpdate")
    public HttpEntity<?> userAddOrUpdate(@RequestBody JSONObject object) {
        User user = object.toJavaObject(User.class);
        if (user != null) {
            return ResponseEntity.ok(userService.saveOrUpdate(user));
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/userDel")
    public HttpEntity<?> userDel(@RequestBody JSONObject object) {
        List<Integer> ids = JSONObject.parseArray(object.getJSONArray("ids").toJSONString(), Integer.class);
        if (ids.size() != 0) {
            return ResponseEntity.ok(userService.delUserByIds(ids));
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }
}
