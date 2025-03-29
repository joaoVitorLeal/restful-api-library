package io.github.joaoVitorLeal.libraryapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class DataSourceConfiguration {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.driver-class-name}")
    String driver;

    @Bean
    public DataSource hikariDataSource() {
        log.info("Initiating database connection at the URL: {}", url); // Log database connection attempt

        HikariConfig config = new HikariConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);

        // Configures the connection pool properties for the data source
        config.setMaximumPoolSize(10); // Maximum connections in the pool
        config.setMinimumIdle(1); // Minimum idle connections in the pool
        config.setPoolName("library-db-pool"); // Name of the pool
        config.setMaxLifetime(6000000); // Connection lifetime in milliseconds
        config.setConnectionTimeout(100000); // Timeout for establishing a connection
        config.setConnectionTestQuery("select 1"); // Query to test connection health

        return new HikariDataSource(config);
    }
}
