package io.github.blvckmind.relic.model.mapper

interface MapperAPI<E, D> {
    fun dto(e: E): D
    fun entity(d: D): E
}
