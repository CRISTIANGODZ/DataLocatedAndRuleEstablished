package com.cqupt.case_data_location.service.impl;


import com.cqupt.case_data_location.mapper.*;
import com.cqupt.case_data_location.pojo.entity.*;
import com.cqupt.case_data_location.service.ImportDataService;
import com.cqupt.case_data_location.utils.ESUtils;
import com.cqupt.case_data_location.utils.Logger;

import com.cqupt.case_data_location.utils.ReaderCSV;
import com.cqupt.case_data_location.utils.SeparatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    ESUtils esUtils;


    /**
     * 以CSV格式导入数据
     * @param csvData
     */
    @Override
    public String submitCSVService(MultipartFile csvData, HttpSession session) {
        //获取上传的文件的文件名
        String filename = csvData.getOriginalFilename();
        //获取上传的文件的后缀名
        String hzName = filename.substring(filename.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        //拼接一个新的文件名
        filename = uuid + hzName;
        //获取当前工程的真实路径
//需手动设置!!!
        ServletContext servletContext = session.getServletContext();
        String photoPath = servletContext.getRealPath("/get-csv");
        File file = new File(photoPath);
        if(!file.exists()){
            file.mkdirs();
        }
        String finalPath = photoPath + File.separator + filename;
        //上传文件
        try {
            csvData.transferTo(new File(finalPath));
        } catch (IOException e) {
            Logger.error("上传文件失败！");
            throw new RuntimeException(e);
        } finally {
            Logger.info("成功接收文件！");
        }
        return finalPath;
    }

    /**
     * 解析CSV中的数据，并将其存入数据库中
     * @param csvPath
     */
    @Override
    public boolean parseCSVService(String csvPath) {
        Logger.info("开始解析CSV文件");
        try {
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
                String patientNum = patientsContext.substring(patientsContext.indexOf("编号")+3, patientsContext.indexOf(";"));
                String other  = patientsContext;
                String temporaryStr = patientsContext.substring(patientsContext.indexOf("姓名"), patientsContext.length());
                String name = temporaryStr.substring(temporaryStr.indexOf("姓名")+3, temporaryStr.indexOf(";"));
                patients = new Patients(name,other,patientNum);
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
            patients.setDiseaseId(diseases.getDiseaseId());
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
                texts = new Texts(patients.getPatientId(),firstCategory.getFirstCategoryId(),key,value,themes.getThemeId());
                //3.3将text加到数据库中
                textsMapper.addText(texts);
            }

            //将字段加入es
            esUtils.insertDoc("texts", String.valueOf(patients.getPatientId()), patients);
            return true;
        } catch (Exception e) {
            Logger.error("解析文件失败");
            return false;
        }
    }

}