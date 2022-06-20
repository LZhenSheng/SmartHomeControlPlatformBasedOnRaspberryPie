package com.example.myapplication.util;

import android.util.Log;


import com.example.myapplication.domain.DHT;
import com.example.myapplication.domain.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {
    private static final String TAG ="Dbutils";
    private static String driver="driver";
    private static String user="user";
    private static String password = "password";

    public static Connection getConn(){
        Connection connection = null;
        try{
            Class.forName(driver);
            String url = "url";
            connection= DriverManager.getConnection(url, user, password);

            Log.e("数据库连接", "成功!");
        } catch (Exception e) {
            Log.e("数据库连接", "失败!");
            e.printStackTrace();
        }
        return connection;
    }
    public static User getUser() {
        User user=new User();
        Connection connection = getConn();
        if (connection!=null){
            String sql="select * from user";
            try{
                java.sql.Statement statement = connection.createStatement();
                ResultSet rSet = statement.executeQuery(sql);
                while (rSet.next()) {
                    user.setName(rSet.getString("name"));
                    user.setPassword(rSet.getString("password"));
                    Log.e(TAG,"数组组装成功");
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        Log.e(TAG,"数组已返回");
        return user;
    }

    public static DHT getTemperature() {
        DHT dht=new DHT();
        Connection connection = getConn();
        if (connection!=null){
            String sql="select * from tb_dht11";
            try{
                java.sql.Statement statement = connection.createStatement();
                ResultSet rSet = statement.executeQuery(sql);
                while (rSet.next()) {
                    dht.setTemperation(rSet.getString("temperature"));
                    dht.setHumdity(rSet.getString("humidity"));
                    Log.e(TAG,"数组组装成功");
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        Log.e(TAG,"数组已返回");
        return dht;
    }
}

