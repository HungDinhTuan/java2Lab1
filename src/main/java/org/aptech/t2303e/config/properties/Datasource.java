package org.aptech.t2303e.config.properties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Datasource {
    private static Connection conn;

    public static synchronized Connection getConn(){
        if(conn == null){
            //init conn
            init();
        }
        return conn;
    }

    public static void init(){
        DatabaseProperties dataProps = new DatabaseProperties();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dataProps.getUrl(), dataProps.getUsername(), dataProps.getPassword());
            System.err.println(conn != null ? "connect ok" : "connect error");
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
