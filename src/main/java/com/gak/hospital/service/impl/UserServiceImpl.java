package com.gak.hospital.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gak.hospital.entity.BillInfo;
import com.gak.hospital.entity.Dept;
import com.gak.hospital.entity.MedicalRecord;
import com.gak.hospital.entity.User;
import com.gak.hospital.repository.*;
import com.gak.hospital.service.UserService;
import com.gak.hospital.utils.DateUtils;
import com.gak.hospital.utils.RoleUtils;
import com.gak.hospital.utils.TokenUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final @NonNull UserRepository userRepository;
    private final @NonNull DeptRepository deptRepository;
    private final @NonNull MedicalRecordRepository medicalRecordRepository;
    private final @NonNull BillInfoRepository billInfoRepository;

    @Override
    public User getUserByToken(String token) {
        String userNameFromToken = TokenUtils.getUserNameFromToken(token);
        User one = userRepository.findFirstUserByUsernameOrPhone(userNameFromToken, userNameFromToken);
        one.setPassword("");
        //设置权限list
        one.setRoleList(RoleUtils.getRoles(one.getRole()));
        return one;
    }

    @Override
    public boolean updateUserByUser(User user) {
        user.setRole(null);
        user.setUsername(null);
        user.setPassword(null);
        return userRepository.update(user) > 0;
    }

    @Override
    public boolean updateUserPassword(User user) {
        //判断是否存在
        User usernameOrPhone = userRepository.findFirstUserByUsernameOrPhone(user.getUsername(), user.getUsername());
        if (usernameOrPhone == null) {
            return false;
        }
        if (user.getPassword() != null && !user.getPassword().equals("")) {
            //判断密码是否正确
            if (new BCryptPasswordEncoder().matches(user.getPassword(), usernameOrPhone.getPassword())) {
                //保存新密码
                User update = new User();
                update.setPassword(new BCryptPasswordEncoder().encode(user.getNewPassword()));
                update.setId(usernameOrPhone.getId());
                return userRepository.update(update) > 0;
            }
        }
        return false;
    }

    @Override
    public boolean saveOrUpdate(User user) {
        try {
            if (user.getId() != 0) {
                User userById = userRepository.findUserById(user.getId());
                //判断密码有没有改变
                if (!user.getPassword().equals(userById.getPassword())) {
                    user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                }
            }
            //读取角色权限
            StringBuilder stringBuilder = new StringBuilder();
            for (String role :user.getRoleList()) {
                stringBuilder.append(role).append(",");
            }
            user.setRole(stringBuilder.toString());
            //保存
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Page<User> getUserAllByPageAndName(String name, int pageNumber, int pageSize) {
        if (name == null || name.equals("")) {
            name = "%%";
        } else  {
            name = "%" + name + "%";
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<User> userPage = userRepository.findUserByNameLike(name, pageable);
        return userPage.map(user -> {
            //设置部门名
            if (user.getDeptId() != null) {
                Dept dept = deptRepository.findDeptByDeptId(user.getDeptId());
                user.setDeptName(dept != null? dept.getDeptName(): null);
            }
            //设置权限list
            user.setRoleList(RoleUtils.getRoles(user.getRole()));
            //设置性别
            user.setSexName(user.getSex() == 1 ? "男" : "女");
            //设置生日
            user.setBirthDateName(user.getBirthDate() != null ? DateUtils.formatYMD(user.getBirthDate()) : null);
            return user;
        });
    }

    @Override
    public boolean delUserByIds(List<Integer> ids) {
        try {
            userRepository.deleteUserByIdIn(ids);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<User> getAllByRole(String role) {
        List<User> all = userRepository.findAll();
        List<User> ret = new ArrayList<>();
        for (User user: all) {
            if (RoleUtils.existRole(role, user.getRole())) {
                ret.add(new User(user.getId(), user.getName()));
            }
        }
        return ret;
    }

    @Override
    public JSONObject getBill(User user) {
        JSONObject object = new JSONObject();
        //有可能存在已付费未使用 所有不用这个id
        List<Integer> prescribeIds = new ArrayList<>();
        List<Integer> checklistIds = new ArrayList<>();
        List<BillInfo> billInfos = new ArrayList<>();
        float price = 0;
        //获取未使用的单子
        MedicalRecord record = medicalRecordRepository.getFirstMedicalRecordByPatientIdAndStatusAndHistoryGreaterThan(user.getId(), 1, 1);
        if (record != null) {
            if ( record.getPrescribeIds() != null) {
                for (String id: record.getPrescribeIds().split(",")) {
                    prescribeIds.add(Integer.parseInt(id));
                }
            }
            if ( record.getChecklistIds() != null) {
                for (String id: record.getChecklistIds().split(",")) {
                    checklistIds.add(Integer.parseInt(id));
                }
            }
            List<BillInfo> prescribeArr = billInfoRepository.findPrescribeByIdInAndStatus(prescribeIds, 0);
            List<BillInfo> checklistArr = billInfoRepository.findChecklistByIdInAndStatus(checklistIds, 0);
            if (prescribeArr.size() != 0 || checklistArr.size() != 0) {
                prescribeIds.clear();
                checklistIds.clear();
                for (BillInfo billInfo: prescribeArr) {
                    prescribeIds.add(billInfo.getId());
                    price = price + billInfo.getPrice();
                }
                for (BillInfo billInfo: checklistArr) {
                    checklistIds.add(billInfo.getId());
                    price = price + billInfo.getPrice();
                }
                billInfos.addAll(prescribeArr);
                billInfos.addAll(checklistArr);
                //加入挂号价格
                billInfos.add(new BillInfo(record.getId(), user.getName(), "挂号单", 1, 50));
                price = price + 50;
                object.put("visitNumber", record.getId());
                object.put("prescribeIds", prescribeIds);
                object.put("checklistIds", checklistIds);
                object.put("billInfos", billInfos);
                object.put("price", price);
            }
        }
        return object;
    }
}
