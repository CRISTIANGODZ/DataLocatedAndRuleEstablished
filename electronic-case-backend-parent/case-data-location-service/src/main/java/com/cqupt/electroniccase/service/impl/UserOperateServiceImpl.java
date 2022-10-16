package com.cqupt.electroniccase.service.impl;

import com.cqupt.electroniccase.mapper.*;
import com.cqupt.electroniccase.service.UserOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.Texts;

/**
 * @auther DyingZhang
 * @Create 2022-10-16 下午 12:36
 * @Discriptioon
 */
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
}
