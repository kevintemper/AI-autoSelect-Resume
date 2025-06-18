package com.example.zwtcampuscareerview.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 年度数据实体类
 */
@Data // 自动生成 Getter、Setter、toString、equals、hashCode 等
@NoArgsConstructor // 生成无参构造函数
@AllArgsConstructor // 生成全参构造函数
public class YearlyData {
    private Integer id;    // 自增主键
    private Integer year;  // 年份
    private Integer value1; // 数据值1
    private Integer value2; // 数据值2
}

