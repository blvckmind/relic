package io.github.blvckmind.relic.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Validated
@Configuration
@ConstructorBinding
@EnableConfigurationProperties
@ConfigurationProperties("relic")
class ApplicationProperties {

    @NotNull
    var authorization: AuthorizationProperties = AuthorizationProperties()

    data class AuthorizationProperties(
        @NotBlank
        val password: String = UUID.randomUUID().toString(),

        @NotNull
        val outsideAccess: Boolean = false
    )

}