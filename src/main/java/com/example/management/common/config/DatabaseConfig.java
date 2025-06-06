package com.example.management.common.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {


    private String parseRailwayUrl(String railwayUrl) {
        try {
            System.out.println("Original Railway URL: " + railwayUrl);

            if (railwayUrl.startsWith("postgresql://")) {
                String withoutProtocol = railwayUrl.substring("postgresql://".length());
                int atIndex = withoutProtocol.indexOf('@');
                if (atIndex > 0) {
                    String hostPortDb = withoutProtocol.substring(atIndex + 1);
                    String fixedUrl = "jdbc:postgresql://" + hostPortDb;
                    System.out.println("Parsed Railway URL: " + railwayUrl + " -> " + fixedUrl);
                    return fixedUrl;
                }
            }
            return railwayUrl;
        } catch (Exception e) {
            System.err.println("Failed to parse Railway URL: " + e.getMessage());
            return railwayUrl;
        }
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        String username = System.getenv("PGUSER");
        String password = System.getenv("PGPASSWORD");

        if (databaseUrl == null) {
            databaseUrl = "jdbc:postgresql://localhost:5432/usermanagement";
        } else {
            databaseUrl = parseRailwayUrl(databaseUrl);
        }

        if (username == null) {
            username = "postgres";
        }

        if (password == null) {
            password = "root";
        }

        System.out.println("Database URL: " + databaseUrl);
        System.out.println("Username: " + username);

        return DataSourceBuilder.create()
                .url(databaseUrl)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}