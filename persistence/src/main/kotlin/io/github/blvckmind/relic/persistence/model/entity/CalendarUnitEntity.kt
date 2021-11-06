package io.github.blvckmind.relic.persistence.model.entity

import io.github.blvckmind.relic.persistence.model.enums.SourceTypeEnum
import jakarta.validation.constraints.Size
import javax.persistence.*

@Entity
@Table(name = "calendar_units")
data class CalendarUnitEntity(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,

        @Column(name = "source_id", length = 36, nullable = false)
        var sourceId: String,

        @Column(name = "source_type", nullable = false, updatable = false, length = 64)
        @Enumerated(EnumType.STRING)
        var sourceType: SourceTypeEnum,

        @Column(name = "day", length = 2)
        @Size(min = 1, max = 31)
        var day: Short? = null,

        @Column(name = "month", length = 2)
        @Size(min = 1, max = 12)
        var month: Short? = null,

        @Column(name = "year", length = 4)
        var year: Short? = null,

        @Column(name = "notify")
        var notify: Boolean = true

)