package com.example.demo.config;

import lombok.SneakyThrows;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Objects;

@Component
public class FlywayDataSourceMigration implements DataSourceMigration {

    @Override
    @SneakyThrows
    public void applyTenantDataSourceMigration(DataSource dataSource) {
        String migrationDirPath = "db/migration";
        FluentConfiguration fluentConfiguration = Flyway
                .configure().locations(migrationDirPath).dataSource(dataSource);
        Flyway flyway = new Flyway(fluentConfiguration);
        flyway.migrate();
    }
}
