package com.gak.hospital.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.*;
import java.util.Date;

/**
 * 处方
 */
@Entity
@Data
@Table(name="prescribe")
public class Prescribe implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //药品id
    private int drugId;
    //就诊号
    private int visitNumber;
    private String userName;
    //医生
    private int doctorId;
    //药品数量
    private int drugNumber;
    //0.待付款 1.待使用 2.已使用 当已付款在状态上加 当使用后在总状态上减
    private int status;
    @CreatedDate
    private Date createDate;
    @Transient
    private String drugName;
    @Transient
    private float price;
    @Transient
    private String doctorName;
}
