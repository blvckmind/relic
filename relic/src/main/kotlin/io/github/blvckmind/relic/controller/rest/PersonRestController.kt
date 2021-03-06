package io.github.blvckmind.relic.controller.rest

import io.github.blvckmind.relic.persistence.model.entity.PersonEntity
import io.github.blvckmind.relic.model.person_dto.CreatePersonDto
import io.github.blvckmind.relic.model.ResponseEntities
import io.github.blvckmind.relic.model.person_dto.GetPersonDto
import io.github.blvckmind.relic.model.person_dto.UpdatePersonDto
import io.github.blvckmind.relic.service.PersonDtoService
import io.github.blvckmind.relic.persistence.service.PersonEntityService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/person")
class PersonRestController(
    val personEntityService: PersonEntityService,
    val personDtoService: PersonDtoService
) {

    @GetMapping
    fun getAllPersons(): List<PersonEntity> = personEntityService.getAll()

    @GetMapping("/id/{id}")
    fun getById(@PathVariable("id") id: String) = personDtoService.getById(id)

    @PostMapping("/create")
    fun createPerson(@RequestBody createPersonDto: CreatePersonDto) = personDtoService.create(createPersonDto)

    @PostMapping("/search")
    fun searchPersons(
        @RequestParam("name", required = false) name: String?,
        @RequestParam("project_id", required = false) projectId: Int?
    ): ResponseEntities<GetPersonDto> {
        val persons = personDtoService.search(name, projectId)
        return ResponseEntities(persons, -1, persons.size.toLong())
    }

    @PutMapping("/update")
    fun updatePerson(@RequestBody updatePersonDto: UpdatePersonDto) = personDtoService.update(updatePersonDto)

    @DeleteMapping("/delete")
    fun deletePerson(@RequestParam("id") id: String) = personDtoService.delete(id)

    @DeleteMapping("/delete_list")
    fun deletePersonList(@RequestParam("ids") ids: Set<String>) = ids.forEach { personDtoService.delete(it) }

}