package io.github.joaoVitorLeal.libraryapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.driver-class-name}")
    String driver;

    /**
     * Criação de um data source básico.
     * */
//    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource(); // Não utiliza-se em produção
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driver);

        return ds;
    }

    /**
     * Por PADRÃO o Spring Boot ja usa o Hikari
     * Conexão com o Hikari data source,
     * é utilizado em produção por possuir melhor desempenho,
     * maior complexidade e gestão dinâmico de pool de conexões.
     * Repositório Hikari: https://github.com/brettwooldridge/HikariCP
     *
     * @return config
     */
    @Bean  // Configurando Data Source
    public DataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);

        /*
         * Configurando as propriedades do pool de conexões do Data Source.
         * */
        config.setMaximumPoolSize(10); // Tamanho máximo de conexões no pool - (10)
        config.setMinimumIdle(1); // Tamanho mínimo inicial do pool - (1)
        config.setPoolName("library-db-pool"); // Nome do pool
        config.setMaxLifetime(6000000); // Duração da conexão em milissegundos // 600 mil ms (10 minutos)
        config.setConnectionTimeout(100000); // Timeout para estabelecer uma conexão
        config.setConnectionTestQuery("select 1"); // Query de teste de conexão

        return new HikariDataSource(config);
    }
}
