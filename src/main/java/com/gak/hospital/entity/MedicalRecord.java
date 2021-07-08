package com.gak.hospital.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.*;
import java.util.Date;

/**
 * 病历
 */
@Entity
@Data
@Table(name="medical_record")
public class MedicalRecord implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //就诊号
    private int id;
    //患者id
    private int patientId;
    private String checklistIds;
    private String prescribeIds;
    @CreatedDate
    private Date createDate;
    //0.未检查 1.待付款（已处理）
    private int status;
    //是否病历 0.否 1.是 >1有还未使用药品或者检查单
    private int history;
    //备注
    private String remark;
    //患者名
    @Transient
    private String userName;
    @Transient
    private String sexName;
    @Transient
    private String phone;
    @Transient
    private String createDateName;
    @Transient
    private String[] checklistIdsArr;
    @Transient
    private String[] prescribeIdsArr;
}
