package io.github.blvckmind.relic.service

import io.github.blvckmind.relic.model.project_dto.GetProjectDto
import io.github.blvckmind.relic.persistence.service.ProjectEntityService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = [Exception::class], isolation = Isolation.SERIALIZABLE)
class ProjectDtoService(val projectEntityService: ProjectEntityService) {

    fun getList(): List<GetProjectDto> {

    }

}