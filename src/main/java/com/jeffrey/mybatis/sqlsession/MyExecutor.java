package com.jeffrey.mybatis.sqlsession;

import com.jeffrey.entity.Monster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @program: my_mybatis
 * @author: Jeffrey
 * @create: 2024-05-05 14:52
 * @description:
 **/
public class MyExecutor implements Executor{
    //属性
    private MyConfiguration myConfiguration =
            new MyConfiguration();
    
    /** 
     * @author Jeffrey
     * @date 14:56 2024/5/5
     * @param sql
     * @param parameter 
     * @return T
     **/
    @Override
    public <T> T query(String sql, Object parameter) {
        //得到连接Connection
        Connection connection = getConnection();
        //返回查询的结果集
        ResultSet set = null;
        PreparedStatement pre = null;

        try {
            pre = connection.prepareStatement(sql);
            pre.setString(1,parameter.toString());
            set = pre.executeQuery();
            //把set数据封装到对象-monster
            //这里进行简化处理
            //完善的写法是通过一套反射机制
            Monster monster = new Monster();
            //遍历结果集，把数据封装到monster对象
            while (set.next()){
                monster.setId(set.getInt("id"));
                monster.setName(set.getString("name"));
                monster.setEmail(set.getString("email"));
                monster.setGender(set.getInt("gender"));
                monster.setAge(set.getInt("age"));
                monster.setBirthday(set.getDate("birthday"));
                monster.setSalary(set.getDouble("salary"));
            }
            return (T)monster;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (set != null){
                    set.close();
                }
                if (pre != null){
                    pre.close();
                }
                if (connection != null){
                    connection.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    //编写方法，通过 myConfiguration 对象返回连接
    private Connection getConnection(){
        Connection build = myConfiguration.build("my_mybatisconfig.xml");
        return build;
    }
}
