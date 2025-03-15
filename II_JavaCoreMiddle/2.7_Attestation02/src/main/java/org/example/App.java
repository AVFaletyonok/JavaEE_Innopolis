package org.example;

import org.flywaydb.core.Flyway;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App
{
    public static void main( String[] args )
    {
        // Чтение настроек из файла properties
//        Properties properties = new Properties();
//        try (InputStream input = new FileInputStream("application.properties")) {
//            properties.load(input);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        String url = properties.getProperty("spring.datasource.url");
//        String user = properties.getProperty("spring.datasource.user");
//        String password = properties.getProperty("spring.datasource.password");

//        String url = "jdbc:postgresql://host.docker.internal:5432/postgres";
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
//        String password = "postgres";
        String password = "1";

        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .load();
        flyway.migrate();

        System.out.println( "Hello World!" );
    }
}
