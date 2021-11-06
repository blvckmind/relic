package io.github.blvckmind.relic.persistence.model.entity

import io.github.blvckmind.relic.persistence.model.enums.GenderEnum
import io.github.blvckmind.relic.persistence.model.enums.TrustLevelEnum
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "persons")
@EntityListeners(AuditingEntityListener::class)
data class PersonEntity(
        @Id
        @Column(length = 36)
        var id: String,

        @Column(name = "photo_id", length = 64)
        @Size(min = 64, max = 64)
        var photoId: String? = null,

        @Column(name = "color_number")
        var colorNumber: Short? = 0,

        @Column(name = "first_name", length = 128)
        var firstName: String? = null,

        @Column(name = "last_name", length = 128)
        var lastName: String? = null,

        @Column(name = "patronymic", length = 128)
        var patronymic: String? = null,

        @Column(name = "other_names", length = 384)
        var otherNames: String? = null,

        @Column(name = "gender", length = 16)
        @Enumerated(EnumType.STRING)
        var gender: GenderEnum? = null,

        @Column(name = "trust_level", length = 16)
        @Enumerated(EnumType.STRING)
        var trustLevel: TrustLevelEnum? = null,

        @OneToOne(
            cascade = [(CascadeType.ALL)],
            fetch = FetchType.EAGER,
            targetEntity = CalendarUnitEntity::class
        )
        @JoinColumn(name = "dob_cal_id")
        var dob: CalendarUnitEntity? = null,

        @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = ProjectEntity::class
        )
        @JoinColumn(name = "project_id")
        var project: ProjectEntity? = null,

        @Lob
        @Column(name = "info")
        var information: String? = null,

        @CreatedDate
        @Column(updatable = false)
        val createdDate: Date? = null,

        @LastModifiedDate
        @Column
        var updatedDate: Date? = null
)