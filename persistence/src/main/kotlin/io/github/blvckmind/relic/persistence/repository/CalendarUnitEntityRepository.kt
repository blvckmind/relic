package io.github.blvckmind.relic.persistence.repository

import io.github.blvckmind.relic.persistence.model.entity.CalendarUnitEntity
import io.github.blvckmind.relic.persistence.model.enums.SourceTypeEnum
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CalendarUnitEntityRepository : JpaRepository<CalendarUnitEntity, Int> {

    fun getAllBySourceId(sourceId: String, pageable: Pageable): Page<CalendarUnitEntity>

    fun getAllBySourceIdAndSourceType(sourceId: String, sourceType: SourceTypeEnum, pageable: Pageable): Page<CalendarUnitEntity>

    fun deleteAllBySourceIdAndSourceType(sourceId: String, sourceType: SourceTypeEnum)

}