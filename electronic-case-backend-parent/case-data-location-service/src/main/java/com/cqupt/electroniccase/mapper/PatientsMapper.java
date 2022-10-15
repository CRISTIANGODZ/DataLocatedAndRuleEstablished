package com.cqupt.electroniccase.mapper;

import org.apache.ibatis.annotations.Mapper;
import pojo.Patients;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 8:52
 * @Discriptioon
 */
@Mapper
public interface PatientsMapper {

    /**
     * 添加病人信息：病种名称，第一种类id，病人姓名
     * 并返回主键id
     * @param patients
     */
    void addPatient(Patients patients);
}
