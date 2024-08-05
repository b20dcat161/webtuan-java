package com.example.demo8.DAO;


import java.sql.*;
import com.example.demo8.Demo8Application;

public class MySQLConnection {
    public Connection getConnection() {
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/student?useUnicode=true&characterEncoding=utf8";
        String username = "root";
        String password = "root";
        try {
            // load driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            // Xử lý lỗi: không tìm thấy driver
            e.printStackTrace();
        } catch (SQLException e) {
            // Xử lý lỗi SQL
            e.printStackTrace();
        }

        return conn;
    }

    public static void main(String[] args) {
        MySQLConnection con =  new MySQLConnection();
        System.out.println( con.getConnection());
    }
}
