package com.cqupt.electroniccase.service;

import java.io.IOException;

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
    void getCSVService() throws IOException;

}
