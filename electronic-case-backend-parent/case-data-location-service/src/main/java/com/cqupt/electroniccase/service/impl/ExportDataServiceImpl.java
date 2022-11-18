package com.cqupt.electroniccase.service.impl;

import com.cqupt.electroniccase.mapper.*;
import com.cqupt.electroniccase.service.ExportDataService;
import com.csvreader.CsvWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.cqupt.electroniccase.pojo.Diseases;
import com.cqupt.electroniccase.pojo.FirstCategory;
import com.cqupt.electroniccase.pojo.Patients;
import com.cqupt.electroniccase.pojo.Texts;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
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
     * 获取用户相关的所有text信息
     * @param name
     * @return
     */
    @Override
    public List<Texts> getPatientAllText(String name) {
        Patients patient = patientsMapper.getPatientIdByName(name);
        Long patientId = patient.getPatientId();
        List<Texts> list = textsMapper.getPatientTexts(patientId);
        return list;
    }

    /**
     * 将数据库中数据查询，封装到CSV文件中
     */
    @Override
    public String getCSVService(List<Texts> allTexts, HttpSession session){
        //创建CsvWriter，初始化csv文件
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + ".csv";
        //获取ServletContext对象
        ServletContext servletContext = session.getServletContext();
        //获取服务器中文件的真实路径
        String realPath = servletContext.getRealPath("/submit-csv");
        fileName = realPath + fileName;
        File file = new File(fileName);
        if (!file.exists()){
            try {
                file.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("创建成功！");
            }
        }
        CsvWriter csvWriter = new CsvWriter(fileName, ' ', Charset.forName("GBK"));

        //2.根据一条Text信息的first_category_id,patient_id,disease_id查询前三行信息
        Texts text0 = allTexts.get(0);
        //2.1查询第一种类信息
        FirstCategory firstCategory = firstCategoryMapper.getFirstCategory(text0.getFirstCategoryId());
        //2.2查询patient信息
        Patients patient = patientsMapper.getPatient(text0.getPatientId());
        //2.3查询disease信息
        Diseases disease = diseasesMapper.getDisease(patient.getDiseaseId());
        //2.4将第一种类，patient,disease添加到csv中
        try {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //关闭csvWriter
            csvWriter.close();
        }
        return fileName;
    }

}
