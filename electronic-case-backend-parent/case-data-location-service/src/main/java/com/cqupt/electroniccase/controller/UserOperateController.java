package com.cqupt.electroniccase.controller;

import com.cqupt.electroniccase.service.ExportDataService;
import com.cqupt.electroniccase.service.UserOperateService;
import com.cqupt.electroniccase.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    UserOperateService userOperateService;

    /**
     * 根据病人的姓名查询病人信息，的所有text信息返回给前端
     * @param name
     * @return
     */
    @GetMapping("/patient/text/get")
    public R getPatientTextByNameController(String name){
        List<Texts> patientAllText = exportDataService.getPatientAllText(name);
        return R.ok().addList(patientAllText);
    }

    /**
     * 删除病人某条text数据信息
     * @param texts
     * @return
     */
    @PostMapping("/patient/text/delete")
    public R deletePatientTextByTextIdController(Texts texts){
        userOperateService.deleteText(texts);
        return R.ok().message("删除成功");
    }




}
