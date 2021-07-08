package com.gak.hospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 药品
 */
@Entity
@Data
@Table(name="drug")
public class Drug implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    //批号
    private String batchNumber;
    private float price;
    //类别 处方药和非处方药
    private String category;
    //生产日期
    private Date manufactureDate;
    //保质期（月）
    private int dueMonth;
    @Transient
    private String manufactureDateName;
}
