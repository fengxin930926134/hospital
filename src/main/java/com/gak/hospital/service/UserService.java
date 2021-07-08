package com.gak.hospital.service;

import com.alibaba.fastjson.JSONObject;
import com.gak.hospital.entity.User;
import org.springframework.data.domain.Page;
import java.util.List;

public interface UserService {

    User getUserByToken(String token);

    boolean updateUserByUser(User user);

    boolean updateUserPassword(User user);

    /**
     * 添加or更新 管理员
     * @param user user
     * @return boolean
     */
    boolean saveOrUpdate(User user);

    /**
     * 分页模糊查询
     * @param name name
     * @param pageNumber 页
     * @param pageSize 一页多少条
     * @return page
     */
    Page<User> getUserAllByPageAndName(String name, int pageNumber, int pageSize);

    /**
     * 根据ids删除 （管理员）
     * @param ids ids
     * @return boolean
     */
    boolean delUserByIds(List<Integer> ids);

    /**
     * 获取指定类型的用户
     * @param role role
     * @return List
     */
    List<User> getAllByRole(String role);

    /**
     * 获取账单
     * @param user user
     * @return json
     */
    JSONObject getBill(User user);
}
