package io.github.blvckmind.relic.config

import io.github.blvckmind.relic.model.LocalhostInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.InterceptorRegistry

@Configuration
class WebMvcConfig (val localhostInterceptor: LocalhostInterceptor) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(localhostInterceptor)
    }
}