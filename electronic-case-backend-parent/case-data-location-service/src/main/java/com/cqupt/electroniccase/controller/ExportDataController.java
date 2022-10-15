package com.cqupt.electroniccase.controller;

import com.cqupt.electroniccase.service.ExportDataService;
import com.cqupt.electroniccase.service.impl.ExportDataServiceImpl;
import com.cqupt.electroniccase.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther DyingZhang
 * @Create 2022-10-15 下午 5:14
 * @Discriptioon
 */
@RestController
@RequestMapping("/electronic/case")
public class ExportDataController {

    @Autowired
    ExportDataService exportDataService;

    @GetMapping("/get/csv")
    public R getCSVController(){
        exportDataService.getCSVService();
        return R.ok();
    }
}
