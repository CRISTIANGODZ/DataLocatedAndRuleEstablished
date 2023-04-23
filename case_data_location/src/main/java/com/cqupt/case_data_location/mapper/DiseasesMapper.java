package com.cqupt.case_data_location.mapper;

import com.cqupt.case_data_location.pojo.entity.Diseases;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 8:51
 * @Discriptioon
 */
@Mapper
public interface DiseasesMapper {

    /**
     * 添加病种信息，并返回自增主键ID
     * @param diseases
     */
    void addDiseases(Diseases diseases);

    /**
     * 通过diseaseId查询diseaseName
     * @param diseaseId
     * @return
     */
    Diseases getDisease(@Param("diseaseId") Long diseaseId);

    /**
     * 判断病种是否存在
     * @param diseaseName
     */
    Diseases isDiseaseExist(@Param("diseaseName") String diseaseName);
}
