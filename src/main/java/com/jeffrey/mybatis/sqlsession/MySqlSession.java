package com.jeffrey.mybatis.sqlsession;

import java.lang.reflect.Proxy;

/**
 * @program: my_mybatis
 * @author: Jeffrey
 * @create: 2024-05-05 15:51
 * @description:
 * 搭建Configuration 和 executor 之间的桥梁
 **/
public class MySqlSession {
    //属性
    private Executor executor = new MyExecutor();
    private MyConfiguration myConfiguration = new MyConfiguration();

    //编写一个方法selectOne  返回一条记录-对象
    //在原生 Mybatis 中 statement 不是sql，而是要执行的接口方法
    //这里做了简化
    public <T>T selectOne(String statement,Object parameter){
        return executor.query(statement,parameter);

    }

    /** 
     * @author Jeffrey
     * @date 20:01 2024/5/5 
     * @param clazz
     * @return T
     * 返回 mapper 的动态代理对象
     * 这里clazz 到时传入的是 MonsterMapper.class
     * 返回的就是这里 MonsterMapper 接口的代理对象
     * 当执行接口方法时（通过代理对象调用），根据动态代理机制，会执行到 MyMapperProxy-invoke 方法
     **/
    public <T>T getMapper(Class<T> clazz){
        //返回动态代理对象
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},
                new MyMapperProxy(myConfiguration,this,clazz));
    }
}
