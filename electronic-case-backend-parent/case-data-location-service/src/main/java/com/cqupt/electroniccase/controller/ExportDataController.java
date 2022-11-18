package com.cqupt.electroniccase.controller;

import com.cqupt.electroniccase.service.ExportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cqupt.electroniccase.pojo.Texts;

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
@RestController
@RequestMapping("/electronic/case")
public class ExportDataController {

    @Autowired
    ExportDataService exportDataService;

    @GetMapping("/get/csv")
    public ResponseEntity<byte[]> getCSVController(HttpSession session,String name)throws IOException{
        List<Texts> patientTextList = exportDataService.getPatientAllText(name);
        String fileRealPath = exportDataService.getCSVService(patientTextList, session);

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
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers,
                statusCode);
        //关闭输入流
        is.close();


        return responseEntity;
    }


}
