package zjc.examples.r2dbc.configuration;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;

@Configuration
@EnableR2dbcRepositories(basePackages = "zjc.examples.r2dbc.repository")
public class R2DBCConfiguration extends AbstractR2dbcConfiguration {

    @Bean
    public PostgresqlConnectionFactory connectionFactory() {
                return new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
                .host("localhost")
                .port(5432)
                .username("postgres")
                .password("postgres")
                .database("reactivedb")
                .build());
    }
}
