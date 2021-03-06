package io.github.blvckmind.relic.persistence.service

import io.github.blvckmind.relic.persistence.model.entity.CalendarUnitEntity
import io.github.blvckmind.relic.persistence.model.enums.SourceTypeEnum
import io.github.blvckmind.relic.persistence.repository.CalendarUnitEntityRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class CalendarUnitEntityService(private val calendarUnitEntityRepository: CalendarUnitEntityRepository) {

    fun getAllBySourceId(sourceId: String, page: Int): Page<CalendarUnitEntity> =
            calendarUnitEntityRepository.getAllBySourceId(sourceId, PageRequest.of(page, 100, Sort.by("year").descending()))

    fun getAllBySourceIdAndSourceType(sourceId: String, sourceType: SourceTypeEnum, page: Int): Page<CalendarUnitEntity> =
            calendarUnitEntityRepository.getAllBySourceIdAndSourceType(sourceId, sourceType, PageRequest.of(page, 100, Sort.by("year").descending()))

    fun deleteById(id: Int) = calendarUnitEntityRepository.deleteById(id)

    fun delete(calendarUnitEntity: CalendarUnitEntity) = calendarUnitEntityRepository.delete(calendarUnitEntity)

    @Transactional
    open fun deleteAllBySourceIdAndSourceType(sourceId: String, sourceType: SourceTypeEnum) =
        calendarUnitEntityRepository.deleteAllBySourceIdAndSourceType(sourceId, sourceType)
}