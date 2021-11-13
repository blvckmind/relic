package io.github.blvckmind.relic.persistence.repository

import io.github.blvckmind.relic.persistence.model.entity.PersonEntity
import io.github.blvckmind.relic.persistence.model.entity.ProjectEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<PersonEntity, String> {
    fun findFirstById(uuid: String?): PersonEntity?

    fun findAllByFirstNameIsLike(firstName: String): List<PersonEntity>
    fun findAllByFirstNameIsLike(firstName: String, pageable: Pageable): Page<PersonEntity>
    fun findAllByFirstNameIsContainingIgnoreCase(firstName: String, pageable: Pageable): Page<PersonEntity>

    @Query(
        value = "select p from PersonEntity p where lower(p.firstName) like %:name%" +
                " OR lower(p.lastName) like %:name%  OR lower(p.otherNames) like %:name%"
    )
    fun fullTextSearch(@Param("name") text: String, sort: Sort): MutableList<PersonEntity>

    @Query(
        value = "select p from PersonEntity p where p.project = :projectEntity AND (lower(p.firstName) like %:name%" +
                " OR lower(p.lastName) like %:name%  OR lower(p.otherNames) like %:name%)"
    )
    fun fullTextSearchWithProject(
        @Param("name") text: String,
        @Param("projectEntity") projectEntity: ProjectEntity,
        sort: Sort
    ): MutableList<PersonEntity>

    fun countAllByProject(project: ProjectEntity): Int

    fun findAllByProject(project: ProjectEntity, sort: Sort): MutableList<PersonEntity>
}