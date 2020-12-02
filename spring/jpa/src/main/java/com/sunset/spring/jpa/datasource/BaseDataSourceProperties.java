package com.sunset.spring.jpa.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(value="sunset.datasource.base")
public class BaseDataSourceProperties {
    private String jdbcUrl;

    private String username;

    private String password;

    private String driverClassName;

    private String poolName;

    private int maximumPoolSize;

    private int minimumIdle;

    private int maxLifetime;

    private int connectionTimeout;

    private int idleTimeout;
}
