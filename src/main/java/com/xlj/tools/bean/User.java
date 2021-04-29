package com.xlj.tools.bean;

import lombok.Builder;
import lombok.Data;

/**
 * @author xlj
 * @date 2021/4/29
 */
@Data
@Builder
public class User {
    private Integer id;
    private String name;
}
