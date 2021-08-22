package io.github.blvckmind.relic.model.person_dto

import io.github.blvckmind.relic.persistence.model.enums.GenderEnum

open class PersonDto {
    var photoId: String? = null

    var firstName: String? = null
    var lastName: String? = null
    var patronymic: String? = null
    var otherNames: String? = null

    var information: String? = null

    var gender: GenderEnum? = null

    var dobId: Long? = null
    var dobDay: Short? = null
    var dobMonth: Short? = null
    var dobYear: Short? = null
    var dobNotify: Boolean? = null
    var color: String? = "#1cb491"
}
