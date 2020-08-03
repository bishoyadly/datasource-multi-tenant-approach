package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class MultiTenantConfiguration {

    private DataSourceProperties dataSourceProperties;
    private DataSourceMigration dataSourceMigration;

    @Autowired
    MultiTenantConfiguration(DataSourceProperties dataSourceProperties,
                             @Qualifier("liquibaseDataSourceMigration") DataSourceMigration dataSourceMigration) {
        this.dataSourceProperties = dataSourceProperties;
        this.dataSourceMigration = dataSourceMigration;
    }


    public DataSource defaultDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder
                .create(this.getClass().getClassLoader())
                .driverClassName(dataSourceProperties.getDriverClassName())
                .url(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword());

        return dataSourceBuilder.build();
    }


    @Bean
    public DataSource dataSource() {
        File[] files = Paths.get("src/main/resources/tenants").toFile().listFiles();
        Map<Object, Object> resolvedDataSources = new HashMap<>();


        for (File tenantPropertyFile : files) {

            Properties tenantProperties = new Properties();
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create(this.getClass().getClassLoader());

            try {
                tenantProperties.load(new FileInputStream(tenantPropertyFile));
                String tenantId = tenantProperties.getProperty("name");
                dataSourceBuilder
                        .driverClassName(dataSourceProperties.getDriverClassName())
                        .url(tenantProperties.getProperty("datasource.url"))
                        .username(tenantProperties.getProperty("datasource.username"))
                        .password(tenantProperties.getProperty("datasource.password"));
                DataSource dataSource = dataSourceBuilder.build();
                resolvedDataSources.put(tenantId, dataSource);
                dataSourceMigration.applyTenantDataSourceMigration(dataSource);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        MultiTenantDataSource multiTenantDataSource = new MultiTenantDataSource();
        multiTenantDataSource.setTargetDataSources(resolvedDataSources);
        multiTenantDataSource.setDefaultTargetDataSource(defaultDataSource());
        multiTenantDataSource.afterPropertiesSet();
        return multiTenantDataSource;
    }


}
