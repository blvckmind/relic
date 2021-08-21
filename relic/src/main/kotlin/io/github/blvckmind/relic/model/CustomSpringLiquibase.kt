package io.github.blvckmind.relic.model

import io.github.blvckmind.relic.util.loggerFor
import liquibase.integration.spring.SpringLiquibase
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import kotlin.system.exitProcess

open class CustomSpringLiquibase(val context: ConfigurableApplicationContext) : SpringLiquibase() {

    private val logger = loggerFor(CustomSpringLiquibase::class.java)

    override fun afterPropertiesSet() {
        try {
            super.afterPropertiesSet()
        } catch (e: Exception) {
            logger.error("Liquibase error", e)
            SpringApplication.exit(context, ExitCodeGenerator { 0 })
            exitProcess(0)
        }
    }


}
