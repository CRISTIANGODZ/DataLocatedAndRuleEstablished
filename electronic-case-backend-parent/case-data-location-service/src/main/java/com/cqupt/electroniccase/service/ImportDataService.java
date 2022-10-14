package com.cqupt.electroniccase.service;

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
    String  submitCSVService(MultipartFile csvData);

    /**
     * 解析CSV数据
     * @param csvPath
     */
    void parseCSVService(String csvPath);

}
