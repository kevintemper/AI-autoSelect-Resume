package com.example.zwtcampuscareerview.repositories.mapper;

import com.example.zwtcampuscareerview.models.YearlyData;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface YearlyDataMapper {

    @Insert("INSERT INTO yearly_data (year, value1, value2) VALUES (#{year}, #{value1}, #{value2})")
    void insertYearlyData(YearlyData yearlyData);

    @Select("SELECT * FROM yearly_data WHERE year = #{year}")
    List<YearlyData> getYearlyDataByYear(@Param("year") int year);

    @Select("SELECT * FROM yearly_data")
    List<YearlyData> getAllYearlyData();
}
