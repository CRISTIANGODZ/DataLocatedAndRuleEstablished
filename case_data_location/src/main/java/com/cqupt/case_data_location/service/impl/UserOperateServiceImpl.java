package com.cqupt.case_data_location.service.impl;


import com.cqupt.case_data_location.mapper.*;
import com.cqupt.case_data_location.pojo.entity.Patients;
import com.cqupt.case_data_location.pojo.entity.Texts;
import com.cqupt.case_data_location.service.UserOperateService;
import com.cqupt.case_data_location.utils.ESUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @auther DyingZhang
 * @Create 2022-10-16 下午 12:36
 * @Discriptioon
 */
@Transactional(isolation = Isolation.READ_COMMITTED)
@Service
public class UserOperateServiceImpl implements UserOperateService {

    @Autowired
    FirstCategoryMapper firstCategoryMapper;
    @Autowired
    PatientsMapper patientsMapper;
    @Autowired
    DiseasesMapper diseasesMapper;
    @Autowired
    TextsMapper textsMapper;
    @Autowired
    ThemesMapper themesMapper;
    @Autowired
    ESUtils esUtils;


    /**
     * 删除病人某条text数据信息
     * @param texts
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void deleteText(Texts texts) {
        //1.删除该Texts字段
        textsMapper.deleteText(texts);
        //2.删除themes相关信息
        Long themeId = texts.getThemeId();
        themesMapper.deleteThemes(themeId);
    }

    /**
     * 修改病人某条text数据信息
     * @param texts
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void updateText(Texts texts) {
        textsMapper.updateText(texts);
    }

    /**
     * 删除某个病人的所有相关信息
     * @param texts
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void deletePatientAllText(Texts texts) {
        //1根据病人id查找出所有text
        Long patientId = texts.getPatientId();
        List<Texts> allTexts = textsMapper.getAllThemeIdByPatientId(patientId);
        //2.删除所有text
        for (Texts text : allTexts){
            textsMapper.deleteText(text);
        }
        //3.删除所有的theme信息
        for (Texts text : allTexts){
            Long themeId = text.getThemeId();
            themesMapper.deleteThemes(themeId);
        }
        //4.删除第一种类的信息
        Long firstCategoryId = texts.getFirstCategoryId();
        firstCategoryMapper.deleteFirstCategoryById(firstCategoryId);
        //5.删除patient信息
        patientsMapper.deletePatient(patientId);
        //6.删除es中patient信息
        esUtils.delDoc("texts", String.valueOf(patientId));
    }

    /**
     * 判断数据是否存在
     * @param textId
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public boolean isTextExists(Long textId) {
        Texts text = textsMapper.getTextByTextId(textId);
        if (text != null){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据templateId获取text
     * @param textId
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public Texts getSingleText(Long textId) {
        Texts text = textsMapper.getTextByTextId(textId);
        return text;
    }

    /**
     * 根据id获取所有的Texts
     * @param id
     * @return
     */
    @Override
    public List<Texts> getTextsByPatientId(Long id) {
        List<Texts> textsList = textsMapper.getPatientTexts(id);
        return textsList;
    }

    /**
     * 获取所有的Patients
     * @return
     */
    @Override
    public List<Patients> getPatients() {
        List<Patients> patientsList = patientsMapper.getAllPatient();
        return patientsList;
    }

}
