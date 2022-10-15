package com.cqupt.electroniccase.service.impl;

import com.cqupt.electroniccase.mapper.*;
import com.cqupt.electroniccase.service.ExportDataService;
import com.csvreader.CsvWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pojo.Diseases;
import pojo.FirstCategory;
import pojo.Patients;
import pojo.Texts;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

/**
 * @auther DyingZhang
 * @Create 2022-10-15 下午 6:38
 * @Discriptioon
 */
@Transactional(isolation = Isolation.READ_COMMITTED)
@Service
public class ExportDataServiceImpl implements ExportDataService {

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
     * 将数据库中数据查询，封装到CSV文件中
     */
    @Override
    public void getCSVService() throws IOException {
        //创建CsvWriter，初始化csv文件
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + ".csv";
        String prefix = "F:\\project-workspace\\DataLocatedAndRuleEstablished\\electronic-case-backend-parent\\case-data-location-service\\src\\main\\resources\\get-csv\\";
        File file = new File(prefix + fileName);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("创建成功！");
            }
        }
        CsvWriter csvWriter = new CsvWriter(prefix + fileName, ' ', Charset.forName("GBK"));

        //1.查询出所有的Texts信息
        List<Texts> allTexts = textsMapper.getAllTexts();
        System.out.println(allTexts);

        //2.根据一条Text信息的first_category_id,patient_id,disease_id查询前三行信息
        Texts text0 = allTexts.get(0);
        //2.1查询第一种类信息
        FirstCategory firstCategory = firstCategoryMapper.getFirstCategory(text0.getFirstCategoryId());
        System.out.println(firstCategory);
        //2.2查询patient信息
        Patients patient = patientsMapper.getPatient(text0.getPatientId());
        System.out.println(patient);
        //2.3查询disease信息
        Diseases disease = diseasesMapper.getDisease(text0.getDiseaseId());
        System.out.println(disease);
        //2.4将第一种类，patient,disease添加到csv中
        // 添加第一种类
        String[] addFirstCategory = {firstCategory.getFirstCategoryName()+"："};
        csvWriter.writeRecord(addFirstCategory);
        // 添加patient信息
        String[] addPatient = {"姓名" + "：" + patient.getName()};
        csvWriter.writeRecord(addPatient);
        // 添加disease信息
        String[] addDisease = {"病种" + "：" + disease.getDiseaseName()};

        //3.添加其他信息
        for (Texts text : allTexts){
            String key = text.getTitle();
            String value = text.getContent();
            String[] theme = {key + "：" + value};
            csvWriter.writeRecord(theme);
        }

        //关闭csvWriter
        csvWriter.close();

    }
}
