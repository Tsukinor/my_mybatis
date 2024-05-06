package com.jeffrey.mybatis.sqlsession;

/**
 * @program: my_mybatis
 * @author: Jeffrey
 * @create: 2024-05-05 14:50
 * @description:
 **/
public interface Executor {
    public <T>T query(String statement,Object parameter);
}
