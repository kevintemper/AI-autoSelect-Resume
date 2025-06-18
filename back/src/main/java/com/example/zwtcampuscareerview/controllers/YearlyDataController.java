package com.example.zwtcampuscareerview.controllers;

import com.example.zwtcampuscareerview.models.YearlyData;
import com.example.zwtcampuscareerview.repositories.mapper.YearlyDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/data")
public class YearlyDataController {

    @Autowired
    private YearlyDataMapper yearlyDataMapper;

    @GetMapping
    public ResponseEntity<List<Integer>> getYearlyData(@RequestParam int year) {
        // Check if data exists for the year
        List<YearlyData> data = yearlyDataMapper.getYearlyDataByYear(year);

        if (data.isEmpty()) {
            // Generate random data if not found
            Random random = new Random();
            int value1 = random.nextInt(100) + 200;
            int value2 = random.nextInt(100) + 100;

            // Save to database
            YearlyData newData = new YearlyData();
            newData.setYear(year);
            newData.setValue1(value1);
            newData.setValue2(value2);
            yearlyDataMapper.insertYearlyData(newData);

            return ResponseEntity.ok(List.of(value1, value2));
        } else {
            // Return existing data
            YearlyData existingData = data.get(0);
            return ResponseEntity.ok(List.of(existingData.getValue1(), existingData.getValue2()));
        }
    }
}

