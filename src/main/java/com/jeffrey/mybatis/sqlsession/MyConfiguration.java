package com.jeffrey.mybatis.sqlsession;

import com.jeffrey.mybatis.config.Function;
import com.jeffrey.mybatis.config.MapperBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @program: my_mybatis
 * @author: Jeffrey
 * @create: 2024-05-05 12:43
 * @description:
 *
 * 用于读取xml文件， 建立连接
 **/
public class MyConfiguration {
    //属性 - 类的加载器
    private static ClassLoader loader =
            ClassLoader.getSystemClassLoader();

    //读取xml文件信息，并处理
    public Connection build(String resource){

        Connection connection = null;
        try {
            //加载配置 mybatisConfig.xml ,获取对应的InputStream
            InputStream resourceAsStream =
                    loader.getResourceAsStream(resource);
            //解析xml
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            //解析root ，返回 Connection =》单独写一个方法
            System.out.println("rootElement = " + rootElement);
            connection = evalDataSource(rootElement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return connection;
    }


    //会解析mybatisConfig.xml 信息，并返回Connection
    private Connection evalDataSource(Element root){
        if (!"database".equals(root.getName())){
            throw new RuntimeException("root节点应该是<database>");
        }
        String driverClassName = null;
        String url = null;
        String userName = null;
        String password = null;

        //遍历root 子节点，获取属性值
        for (Object item: root.elements("property")){
            Element i = (Element)item;//对应的就是property 节点
            String name = i.attributeValue("name");
            String value = i.attributeValue("value");
            //判断是否得到 name 和 value
            if (name == null || value == null){
                throw new RuntimeException("property 节点没有设置 name/value");
            }
            switch (name){
                case "driverClassName":
                    driverClassName = value;
                    break;
                case "url" :
                    url = value;
                    break;
                case "username" :
                    userName = value;
                    break;
                case "password":
                    password = value;
                    break;
                default:
                    throw new RuntimeException("属性名没有匹配");
            }
        }
            Connection connection = null;
        try {
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    //读取 XXXmapper.xml 能够创建 MapperBean 对象
    //path 是xml 路径
    //如果 XXXmapper.xml 文件是放在resources 目录下，直接传入 xml 文件名即可获取该文件
    public MapperBean readMapper(String path){

        MapperBean mapperBean = new MapperBean();
        try {
            //获取 xml 文件对应的流
            InputStream resourceAsStream = loader.getResourceAsStream(path);
            SAXReader saxReader = new SAXReader();
            //获取到 xml 文件对应的 document 对象
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
//            System.out.println("rootElement=" + rootElement);
            //获取到namespace
            String namespace = rootElement.attributeValue("namespace").trim();
            //设置 mapperBean 的 InterfaceName
            mapperBean.setInterfaceName(namespace);
            //得到root 的迭代器 遍历子节点 生成 Function
            Iterator rootIterator = rootElement.elementIterator();
            //保存接口下所有的方法信息
            List<Function> functions = new ArrayList<>();
            //遍历子节点生成 function
            while (rootIterator.hasNext()){
                //取出一个子节点
//      <select id="getMonsterById" resultType="com.jeffrey.entity.Monster">
//             select * from monster where id = ?
//        </select>
                Element ele = (Element)rootIterator.next();
                String sqlType = ele.getName().trim();
                String funcName = ele.attributeValue("id").trim();
                // resultType 返回的是全类名
                String resultType = ele.attributeValue("resultType").trim();
                String sql = ele.getText().trim();
                //开始封装
                Function function = new Function();
                function.setFuncName(funcName);
                function.setSql(sql);
                function.setSqlType(sqlType);
                //反射生成对象，放入对象
                Class<?> aClass = Class.forName(resultType);
                Object resultTypeInstance = aClass.newInstance();
                function.setResultType(resultTypeInstance);

                //将封装好的 function 放入到 functions中
                functions.add(function);
            }
            //循环结束后将 functions 放入 mapperBean
            mapperBean.setFunctions(functions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return mapperBean;
    }
}
