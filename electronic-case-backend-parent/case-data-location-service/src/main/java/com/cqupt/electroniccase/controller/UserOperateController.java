package com.cqupt.electroniccase.controller;

import com.cqupt.electroniccase.service.ExportDataService;
import com.cqupt.electroniccase.service.UserOperateService;
import com.cqupt.electroniccase.utils.Logger;
import com.cqupt.electroniccase.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.cqupt.electroniccase.pojo.Texts;

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
        Logger.info("查询病人信息接口 ---> ");
        List<Texts> patientAllText = exportDataService.getPatientAllText(name);
        if (patientAllText != null){
            Logger.info(" <--- 查询病人信息接口\n");
            return R.ok().addList(patientAllText).message("返回成功！");
        } else {
            return R.error().message("返回失败！");
        }
    }

    /**
     * 删除病人某条text数据信息
     * @param texts
     * @return
     */
    @PostMapping("/patient/text/delete")
    public R deletePatientTextByTextIdController(Texts texts){
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
    @PostMapping("/patient/text/update")
    public R updatePatientTextByTextIdController(Texts texts){
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
    @PostMapping("/delete/patient/all/text")
    public R deletePatientAllTextController(Texts texts){
        Logger.info("删除信息接口 --> ");
        userOperateService.deletePatientAllText(texts);
        Logger.info(" <-- 删除信息接口\n");
        return R.ok().message("该病人信息以全部删除");
    }

    /**
     * 根据textId查询text记录
     */
    @GetMapping("/patient/get/single/text")
    public R getSingleText(Texts texts){
        Logger.info("根据textId查询text记录接口 --> ");
        Texts text = userOperateService.getSingleText(texts);
        if (text != null){
            Logger.info("查询成功！");
            Logger.info(" <-- 根据textId查询text记录接口\n");
            return R.ok().addTexts(text).message("返回成功！");
        } else {
            Logger.error("查询失败！");
            Logger.info(" <-- 根据textId查询text记录接口\n");
            return R.error().message("数据不存在，返回失败！");
        }
    }


}
