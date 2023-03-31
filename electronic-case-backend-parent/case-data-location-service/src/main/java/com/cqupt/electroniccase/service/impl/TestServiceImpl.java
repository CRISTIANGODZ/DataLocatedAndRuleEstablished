package com.cqupt.electroniccase.service.impl;

import com.cqupt.electroniccase.bean.ESUtils;
import com.cqupt.electroniccase.domain.entity.Diseases;
import com.cqupt.electroniccase.service.TestService;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @auther DyingZhang
 * @Create 2023-03-29 下午 10:21
 * @Discriptioon
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    ESUtils esUtils;


    @Override
    public void addIndex(String name) {
        esUtils.createIndex(name);
    }

    @Override
    public void insertDocService(Diseases diseases) {
        try {
            esUtils.insertDoc("diseases", String.valueOf(diseases.getDiseaseId()), diseases);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getDocByTerm(Diseases diseases) {
        SearchHits docByTerm = esUtils.getDocByTerm("diseases", "diseaseName", diseases.getDiseaseName());
        for (SearchHit hit : docByTerm) {
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
        }
    }
}
