package com.h2h.redis_stand_alone.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;

/**
 * 随便找了个测试类
 * @author 小波
 */
//返回值忽略null的注解
@Data
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class PrintParam implements Serializable{

    private Long mchId;
    private Long userId;
    private Integer type;
    private String start;
    private String end;
}
