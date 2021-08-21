package io.github.blvckmind.relic.config

import io.github.blvckmind.relic.model.CustomSpringLiquibase
import org.springframework.context.annotation.Configuration
import liquibase.integration.spring.SpringLiquibase
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import javax.sql.DataSource


@Configuration
class LiquibaseConfig(val dataSource: DataSource) {

    @Bean
    @ConditionalOnProperty(prefix = "spring.liquibase", value = ["enabled"])
    @ConfigurationProperties(prefix= "spring.liquibase")
    fun liquibase(context: ConfigurableApplicationContext): SpringLiquibase {
        val liquibase = CustomSpringLiquibase(context)
        liquibase.dataSource = dataSource
        return liquibase
    }

}