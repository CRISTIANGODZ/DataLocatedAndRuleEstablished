package com.cqupt.case_data_location.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 6:55
 * @Discriptioon
 */
@Data
@NoArgsConstructor
public class Patients {
    private Long patientId;
    private String name;

    private Long diseaseId;

    private String other;

    private String patient_num;


    public Patients(String name, String other, String patient_num) {
        this.name = name;
        this.other = other;
        this.patient_num = patient_num;
    }
}
