package com.cqupt.case_data_location.controller;


import com.cqupt.case_data_location.pojo.entity.Texts;
import com.cqupt.case_data_location.service.ExportDataService;
import com.cqupt.case_data_location.service.UserOperateService;
import com.cqupt.case_data_location.utils.Logger;
import com.cqupt.case_data_location.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auther DyingZhang
 * @Create 2022-10-16 下午 12:30
 * @Discriptioon
 */
@Api(tags = "病历操作")
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
    @ApiOperation(value = "病历查询", notes = "<font color='green'>描述：</font>根据用户姓名查询病人信息", response = Texts.class)
    @GetMapping("/patient/text/get")
    @ResponseBody
    public R getPatientTextByNameController(@ApiParam(name = "name", required = true, value = "患者名字") String name){
        Logger.info("查询病人信息接口 ---> ");
        List<Texts> patientAllText = exportDataService.getPatientAllText(name);
        if (patientAllText != null){
            Logger.info(" <--- 查询病人信息接口\n");
            return R.ok().data("patientAllText", patientAllText).message("返回成功！");
        } else {
            return R.error().message("返回失败！");
        }
    }

    /**
     * 删除病人某条text数据信息
     * @param texts
     * @return
     */
    @ApiOperation(value = "删除病例信息", notes = "<font color='green'>描述：</font>删除病人的某条病历信息")
    @PostMapping("/patient/text/delete")
    @ResponseBody
    public R deletePatientTextByTextIdController(@RequestBody Texts texts){
        Logger.info("删除病人信息接口 --->");
        boolean isTextExists = userOperateService.isTextExists(texts);
        if (isTextExists) {
            userOperateService.deleteText(texts);
            Logger.info(" <--- 删除病人信息接口\n");
            return R.ok().message("删除成功！");
        } else {
            Logger.error(" <--- 删除病人信息失败\n");
            return R.error().message("该信息不存在，删除失败！");
        }
    }

    /**
     * 修改病人某条text数据信息
     */
    @ApiOperation(value = "修改数据", notes = "<font color='green'>描述：</font>修改病人某条数据信息")
    @PostMapping("/patient/text/update")
    @ResponseBody
    public R updatePatientTextByTextIdController(@RequestBody Texts texts){
        Logger.info("修改信息接口 --> ");
        if (userOperateService.isTextExists(texts)) {
            userOperateService.updateText(texts);
            Logger.info("修改信息成功");
            Logger.info(" <-- 修改信息接口\n");
            return R.ok().message("修改成功");
        } else {
            Logger.error("修改信息失败!");
            Logger.info(" <-- 修改信息接口\n");
            return R.error().message("未查询到该条信息，修改失败！");
        }
    }

    /**
     * 删除某个病人的所有相关信息
     */
    @ApiOperation(value = "删除病人信息", notes = "<font color='green'>描述：</font>根据病人id删除病人所有信息")
    @PostMapping("/delete/patient/all/text")
    @ResponseBody
    public R deletePatientAllTextController(@RequestBody Texts texts){
        Logger.info("删除信息接口 --> ");
        userOperateService.deletePatientAllText(texts);
        Logger.info(" <-- 删除信息接口\n");
        return R.ok().message("该病人信息已全部删除");
    }

    /**
     * 根据textId查询text记录
     */
    @ApiOperation(value = "查询text记录", notes = "<font color='green'>描述：</font>根据id查询某条具体记录", response = Texts.class)
    @GetMapping("/patient/get/single/text")
    @ResponseBody
    public R getSingleText(@RequestBody Texts texts){
        Logger.info("根据textId查询text记录接口 --> ");
        Texts text = userOperateService.getSingleText(texts);
        if (text != null){
            Logger.info("查询成功！");
            Logger.info(" <-- 根据textId查询text记录接口\n");
            return R.ok().data("text", text).message("返回成功！");
        } else {
            Logger.error("查询失败！");
            Logger.info(" <-- 根据textId查询text记录接口\n");
            return R.error().message("数据不存在，返回失败！");
        }
    }

}
