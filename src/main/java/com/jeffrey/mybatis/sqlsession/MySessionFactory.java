package com.jeffrey.mybatis.sqlsession;

/**
 * @program: my_mybatis
 * @author: Jeffrey
 * @create: 2024-05-05 20:15
 * @description:
 **/
public class MySessionFactory {
    public static MySqlSession openSession(){
        return new MySqlSession();
    }
}
