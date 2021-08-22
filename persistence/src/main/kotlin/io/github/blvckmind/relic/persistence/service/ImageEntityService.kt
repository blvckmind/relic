package io.github.blvckmind.relic.persistence.service

import io.github.blvckmind.relic.persistence.model.entity.ImageEntity
import io.github.blvckmind.relic.persistence.repository.ImageEntityRepository
import org.springframework.stereotype.Service

@Service
class ImageEntityService(private val imageEntityRepository: ImageEntityRepository) {

    fun getFirstByChecksum(checksum: String): ImageEntity? = imageEntityRepository.getFirstByChecksum(checksum)

    fun existsByChecksum(checksum: String): Boolean = imageEntityRepository.existsByChecksum(checksum)

    fun deleteByChecksum(checksum: String) = imageEntityRepository.deleteImageEntityByChecksum(checksum)

}