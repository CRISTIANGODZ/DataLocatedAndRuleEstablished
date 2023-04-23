package com.cqupt.case_data_location.controller;


import com.cqupt.case_data_location.service.ImportDataService;
import com.cqupt.case_data_location.utils.Logger;
import com.cqupt.case_data_location.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @auther DyingZhang
 * @Create 2022-10-12 下午 4:07
 * @Discriptioon
 */
@RestController
@RequestMapping("/electronic/case")
public class ImportDataController {

    @Autowired
    ImportDataService importDataService;

    /**
     * 以CSV格式导入数据并存入数据库中
     * @return
     */
    @PostMapping("/submit/csv")
    public R submitCSVController(@RequestPart("csvData") MultipartFile csvData, HttpSession session) throws IOException {
        Logger.info("\n上传文件接口 ---> ");
        String csvPath = importDataService.submitCSVService(csvData, session);
        boolean isSuccessful = importDataService.parseCSVService(csvPath);
        if (isSuccessful) {
            Logger.info(" <--- 上传文件成功");
            Logger.info("csvPath:" + csvPath);
            return R.ok().message("上传成功！\n");
        } else {
            Logger.info(" <--- 上传文件失败");
            return R.error().message("上传失败！\n");
        }
    }

}
