package io.github.blvckmind.relic.model

import java.io.Serializable

class ResponseEntities<T>(
        var payload: List<T>,
        var pageNum: Int,
        var total: Long
) : Serializable