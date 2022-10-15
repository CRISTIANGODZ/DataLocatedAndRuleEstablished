package com.cqupt.electroniccase.service.impl;

import com.cqupt.electroniccase.mapper.*;
import com.cqupt.electroniccase.service.ExportDataService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @auther DyingZhang
 * @Create 2022-10-15 下午 5:17
 * @Discriptioon
 */
public class ExportDataServiceImpl implements ExportDataService {

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

    @Override
    public void getCSVService() {

    }
}
