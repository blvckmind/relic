package io.github.blvckmind.relic.persistence.repository

import io.github.blvckmind.relic.persistence.model.entity.ImageEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageEntityRepository : JpaRepository<ImageEntity, String> {
    fun getFirstByChecksum(checksum: String): ImageEntity?
    fun existsByChecksum(checksum: String): Boolean
    fun deleteImageEntityByChecksum(checksum: String)
}


