package com.gak.hospital.entity;

import lombok.Data;
import javax.persistence.*;

/**
 * 医技
 */
@Entity
@Data
@Table(name="medical")
public class Medical implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int medicalId;
    private String medicalName;
    private float medicalPrice;
}
