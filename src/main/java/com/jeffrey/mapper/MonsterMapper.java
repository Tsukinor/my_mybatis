package com.jeffrey.mapper;

import com.jeffrey.entity.Monster;

/**
 * @program: my_mybatis
 * @author: Jeffrey
 * @create: 2024-05-05 15:59
 * @description:
 **/
public interface MonsterMapper {

    //查询
    Monster getMonsterById(Integer id);
}
