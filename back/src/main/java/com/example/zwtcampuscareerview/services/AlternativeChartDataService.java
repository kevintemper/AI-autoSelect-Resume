package com.example.zwtcampuscareerview.services;

import com.example.zwtcampuscareerview.repositories.mapper.AlternativeChartDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlternativeChartDataService {

    @Autowired
    private AlternativeChartDataMapper dataMapper;

    public List<Integer> getChartData(String category) {
        String dataString = dataMapper.getDataByCategory(category);
        if (dataString == null) {
            return List.of(0, 0, 0); // 默认值
        }
        return Arrays.stream(dataString.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}

