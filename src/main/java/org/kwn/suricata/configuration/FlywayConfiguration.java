package org.kwn.suricata.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfiguration {

    // as we use the same database from environment
    //private final Environment environment;

    private final String databaseURL;

    private final String username;

    private final String password;

    public FlywayConfiguration(
            @Value("${spring.datasource.url}") String databaseURL,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password
    ) {
        this.databaseURL = databaseURL;
        this.username = username;
        this.password = password;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway()  {
        return new Flyway(Flyway.configure()
                .baselineOnMigrate(true)
                .dataSource(databaseURL, username, password));
    }
}
