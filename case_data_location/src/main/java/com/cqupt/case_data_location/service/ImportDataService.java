package com.cqupt.case_data_location.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/**
 * @auther DyingZhang
 * @Create 2022-10-13 下午 10:20
 * @Discriptioon
 */
public interface ImportDataService {

    /**
     * 以CSV格式导入数据
     * @param csvData
     */
    String  submitCSVService(MultipartFile csvData, HttpSession session);

    /**
     * 解析CSV数据
     * @param csvPath
     */
    boolean parseCSVService(String csvPath);


}