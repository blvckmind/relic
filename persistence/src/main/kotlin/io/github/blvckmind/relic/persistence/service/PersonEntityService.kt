package io.github.blvckmind.relic.persistence.service

import io.github.blvckmind.relic.persistence.model.entity.PersonEntity
import io.github.blvckmind.relic.persistence.model.entity.ProjectEntity
import io.github.blvckmind.relic.persistence.repository.PersonRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersonEntityService(private val personRepository: PersonRepository) {

    val sort = Sort.by("updatedDate").descending()

    fun save(personEntity: PersonEntity): PersonEntity {
        personEntity.updatedDate = Date()
        return personRepository.save(personEntity)
    }

    fun search(keyword: String?, project: ProjectEntity?): List<PersonEntity> {
        if (keyword.isNullOrBlank() && project == null) {
            return personRepository.findAll(sort)
        }

        if (keyword.isNullOrBlank()) {
            return personRepository.findAllByProject(project!!, sort)
        }

        if (project == null){
            return personRepository.fullTextSearch(keyword.trim().lowercase(), sort)
        }

        return personRepository.fullTextSearchWithProject(
            keyword.trim().lowercase(),
            project,
            sort
        )
    }

    fun getAll(): List<PersonEntity> = personRepository.findAll()

    fun getById(uuid: String): PersonEntity? = personRepository.findFirstById(uuid)

    fun getCount() = personRepository.count()

    fun getCount(projectEntity: ProjectEntity) = personRepository.countAllByProject(projectEntity)

    fun delete(personEntity: PersonEntity) = personRepository.delete(personEntity)

    fun delete(id: String) = personRepository.deleteById(id)

    fun delete(ids: Iterable<String>) = personRepository.deleteAllById(ids)

}