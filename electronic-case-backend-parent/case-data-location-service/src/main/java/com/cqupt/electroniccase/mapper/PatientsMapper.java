package com.cqupt.electroniccase.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.cqupt.electroniccase.pojo.Patients;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 8:52
 * @Discriptioon
 */
@Mapper
public interface PatientsMapper {

    /**
     * 添加病人信息：病种id，病人姓名
     * 并返回主键id
     * @param patients
     */
    void addPatient(Patients patients);

    /**
     * 用patientId查询patient的相关信息
     * @param patientId
     * @return
     */
    Patients getPatient(@Param("patientId") Long patientId);

    /**
     * 根据name查询所有patient_id
     * @param name
     * @return
     */
    Patients getPatientIdByName(@Param("name") String name);

    /**
     * 根据patientId删除patient信息
     * @param patientId
     */
    void deletePatient(@Param("patientId") Long patientId);
}
