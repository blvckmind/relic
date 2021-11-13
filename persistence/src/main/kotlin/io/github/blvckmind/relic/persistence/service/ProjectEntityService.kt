package io.github.blvckmind.relic.persistence.service

import io.github.blvckmind.relic.persistence.model.entity.ProjectEntity
import io.github.blvckmind.relic.persistence.repository.ProjectEntityRepository
import org.springframework.stereotype.Service

@Service
class ProjectEntityService(private val projectEntityRepository: ProjectEntityRepository) {

    fun get(id: Int) = projectEntityRepository.findFirstById(id)

    fun all(): List<ProjectEntity> = projectEntityRepository.findAll()

    fun save(projectEntity: ProjectEntity) = projectEntityRepository.save(projectEntity);

    fun delete(projectEntity: ProjectEntity) = projectEntityRepository.delete(projectEntity);

    fun delete(id: Int) = projectEntityRepository.deleteById(id);
    
}