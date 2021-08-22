package io.github.blvckmind.relic.persistence.model.entity

import jakarta.validation.constraints.Size
import org.springframework.data.annotation.CreatedDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "projects")
data class ProjectEntity(
        @Id
        @Column(length = 36)
        var id: String?,

        @Column(name = "photo_id", length = 64)
        @Size(min = 64, max = 64)
        var photo_id: String?,

        @Column(name = "title", length = 128)
        @Size(max = 128)
        var title: String,

        @Column(name = "description", length = 1024)
        @Size(max = 1024)
        var description: String?,

        @CreatedDate
        @Column(updatable = false)
        val createdDate: Date?
)