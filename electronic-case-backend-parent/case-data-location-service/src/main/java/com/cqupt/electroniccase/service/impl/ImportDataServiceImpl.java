package com.cqupt.electroniccase.service.impl;

import com.cqupt.electroniccase.mapper.*;
import com.cqupt.electroniccase.service.ImportDataService;
import com.cqupt.electroniccase.utils.SeparatorUtil;
import com.cqupt.electroniccase.utils.ReaderCSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pojo.*;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @auther DyingZhang
 * @Create 2022-10-13 下午 10:20
 * @Discriptioon
 */
@Transactional(isolation = Isolation.READ_COMMITTED)
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
        //1.读取CSV的内容进入内存
        StringBuffer stringBuffer = ReaderCSV.printCSVFile(csvPath,' ');
        //1.1解析第一种类，病人信息，病种的字符串
        String firstCategoryName = stringBuffer.substring(0, stringBuffer.indexOf(";")-1);
        stringBuffer.delete(0,stringBuffer.indexOf(";")+1);
        String patientsContext = stringBuffer.substring(0, stringBuffer.indexOf("病种"));
        stringBuffer.delete(0,stringBuffer.indexOf("病种"));
        String diseaseName = stringBuffer.substring(stringBuffer.indexOf("病种"), stringBuffer.indexOf(";"));
        stringBuffer.delete(stringBuffer.indexOf("病种"), stringBuffer.indexOf(";")+1);

        //2.将第一种类，病人信息，病种加入数据库中
        FirstCategory firstCategory = null;
        Patients patients = null;
        Diseases diseases = null;
        Themes themes = null;
        Texts texts = null;
        //2.1 获取第一种类的实体类对象
        if ((firstCategoryName != "") && (firstCategoryName != null)){
            firstCategory = new FirstCategory(firstCategoryName);
        }
        //2.2 获取病人信息的对象
        if ((patientsContext != "") && (patientsContext != null) && patientsContext.contains("姓名")){
            String temporaryStr = patientsContext.substring(patientsContext.indexOf("姓名"), patientsContext.length());
            String name = temporaryStr.substring(temporaryStr.indexOf("姓名")+3, temporaryStr.indexOf(";"));
            patients = new Patients(name);
        }
        //2.3 获取病种的对象
        if ((diseaseName != "") && (diseaseName != null)){
            String diseasesName = diseaseName.substring(diseaseName.indexOf("病种") + 3, diseaseName.length());
            diseases = new Diseases(diseasesName);
        }
        //2.4 将三个对象分别加入数据库中，并处理好之间关系：patients表中要有病种名称和第一种类id
        firstCategoryMapper.addFirstCategoryMapper(firstCategory);
        //查找该病种信息是否存在
        Diseases isDiseaseExist = diseasesMapper.isDiseaseExist(diseases.getDiseaseName());
        if (isDiseaseExist == null){
            diseasesMapper.addDiseases(diseases);
        } else {
            diseases.setDiseaseId(isDiseaseExist.getDiseaseId());
        }
        patients.setDiseaseIdList(diseases.getDiseaseId().toString());
        patients.setFirstCategory(firstCategory.getFirstCategoryId());
        patientsMapper.addPatient(patients);

        //3.将其他字段作为一条texts数据加入数据库中
        while (stringBuffer.length() != 0){
            String singleInfo = stringBuffer.substring(0, stringBuffer.indexOf(";"));
            stringBuffer.delete(0, stringBuffer.indexOf(";") + 1);
            String key = singleInfo.substring(0, singleInfo.indexOf(SeparatorUtil.getSeparator(singleInfo.toString())));
            String value = singleInfo.substring(singleInfo.indexOf(SeparatorUtil.getSeparator(singleInfo.toString()))+1, singleInfo.length());
            //3.1添加theme信息
            themes = new Themes(key);
            themesMapper.addTheme(themes);
            //3.2封装好text信息
            texts = new Texts(patients.getPatientId(),firstCategory.getFirstCategoryId(),diseases.getDiseaseId(),key,value,themes.getThemeId());
            //3.3将text加到数据库中
            textsMapper.addText(texts);
        }


    }

}
