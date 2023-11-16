package org.aptech.t2303e.config.properties;

import lombok.Data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Data
public class DatabaseProperties {
    private String url;
    private String username;
    private String password;

    public DatabaseProperties() {
        // create reader for props file
        try{
            FileReader reader = new FileReader("./src/main/resources/application.properties");
            Properties props = new Properties();
            props.load(reader);
            this.setUrl(props.getProperty("datasource.mysql.url"));
            this.setUsername(props.getProperty("datasource.mysql.username"));
            this.setPassword(props.getProperty("datasource.mysql.password"));
        }catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        DatabaseProperties databaseProperties = new DatabaseProperties();
    }
}
