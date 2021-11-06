package io.github.blvckmind.relic.persistence.service

import io.github.blvckmind.relic.persistence.model.entity.ProjectEntity
import io.github.blvckmind.relic.persistence.repository.ProjectEntityRepository
import org.springframework.stereotype.Service

@Service
class ProjectEntityService(private val projectEntityRepository: ProjectEntityRepository) {

    fun all(): List<ProjectEntity> = projectEntityRepository.findAll()

    
}