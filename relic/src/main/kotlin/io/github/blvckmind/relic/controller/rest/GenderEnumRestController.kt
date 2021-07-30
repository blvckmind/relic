package io.github.blvckmind.relic.controller.rest

import io.github.blvckmind.relic.domain.enums.GenderEnum
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/gender_enum")
class GenderEnumRestController {

    @GetMapping
    fun getAllPersons() = GenderEnum.values()

}