package io.github.blvckmind.relic.model.project_dto

import io.github.blvckmind.relic.persistence.model.entity.ProjectTypeEnum

open class ProjectDto : Comparator<ProjectDto> {
    var title: String? = null
    var type: ProjectTypeEnum? = null
    var order: Short = 0
    var photo: String? = null
    var color: String? = null

    override fun compare(o1: ProjectDto?, o2: ProjectDto?): Int {
        return (o1?.order?.toInt() ?: 0).compareTo(o2?.order?.toInt() ?: 0)
    }
}