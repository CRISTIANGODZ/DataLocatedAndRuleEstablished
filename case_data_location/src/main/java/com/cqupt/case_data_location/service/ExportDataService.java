package com.cqupt.case_data_location.service;


import com.cqupt.case_data_location.pojo.entity.Texts;

import javax.servlet.http.HttpSession;
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
    String getCSVService(List<Texts> allTexts, HttpSession session);

    /**
     * 分页查询
     * 获取用户的所有相关的text信息
     * @param name
     * @return
     */
    List<Texts> getPatientAllText(String name, Integer offset, Integer limits);

    /**
     * 获取用户的所有相关的text信息
     * @param name
     * @return
     */
    List<Texts> getPatientAllText(String name);

}
