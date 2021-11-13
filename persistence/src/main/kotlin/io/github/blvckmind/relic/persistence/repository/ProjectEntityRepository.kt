package io.github.blvckmind.relic.persistence.repository

import io.github.blvckmind.relic.persistence.model.entity.ProjectEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectEntityRepository : JpaRepository<ProjectEntity, Int> {
    fun findFirstById(id: Int): ProjectEntity?
}
