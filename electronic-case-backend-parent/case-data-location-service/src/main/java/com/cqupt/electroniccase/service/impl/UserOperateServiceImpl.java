package com.cqupt.electroniccase.service.impl;

import com.cqupt.electroniccase.mapper.*;
import com.cqupt.electroniccase.service.UserOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pojo.Texts;

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

    /**
     * 删除病人某条text数据信息
     * @param texts
     */
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
    @Override
    public void updateText(Texts texts) {
        textsMapper.updateText(texts);
    }

    /**
     * 删除某个病人的所有相关信息
     * @param texts
     */
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
    }


}
