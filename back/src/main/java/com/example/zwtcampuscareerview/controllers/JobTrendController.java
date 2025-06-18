package com.example.zwtcampuscareerview.controllers;

import com.example.zwtcampuscareerview.DTO.ResumeBatchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class JobTrendController {

    @GetMapping("/jobtrendchart")
    public List<Double> getJobTrendData(@RequestParam int year) {
        // 无论请求什么年份，始终返回随机生成的数据
        return generateRandomJobTrendData();
    }

    // 生成随机合理范围的数据
    private List<Double> generateRandomJobTrendData() {
        Random random = new Random();
        List<Double> randomData = new ArrayList<>();
        for (int i = 0; i < 13; i++) { // 生成13个数据点
            double value = 1 + (100 - 1) * random.nextDouble(); // 范围为1到100
            randomData.add(Math.round(value * 10.0) / 10.0); // 保留一位小数
        }
        return randomData;
    }
    @PostMapping("/rank-resumes")
    public ResponseEntity<?> rankResumes(@RequestBody ResumeBatchRequest request) {
        try {
            String json = new ObjectMapper().writeValueAsString(List.of(request.getResumes(), request.getTopk()));

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:7860/run/predict"))  // 根据你 Gradio 模型 API 修改路径
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"data\":" + json + "}"))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return ResponseEntity.ok(response.body());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
