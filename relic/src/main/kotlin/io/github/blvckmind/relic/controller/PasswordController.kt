package io.github.blvckmind.relic.controller

import io.github.blvckmind.relic.config.ApplicationProperties
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PasswordController(val applicationProperties: ApplicationProperties) {
    @GetMapping("/password")
    fun password(): String {
        if (applicationProperties.authorization.outsideAccess){
            return "password"
        }
        return "redirect:/"
    }
}