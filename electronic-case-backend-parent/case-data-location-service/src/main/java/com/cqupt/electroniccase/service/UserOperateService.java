package com.cqupt.electroniccase.service;

import pojo.Texts;

/**
 * @auther DyingZhang
 * @Create 2022-10-16 下午 12:36
 * @Discriptioon
 */
public interface UserOperateService {

    /**
     * 删除病人某条text数据信息
     * @param texts
     */
    void deleteText(Texts texts);

    /**
     * 修改病人某条text数据信息
     * @param texts
     */
    void updateText(Texts texts);

    /**
     * 删除某个病人的所有相关信息
     * @param texts
     */
    void deletePatientAllText(Texts texts);
}
