package com.cqupt.electroniccase.service.impl;

import com.cqupt.electroniccase.bean.ESUtils;
import com.cqupt.electroniccase.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
