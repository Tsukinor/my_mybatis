package com.jeffrey;


import com.jeffrey.entity.Monster;
import com.jeffrey.mapper.MonsterMapper;
import com.jeffrey.mybatis.config.MapperBean;
import com.jeffrey.mybatis.sqlsession.*;
import org.junit.Test;

import java.sql.Connection;

/**
 * @program: my_mybatis
 * @author: Jeffrey
 * @create: 2024-05-05 12:53
 * @description:
 **/
public class MybatisTest {

    @Test
    public void build(){
        MyConfiguration myConfiguration = new MyConfiguration();
        Connection build = myConfiguration.build("my_mybatisconfig.xml");
        System.out.println(build);
    }

    @Test
    public void query(){
        Executor myExecutor = new MyExecutor();
        Monster monster = myExecutor.query("select * from monster where id=?",1);
        System.out.println("monster=" + monster);
    }

    @Test
    public void selectOne(){
        MySqlSession mySqlSession = new MySqlSession();
        Monster monster = mySqlSession.selectOne("select * from monster where id=?", 1);
        System.out.println("monster==" + monster);
    }
    @Test
    public void readMapper(){
        MyConfiguration myConfiguration = new MyConfiguration();
        MapperBean mapperBean = myConfiguration.readMapper("MonsterMapper.xml");
        System.out.println(mapperBean);
    }

    @Test
    public void getMapper(){
        MySqlSession mySqlSession = new MySqlSession();
        MonsterMapper mapper = mySqlSession.getMapper(MonsterMapper.class);
        System.out.println("mapper=" + mapper.getClass());
        Monster monster = mapper.getMonsterById(1);
        System.out.println("monster=" + monster);
    }


    @Test
    public void openSession(){
        MySqlSession mySqlSession = MySessionFactory.openSession();
        MonsterMapper mapper = mySqlSession.getMapper(MonsterMapper.class);
        Monster monsterById = mapper.getMonsterById(1);
        System.out.println(monsterById);
    }

}
