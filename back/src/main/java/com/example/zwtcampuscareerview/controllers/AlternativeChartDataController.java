package com.example.zwtcampuscareerview.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*") // 跨域配置，允许所有来源
@RestController
@RequestMapping("/api/data2")
public class AlternativeChartDataController {

    /**
     * 获取替代图表数据
     *
     * @param category 类别
     * @return 对应类别的模拟数据
     */
    @GetMapping("/GetAlternativeChartData")
    public ResponseEntity<Map<String, List<Integer>>> getAlternativeChartData(@RequestParam String category) {
        Map<String, List<Integer>> response = new HashMap<>();

        // 模拟数据
        switch (category) {
            case "CategoryA":
                response.put("data", List.of(50, 75, 100));
                break;
            case "CategoryB":
                response.put("data", List.of(150, 200, 250));
                break;
            case "CategoryC":
                response.put("data", List.of(300, 350, 400));
                break;
            default:
                response.put("data", List.of(0, 0, 0)); // 默认数据
                break;
        }

        return ResponseEntity.ok(response);
    }
}
