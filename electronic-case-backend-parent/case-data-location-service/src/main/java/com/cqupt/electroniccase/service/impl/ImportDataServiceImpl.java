package com.cqupt.electroniccase.service.impl;

import com.cqupt.electroniccase.mapper.*;
import com.cqupt.electroniccase.service.ImportDataService;
import com.cqupt.electroniccase.utils.ReaderCSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pojo.Diseases;
import pojo.FirstCategory;
import pojo.Patients;

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
    FirstCategoryMapper firstCategoryMapper;
    @Autowired
    PatientsMapper patientsMapper;
    @Autowired
    DiseasesMapper diseasesMapper;
    @Autowired
    TextsMapper textsMapper;
    @Autowired
    ThemesMapper themesMapper;



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
        //读取CSV的内容进入内存
        StringBuffer stringBuffer = ReaderCSV.printCSVFile(csvPath,' ');
        System.out.println(stringBuffer);
        //解析字符串
        String firstCategoryName = stringBuffer.substring(0, stringBuffer.indexOf(";")-1);
        stringBuffer.delete(0,stringBuffer.indexOf(";")+1);
        String patientsContext = stringBuffer.substring(0, stringBuffer.indexOf("病种"));
        stringBuffer.delete(0,stringBuffer.indexOf("病种"));
        String diseaseName = stringBuffer.substring(stringBuffer.indexOf("病种"), stringBuffer.indexOf(";"));
        stringBuffer.delete(stringBuffer.indexOf("病种"), stringBuffer.indexOf(";")+1);
        //1.将第一种类，病人信息，病种加入实体类对象中
        FirstCategory firstCategory = null;
        Patients patients = null;
        Diseases diseases = null;
        //获取第一种类的实体类对象
        if ((firstCategoryName != "") && (firstCategoryName != null)){
            firstCategory = new FirstCategory(firstCategoryName);
        }
        //获取病人信息的对象
        if ((patientsContext != "") && (patientsContext != null) && patientsContext.contains("姓名")){
            String temporaryStr = patientsContext.substring(patientsContext.indexOf("姓名"), patientsContext.length());
            String name = temporaryStr.substring(temporaryStr.indexOf("姓名")+3, temporaryStr.indexOf(";"));
            patients = new Patients(name);
        }
        //获取病种的对象
        if ((diseaseName != "") && (diseaseName != null)){
            String diseasesName = diseaseName.substring(diseaseName.indexOf("病种") + 3, diseaseName.length());
            diseases = new Diseases(diseasesName);
        }





    }
}
