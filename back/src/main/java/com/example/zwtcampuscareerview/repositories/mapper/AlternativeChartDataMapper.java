package com.example.zwtcampuscareerview.repositories.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AlternativeChartDataMapper {
    @Select("SELECT data_values FROM alternative_chart_data WHERE category = #{category}")
    String getDataByCategory(String category);
}

