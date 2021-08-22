package io.github.blvckmind.relic.persistence.service

import io.github.blvckmind.relic.persistence.annotation.ValidName
import io.github.blvckmind.relic.persistence.model.entity.PersonEntity
import io.github.blvckmind.relic.persistence.repository.PersonRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersonEntityService(private val personRepository: PersonRepository) {

    fun save(@ValidName personEntity: PersonEntity): PersonEntity {
        personEntity.updatedDate = Date()
        return personRepository.save(personEntity)
    }

    fun getPersonsByName(keyword: String?, page: Int): Page<PersonEntity> {
        val sortedByName: Pageable = PageRequest.of(page, 1000, Sort.by("updatedDate").descending())

        if (keyword.isNullOrBlank()) {
            return personRepository.findAll(sortedByName)
        }

        return personRepository.fullTextSearch(keyword.trim().lowercase(), sortedByName)
    }

    fun getAll(): List<PersonEntity> = personRepository.findAll()

    fun getById(uuid: String): PersonEntity? = personRepository.findFirstById(uuid)

    fun delete(personEntity: PersonEntity) = personRepository.delete(personEntity)

    fun delete(id: String) = personRepository.deleteById(id)

    fun delete(ids: Iterable<String>) = personRepository.deleteAllById(ids)

}