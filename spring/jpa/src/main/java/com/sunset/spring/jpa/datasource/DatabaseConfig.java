package com.sunset.spring.jpa.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EntityScan(basePackages = "com.sunset.spring.jpa")
@EnableJpaRepositories(basePackages = "com.sunset.spring.jpa") // ?
@EnableTransactionManagement // ?
@Configuration
public class DatabaseConfig {

    @Primary
    @Bean(name = "baseDataSource")
    public DataSource baseDataSource(BaseDataSourceProperties baseDataSourceProperties) {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(baseDataSourceProperties.getJdbcUrl())
                .username(baseDataSourceProperties.getUsername())
                .password(baseDataSourceProperties.getPassword())
                .driverClassName(baseDataSourceProperties.getDriverClassName())
                .build();
    }
}
