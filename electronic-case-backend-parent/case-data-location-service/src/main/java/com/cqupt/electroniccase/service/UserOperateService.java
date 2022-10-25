package com.cqupt.electroniccase.service;

import com.cqupt.electroniccase.pojo.Texts;

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

    /**
     * 判断数据是否存在
     * @param texts
     * @return
     */
    boolean isTextExists(Texts texts);

    /**
     * 根据templateId获取text
     * @param texts
     * @return
     */
    Texts getSingleText(Texts texts);
}
