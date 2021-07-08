package com.gak.hospital.repository;

import com.gak.hospital.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findFirstUserByUsernameOrPhone(String username, String phone);

    @Modifying
    @Transactional
    @Query("update User a SET " +
            "a.username = CASE WHEN :#{#user.username} IS NULL THEN a.username ELSE :#{#user.username} END ," +
            "a.password = CASE WHEN :#{#user.password} IS NULL THEN a.password ELSE :#{#user.password} END ," +
            "a.name = CASE WHEN :#{#user.name} IS NULL THEN a.name ELSE :#{#user.name} END ," +
            "a.phone = CASE WHEN :#{#user.phone} IS NULL THEN a.phone ELSE :#{#user.phone} END ," +
            "a.deptId = CASE WHEN :#{#user.deptId} IS NULL THEN a.deptId ELSE :#{#user.deptId} END ," +
            "a.birthDate =  CASE WHEN :#{#user.birthDate} IS NULL THEN a.birthDate ELSE :#{#user.birthDate} END ," +
            "a.sex =  CASE WHEN :#{#user.sex} = 0 THEN a.sex ELSE :#{#user.sex} END ," +
            "a.role =  CASE WHEN :#{#user.role} IS NULL THEN a.role ELSE :#{#user.role} END " +
            "where a.id = :#{#user.id}")
    int update(User user);

    User findUserById(int id);

    Page<User> findUserByNameLike(String name, Pageable pageable);

    @Transactional
    void deleteUserByIdIn(List<Integer> ids);
}
