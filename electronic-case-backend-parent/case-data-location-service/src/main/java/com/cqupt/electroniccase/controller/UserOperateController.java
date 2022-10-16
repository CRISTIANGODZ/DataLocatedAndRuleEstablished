package com.cqupt.electroniccase.controller;

import com.cqupt.electroniccase.service.ExportDataService;
import com.cqupt.electroniccase.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojo.Texts;

import java.util.List;

/**
 * @auther DyingZhang
 * @Create 2022-10-16 下午 12:30
 * @Discriptioon
 */
@RestController
@RequestMapping("/electronic/case")
public class UserOperateController {

    @Autowired
    ExportDataService exportDataService;

    @GetMapping("/get/patient/text")
    public R getPatientTextByName(String name){
        List<Texts> patientAllText = exportDataService.getPatientAllText(name);
        return R.ok().addList(patientAllText);
    }




}
