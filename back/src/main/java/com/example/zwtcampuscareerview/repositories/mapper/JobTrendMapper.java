package com.example.zwtcampuscareerview.repositories.mapper;

import com.example.zwtcampuscareerview.models.JobTrendData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JobTrendMapper {
    List<JobTrendData> getJobTrendDataByYear(int year); // 查询某年的数据
    void insertJobTrendData(JobTrendData jobTrendData); // 插入一条数据
}

