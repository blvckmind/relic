package io.github.blvckmind.relic.persistence.model.entity

import jakarta.validation.constraints.Size
import org.springframework.data.annotation.CreatedDate
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "projects")
data class ProjectEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int?,

        @Column(name = "photo_id", length = 64)
        @Size(min = 64, max = 64)
        var photoId: String?,

        @Column(name = "color", length = 6)
        @Size(min = 6, max = 6)
        var color: String?,

        @Column(name = "title", length = 128)
        @Size(max = 128)
        var title: String,

        @Column(name = "description", length = 1024)
        @Size(max = 1024)
        var description: String?,

        @Column(name = "type", length = 64, updatable = false, nullable = false)
        @Enumerated(EnumType.STRING)
        var type: ProjectTypeEnum,

        @Column(name = "list_order", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var order: Short = 0,

        @CreatedDate
        @Column(updatable = false)
        val createdDate: Date?
)