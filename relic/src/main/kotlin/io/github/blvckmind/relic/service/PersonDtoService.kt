package io.github.blvckmind.relic.service

import io.github.blvckmind.relic.persistence.model.entity.CalendarUnitEntity
import io.github.blvckmind.relic.persistence.model.entity.PersonEntity
import io.github.blvckmind.relic.persistence.model.enums.SourceTypeEnum
import io.github.blvckmind.relic.model.person_dto.CreatePersonDto
import io.github.blvckmind.relic.model.person_dto.GetPersonDto
import io.github.blvckmind.relic.model.person_dto.UpdatePersonDto
import io.github.blvckmind.relic.persistence.model.entity.ProjectEntity
import io.github.blvckmind.relic.persistence.service.CalendarUnitEntityService
import io.github.blvckmind.relic.persistence.service.ImageEntityService
import io.github.blvckmind.relic.persistence.service.PersonEntityService
import io.github.blvckmind.relic.persistence.service.ProjectEntityService
import io.github.blvckmind.relic.util.CalendarUnitUtil
import io.github.blvckmind.relic.util.colors
import io.github.blvckmind.relic.util.notNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.Exception

@Service
@Transactional(rollbackFor = [Exception::class], isolation = Isolation.SERIALIZABLE)
class PersonDtoService(
    val personEntityService: PersonEntityService,
    val calendarUnitEntityService: CalendarUnitEntityService,
    val projectEntityService: ProjectEntityService,
    val imageEntityService: ImageEntityService
) {

    fun create(createPersonDto: CreatePersonDto): GetPersonDto {
        val personId = UUID.randomUUID().toString()

        val dobEntity: CalendarUnitEntity? = CalendarUnitUtil.get(createPersonDto, personId)

        val personEntity = PersonEntity(
            id = personId,
            firstName = createPersonDto.firstName,
            lastName = createPersonDto.lastName,
            patronymic = createPersonDto.patronymic,
            otherNames = createPersonDto.otherNames,
            information = createPersonDto.information,
            gender = createPersonDto.gender,
            colorNumber = (0..colors.size - 1).random().toShort(),
            dob = dobEntity
        )

        personEntityService.save(personEntity)

        return getById(personId)!!
    }

    fun update(updatePersonDto: UpdatePersonDto): GetPersonDto {
        val personEntity = personEntityService.getById(
            updatePersonDto.id ?: throw IllegalArgumentException("Person ID is not present")
        )
            ?: throw IllegalArgumentException("There is no Person with ID: '${updatePersonDto.id}'")

        personEntity.updatedDate = Date()
        personEntity.firstName = updatePersonDto.firstName?.ifBlank { null }
        personEntity.lastName = updatePersonDto.lastName?.ifBlank { null }
        personEntity.patronymic = updatePersonDto.patronymic?.ifBlank { null }
        personEntity.otherNames = updatePersonDto.otherNames?.ifBlank { null }
        personEntity.information = updatePersonDto.information?.ifBlank { null }
        personEntity.gender = updatePersonDto.gender

        /* photo */
        if (personEntity.photoId != null && !personEntity.photoId.equals(updatePersonDto.photoId)) {
            imageEntityService.deleteByChecksum(personEntity.photoId!!)
        }
        personEntity.photoId = updatePersonDto.photoId?.ifBlank { null }

        /* dob */
        val modifiedDob = CalendarUnitUtil.get(updatePersonDto)
        if (modifiedDob == null && personEntity.dob != null) {
            calendarUnitEntityService.delete(personEntity.dob!!)
        }
        personEntity.dob = modifiedDob

        personEntityService.save(personEntity)

        return getById(updatePersonDto.id)!!
    }

    fun getById(id: String): GetPersonDto? {
        val personEntity: PersonEntity = personEntityService.getById(id) ?: return null

        val getPersonDto = GetPersonDto()
        getPersonDto.id = personEntity.id

        getPersonDto.photoId = personEntity.photoId

        getPersonDto.firstName = personEntity.firstName
        getPersonDto.lastName = personEntity.lastName
        getPersonDto.patronymic = personEntity.patronymic
        getPersonDto.otherNames = personEntity.otherNames

        getPersonDto.information = personEntity.information

        getPersonDto.gender = personEntity.gender
        getPersonDto.color = colors[personEntity.colorNumber?.toInt() ?: 0]


        personEntity.dob.notNull {
            getPersonDto.dobId = it.id
            getPersonDto.dobDay = it.day
            getPersonDto.dobMonth = it.month
            getPersonDto.dobYear = it.year
            getPersonDto.dobNotify = it.notify
        }

        return getPersonDto
    }

    fun delete(id: String) {
        val personEntity = personEntityService.getById(id)
            ?: throw IllegalArgumentException("There is no Person with ID: '${id}'")

        personEntity.photoId.notNull {
            imageEntityService.deleteByChecksum(it)
        }

//        personEntity.dob.notNull {
//            calendarUnitEntityService.delete(it)
//        }

        calendarUnitEntityService.deleteAllBySourceIdAndSourceType(id, SourceTypeEnum.PersonEntity)

        personEntityService.delete(id)
    }

    fun getCount() = personEntityService.getCount()

    fun getCount(projectId: Int): Int {
        val project = projectEntityService.get(projectId)
            ?: throw IllegalArgumentException("There is no Project with ID: '${projectId}'")
        return personEntityService.getCount(project)
    }

    fun search(keyword: String?, projectId: Int?): List<GetPersonDto> {
        var projectEntity: ProjectEntity? = null

        projectId.notNull {
            projectEntity = projectEntityService.get(it)

            if (projectEntity == null) {
                throw IllegalArgumentException("There is no Project with ID: '${it}'")
            }
        }

        return personEntityService
            .search(keyword, projectEntity)
            .map { toGetPersonDto(it) }
    }

    private fun toGetPersonDto(personEntity: PersonEntity): GetPersonDto {
        val getPersonDto = GetPersonDto()
        getPersonDto.id = personEntity.id
        getPersonDto.photoId = personEntity.photoId
        getPersonDto.firstName = personEntity.firstName
        getPersonDto.lastName = personEntity.lastName
        getPersonDto.patronymic = personEntity.patronymic
        getPersonDto.color = colors[personEntity.colorNumber?.toInt() ?: 0]
        return getPersonDto
    }

}