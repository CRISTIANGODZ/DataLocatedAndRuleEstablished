package com.cqupt.electroniccase.controller;

import com.cqupt.electroniccase.mapper.*;
import com.cqupt.electroniccase.service.ExportDataService;
import com.cqupt.electroniccase.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @auther DyingZhang
 * @Create 2022-10-15 下午 6:37
 * @Discriptioon
 */
@RestController
@RequestMapping("/electronic/case")
public class ExportDataController {

    @Autowired
    ExportDataService exportDataService;

    @GetMapping("/get/csv")
    public R getCSVController(){
        try {
            exportDataService.getCSVService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.ok();
    }


}
