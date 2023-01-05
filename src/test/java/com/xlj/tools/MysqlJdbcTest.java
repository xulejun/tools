package com.xlj.tools;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * @author legend xu
 * @date 2023/1/5
 */
@Slf4j
public class MysqlJdbcTest {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/personal?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";
        //1.加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.连接数据库
        Connection connection = DriverManager.getConnection(url, username, password);

        String sql = "select * from product where name = ?";
        String ids = "鼠标1";

        ResultSet resultSet = null;
        try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setObject(1, ids);
            resultSet = prepareStatement.executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getString("id"));
            }
        } catch (SQLException e) {
            log.error("ClickHouse 批量插入异常：", e);
        }
    }
}
