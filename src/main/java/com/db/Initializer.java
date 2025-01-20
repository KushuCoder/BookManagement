package com.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class Initializer {
    String url = "jdbc:postgresql://localhost:5432/intellij";
    String user = System.getenv("DB_USERNAME");
    String password = System.getenv("DB_PASSWORD");
    public Connection creation(){
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);

        }catch(Exception e){
            e.printStackTrace();
        }
        return connection;
    }
}
