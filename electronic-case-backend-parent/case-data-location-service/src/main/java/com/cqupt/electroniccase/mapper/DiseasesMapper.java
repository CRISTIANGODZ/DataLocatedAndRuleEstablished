package com.cqupt.electroniccase.mapper;

import org.apache.ibatis.annotations.Mapper;
import pojo.Diseases;

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
}
