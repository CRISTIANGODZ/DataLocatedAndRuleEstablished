package com.cqupt.electroniccase.service;

import com.cqupt.electroniccase.domain.entity.Diseases;

/**
 * @auther DyingZhang
 * @Create 2023-03-29 下午 10:21
 * @Discriptioon
 */
public interface TestService {
    void addIndex(String name);

    void insertDocService(Diseases diseases);

    void getDocByTerm(Diseases diseases);
}
