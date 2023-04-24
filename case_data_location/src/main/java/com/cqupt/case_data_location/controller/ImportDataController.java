package com.cqupt.case_data_location.controller;

import com.cqupt.case_data_location.service.ImportDataService;
import com.cqupt.case_data_location.utils.Logger;
import com.cqupt.case_data_location.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @auther DyingZhang
 * @Create 2022-10-12 下午 4:07
 * @Discriptioon
 */
@Api(tags = "文件上传")
@RestController
@RequestMapping("/electronic/case")
public class ImportDataController {

    @Autowired
    ImportDataService importDataService;

    /**
     * 以CSV格式导入数据并存入数据库中
     * @return
     */
    @ApiOperation(value = "文件上传接口", notes = "<font color='green'>描述：</font>用于文件上传")
    @PostMapping("/submit/csv")
    @ResponseBody
    public R submitCSVController(@RequestPart("csvData")@ApiParam(name = "csvData", required = true, value = "上传的文件") MultipartFile csvData,
                                 HttpSession session) throws IOException {

        Logger.info("\n上传文件接口 ---> ");
        String csvPath = importDataService.submitCSVService(csvData, session);
        boolean isSuccessful = importDataService.parseCSVService(csvPath);
        if (isSuccessful) {
            Logger.info(" <--- 上传文件成功");
            Logger.info("csvPath:" + csvPath);
            return R.ok().code(302).message("上传成功！\n");
        } else {
            Logger.info(" <--- 上传文件失败");
            return R.error().message("上传失败！\n");
        }
    }

}
