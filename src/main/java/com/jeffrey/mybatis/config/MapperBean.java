package com.jeffrey.mybatis.config;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @program: my_mybatis
 * @author: Jeffrey
 * @create: 2024-05-05 16:10
 * @description:
 * 将Mapper信息进行封装
 **/
@Setter
@Getter
@ToString
public class MapperBean {

    private String interfaceName;//接口名
    //接口下的所有方法
    List<Function> functions;
}
