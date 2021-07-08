package com.gak.hospital.entity;

import lombok.Data;
import javax.persistence.*;

/**
 * 科室
 */
@Entity
@Data
@Table(name="dept")
public class Dept implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int deptId;
    private String deptName;
}
