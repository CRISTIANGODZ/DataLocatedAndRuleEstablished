package com.cqupt.electroniccase.mapper;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pojo.Texts;

import java.util.List;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 8:53
 * @Discriptioon
 */
@Mapper
public interface TextsMapper {

    /**
     * 添加一条text数据到数据库中，并返回主键id
     * @param texts
     */
    void addText(Texts texts);

    /**
     * 获取所有的texts数据，将其封装到List<Texts中>
     * @return
     */
    List<Texts> getAllTexts();

    /**
     * 根据用户id获取所有texts数据，将其封装到List<Texts>中
     * @param patientId
     * @return
     */
    List<Texts> getPatientTexts(@Param("patientId") Long patientId);

    void getThemeIdByTextId(Long textId);

    /**
     * 删除texts字段
     * @param texts
     */
    void deleteText(Texts texts);
}
