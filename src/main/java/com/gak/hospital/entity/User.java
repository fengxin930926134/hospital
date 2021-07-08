package com.gak.hospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="user")
public class User implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String name;
    // 1男 2女
    private int sex;
    private String phone;
    //生日
    private Date birthDate;
    //科室
    private Integer deptId;
    private String role;
    //非一个到数据库表的字段的映射，ORM框架将忽略该属性
    @Transient
    private String newPassword;
    @Transient
    private String deptName;
    @Transient
    private String[] roleList;
    @Transient
    private String sexName;
    @Transient
    private String birthDateName;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public User() {}
}