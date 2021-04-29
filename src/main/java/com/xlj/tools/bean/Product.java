package com.xlj.tools.bean;

import lombok.Builder;
import lombok.Data;

/**
 * 商品
 *
 * @author xlj
 * @date 2021/4/29
 */
@Data
@Builder
public class Product {
    private Integer id;
    private String name;
    private Double price;
}
