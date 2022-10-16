package com.cqupt.electroniccase.controller;

import com.cqupt.electroniccase.mapper.*;
import com.cqupt.electroniccase.service.ExportDataService;
import com.cqupt.electroniccase.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojo.Texts;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/get/csv/{name}")
    public R getCSVController(@PathVariable("name") String name){
        List<Texts> patientTextList = exportDataService.getPatientAllText(name);
        exportDataService.getCSVService(patientTextList);
        return R.ok();
    }


}
