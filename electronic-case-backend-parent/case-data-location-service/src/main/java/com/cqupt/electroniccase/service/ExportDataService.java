package com.cqupt.electroniccase.service;

import pojo.Texts;

import java.util.List;

/**
 * @auther DyingZhang
 * @Create 2022-10-15 下午 6:38
 * @Discriptioon
 */
public interface ExportDataService {

    /**
     * 将数据库中数据查询，封装到CSV文件中
     *
     */
    void getCSVService(List<Texts> allTexts);

    /**
     * 获取用户的所有相关的text信息
     * @param name
     * @return
     */
    List<Texts> getPatientAllText(String name);
}
