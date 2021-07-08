package com.gak.hospital.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 药房
 */
@Entity
@Data
@Table(name="drugstore")
public class Drugstore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int drugId;
    private String drugName;
    private int number;
    @Transient
    private Drug drug;
}
