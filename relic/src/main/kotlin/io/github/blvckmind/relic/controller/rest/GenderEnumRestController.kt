package io.github.blvckmind.relic.controller.rest

import io.github.blvckmind.relic.persistence.model.enums.GenderEnum
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/gender_enum")
class GenderEnumRestController {

    @GetMapping
    fun getAllPersons() = GenderEnum.values()

}