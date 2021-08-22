package io.github.blvckmind.relic.util

import io.github.blvckmind.relic.persistence.model.entity.CalendarUnitEntity
import io.github.blvckmind.relic.persistence.model.enums.SourceTypeEnum
import io.github.blvckmind.relic.model.person_dto.CreatePersonDto
import io.github.blvckmind.relic.model.person_dto.PersonDto
import io.github.blvckmind.relic.model.person_dto.UpdatePersonDto

class CalendarUnitUtil {

    companion object {

        fun get(updatePersonDto: UpdatePersonDto): CalendarUnitEntity? {
            if (!anyDataPresent(updatePersonDto)) return null
            return CalendarUnitEntity(
                id = updatePersonDto.dobId,
                sourceId = updatePersonDto.id!!,
                sourceType = SourceTypeEnum.PersonEntity,
                day = updatePersonDto.dobDay,
                month = updatePersonDto.dobMonth,
                year = updatePersonDto.dobYear,
                notify = updatePersonDto.dobNotify.let { true })
        }

        fun get(createPersonDto: CreatePersonDto, personId: String): CalendarUnitEntity? {
            if (!anyDataPresent(createPersonDto)) return null
            return CalendarUnitEntity(
                id = createPersonDto.dobId,
                sourceId = personId,
                sourceType = SourceTypeEnum.PersonEntity,
                day = createPersonDto.dobDay,
                month = createPersonDto.dobMonth,
                year = createPersonDto.dobYear,
                notify = createPersonDto.dobNotify.let { true })
        }

        private fun anyDataPresent(personDto: PersonDto) =
            listOf(personDto.dobDay, personDto.dobMonth, personDto.dobYear).any { it != null && it > 0 }

    }

}
