package io.github.blvckmind.relic.controller

import io.github.blvckmind.relic.service.PersonEntityService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/person")
class PersonController(private val personEntityService: PersonEntityService) {

    @GetMapping("/{id}")
    fun renderPersonById(@PathVariable("id") uuid: String, model: Model): String {
        model.addAttribute("person_id", uuid)
        return "person_by_id"
    }

}