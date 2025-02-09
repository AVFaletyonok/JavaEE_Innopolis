package org.example.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class JDBCTemplateConfig {

    public static JdbcTemplate jdbcTemplate() {
        DriverManagerDataSource driver = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/postgres", "postgres", "1");
        driver.setDriverClassName("org.postgresql.Driver");
        driver.setSchema("public");
        return new JdbcTemplate(driver);
    }
}
