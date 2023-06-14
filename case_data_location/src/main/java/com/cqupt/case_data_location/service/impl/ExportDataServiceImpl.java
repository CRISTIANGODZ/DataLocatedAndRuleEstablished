package com.cqupt.case_data_location.service.impl;


import com.cqupt.case_data_location.mapper.*;
import com.cqupt.case_data_location.pojo.entity.Diseases;
import com.cqupt.case_data_location.pojo.entity.FirstCategory;
import com.cqupt.case_data_location.pojo.entity.Patients;
import com.cqupt.case_data_location.pojo.entity.Texts;
import com.cqupt.case_data_location.service.ExportDataService;
import com.cqupt.case_data_location.utils.ESUtils;
import com.cqupt.case_data_location.utils.Logger;
import com.csvreader.CsvWriter;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    ESUtils esUtils;

    /**
     * 获取用户相关的所有text信息
     * @param name
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public List<Texts> getPatientAllText(String name) {//不能判断重名;修改不同步
        Long patientId = -1l;
        SearchHits hits = esUtils.getDocByMustColumn("texts", "name", name);//名称含有传入的name，也可以被搜到：可以加映射，不使分词;
        for (SearchHit hit : hits) {
            patientId = Long.parseLong(hit.getId());
        }
        List<Texts> list = textsMapper.getPatientTexts(patientId);
        Logger.info("成功获取text信息");
        return list;
    }

    /**
     * 将数据库中数据查询，封装到CSV文件中
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
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
                file.createNewFile();
            } catch (IOException e) {
                Logger.error("初始化CSV文件失败！");
                e.printStackTrace();
            } finally {
                Logger.info("初始化CSV文件成功！");
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
            Logger.error("封装数据异常！");
            throw new RuntimeException(e);
        } finally {
            //关闭csvWriter
            csvWriter.close();
            Logger.info("封装数据成功！");
        }
        return fileName;
    }

}
