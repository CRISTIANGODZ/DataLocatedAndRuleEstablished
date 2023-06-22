package com.cqupt.case_data_location.service;


import com.cqupt.case_data_location.pojo.entity.Patients;
import com.cqupt.case_data_location.pojo.entity.Texts;

import java.util.List;

/**
 * @auther DyingZhang
 * @Create 2022-10-16 下午 12:36
 * @Discriptioon
 */
public interface UserOperateService {

    /**
     * 删除病人某条text数据信息
     * @param texts
     */
    void deleteText(Texts texts);

    /**
     * 修改病人某条text数据信息
     * @param texts
     */
    void updateText(Texts texts);

    /**
     * 删除某个病人的所有相关信息
     * @param texts
     */
    void deletePatientAllText(Texts texts);

    /**
     * 判断数据是否存在
     * @param textId
     * @return
     */
    boolean isTextExists(Long textId);

    /**
     * 根据templateId获取text
     * @param textId
     * @return
     */
    Texts getSingleText(Long textId);

    /**
     * 根据病人id查询病人信息
     * @param id
     * @return
     */
    List<Texts> getTextsByPatientId(Long id);

    /**
     * 返回所有的patient
     * @return
     */
    List<Patients> getPatients();
}
