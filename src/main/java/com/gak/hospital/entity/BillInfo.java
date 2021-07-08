package com.gak.hospital.entity;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 账单中间表
 */
@Data
@Entity
public class BillInfo implements java.io.Serializable{
    @Id
    private String uuid;
    //代表项目的id
    private int id;
    //就诊号
    private int visitNumber;
    private String userName;
    private String projectName;
    private int number;
    private float price;

    public BillInfo() {}

    public BillInfo(int visitNumber, String userName, String projectName, int number, float price) {
        this.visitNumber = visitNumber;
        this.userName = userName;
        this.number = number;
        this.projectName = projectName;
        this.price = price;
    }

    public BillInfo(@NonNull Prescribe prescribe,@NonNull Drug drug) {
        this.id = prescribe.getId();
        this.visitNumber = prescribe.getVisitNumber();
        this.userName = prescribe.getUserName();
        this.number = prescribe.getDrugNumber();
        this.projectName = drug.getName();
        this.price = drug.getPrice();
    }

    public BillInfo(@NonNull Checklist checklist, @NonNull Medical medical) {
        this.id = checklist.getId();
        this.visitNumber = checklist.getVisitNumber();
        this.userName = checklist.getUserName();
        this.number = checklist.getMedicalNumber();
        this.projectName = medical.getMedicalName();
        this.price = medical.getMedicalPrice();
    }
}
