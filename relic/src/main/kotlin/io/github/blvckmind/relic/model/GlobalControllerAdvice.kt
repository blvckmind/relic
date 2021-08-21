package io.github.blvckmind.relic.model

import io.github.blvckmind.relic.config.ApplicationProperties
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

@ControllerAdvice
class GlobalControllerAdvice(val applicationProperties: ApplicationProperties) {

    @ModelAttribute("application_properties")
    fun populateApplicationProperties() = applicationProperties

}