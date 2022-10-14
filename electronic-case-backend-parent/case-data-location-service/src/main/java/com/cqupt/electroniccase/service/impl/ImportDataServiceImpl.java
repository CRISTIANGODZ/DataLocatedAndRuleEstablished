package com.cqupt.electroniccase.service.impl;

import com.cqupt.electroniccase.mapper.ImportDataMapper;
import com.cqupt.electroniccase.service.ImportDataService;
import com.cqupt.electroniccase.utils.ReaderCSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @auther DyingZhang
 * @Create 2022-10-13 下午 10:20
 * @Discriptioon
 */
@Service
public class ImportDataServiceImpl implements ImportDataService {

    @Autowired
    ImportDataMapper importDataMapper;

    /**
     * 以CSV格式导入数据
     * @param csvData
     */
    @Override
    public String  submitCSVService(MultipartFile csvData) {
        //获取上传的文件的文件名
        String filename = csvData.getOriginalFilename();
        //获取上传的文件的后缀名
        String hzName = filename.substring(filename.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        //拼接一个新的文件名
        filename = uuid + hzName;
        //获取当前工程的真实路径

//需手动设置!!!
        String csvDataPath = "F:\\project-workspace\\DataLocatedAndRuleEstablished\\electronic-case-backend-parent\\case-data-location-service\\src\\main\\resources\\submit-csv";
        //创建photoPath所对应的File对象
        File file = new File(csvDataPath);
        if (!file.exists()){
            file.mkdir();
        }
        String finalPath = csvDataPath + File.separator + filename;
        //上传文件
        try {
            csvData.transferTo(new File(finalPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return finalPath;
    }

    /**
     * 解析CSV中的数据，并将其存入数据库中
     * @param csvPath
     */
    @Override
    public void parseCSVService(String csvPath) {
        StringBuffer stringBuffer = ReaderCSV.printCSVFile(csvPath,' ');
        System.out.println(stringBuffer);
    }
}
