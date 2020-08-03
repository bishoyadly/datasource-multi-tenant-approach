package com.example.demo.config;

import javax.sql.DataSource;

public interface DataSourceMigration {

    void applyTenantDataSourceMigration(DataSource dataSource);
}
