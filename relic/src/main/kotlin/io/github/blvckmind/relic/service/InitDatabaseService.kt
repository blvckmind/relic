package io.github.blvckmind.relic.service

import io.github.blvckmind.relic.domain.entity.CalendarUnitEntity
import io.github.blvckmind.relic.domain.entity.PersonEntity
import io.github.blvckmind.relic.domain.enums.SourceTypeEnum
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct

@Service
@Profile("dev")
class InitDatabaseService(private val personEntityService: PersonEntityService) {

//    @PostConstruct
    fun createPersons() {
        val uuid = UUID.randomUUID().toString()
        val dobEntity = CalendarUnitEntity(null, uuid, SourceTypeEnum.PersonEntity, 31, 12, 1982, true)

        val person = PersonEntity(id = uuid, firstName = "Name", lastName = "Lastname", dob = dobEntity)

        personEntityService.save(person)
    }

}