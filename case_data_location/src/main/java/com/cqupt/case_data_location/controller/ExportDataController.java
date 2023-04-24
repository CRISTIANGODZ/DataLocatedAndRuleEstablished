package com.cqupt.case_data_location.controller;

import com.cqupt.case_data_location.pojo.entity.Texts;
import com.cqupt.case_data_location.service.ExportDataService;
import com.cqupt.case_data_location.utils.Logger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * @auther DyingZhang
 * @Create 2022-10-15 下午 6:37
 * @Discriptioon
 */
@Api(tags = "文件导出")
@RestController
@RequestMapping("/electronic/case")
public class ExportDataController {

    @Autowired
    ExportDataService exportDataService;

    @ApiOperation(value = "导出文件接口", notes = "<font color='green'>描述：</font>用于文件的导出")
    @GetMapping("/get/csv")
    @ResponseBody
    public ResponseEntity<byte[]> getCSVController(HttpSession session,
                                                   @ApiParam(name = "name", required = true, value = "患者姓名") String name){
        Logger.info("导出文件接口 ---> ");
        ResponseEntity<byte[]> responseEntity = null;
        try {
            List<Texts> patientTextList = exportDataService.getPatientAllText(name);
            String fileRealPath = exportDataService.getCSVService(patientTextList, session);
            Logger.info("return-csv-path:" + fileRealPath);
            //获取ServletContext对象
            ServletContext servletContext = session.getServletContext();
            //创建输入流
            InputStream is = new FileInputStream(fileRealPath);
            //创建字节数组,is.available()获取输入流所对应文件的字节数
            byte[] bytes = new byte[is.available()];
            //将流读到字节数组中
            is.read(bytes);
            //创建HttpHeaders对象设置响应头信息
            MultiValueMap<String, String> headers = new HttpHeaders();
            //设置要下载方式以及下载文件的名字
            String fileName = UUID.randomUUID().toString() + ".csv";
            headers.add("Content-Disposition", "attachment;filename=" + fileName);
            //设置响应状态码
            HttpStatus statusCode = HttpStatus.OK;
            //创建ResponseEntity对象
            responseEntity = new ResponseEntity<>(bytes, headers,
                    statusCode);
            //关闭输入流
            is.close();
        } catch (IOException e) {
            Logger.error("导出文件发生IO异常！");
            throw new RuntimeException(e);
        } finally {
            Logger.info(" <--- 导出文件接口\n");
            return responseEntity;
        }
    }


}
