package io.github.blvckmind.relic.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PasswordController {
    @GetMapping("/password")
    fun password() = "password"
}