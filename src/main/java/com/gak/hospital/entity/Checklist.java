package com.gak.hospital.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.*;
import java.util.Date;

/**
 * 检查单
 */
@Entity
@Data
@Table(name="checklist")
public class Checklist implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //医技id
    private int medicalId;
    //就诊号
    private int visitNumber;
    private String userName;
    private int doctorId;
    private int medicalNumber;
    //0.待付款 1.待使用 2.已使用  当已付款在状态上加 当使用后在总状态上减
    private int status;
    @CreatedDate
    private Date createDate;
    @Transient
    private String doctorName;
    @Transient
    private String medicalName;
}
