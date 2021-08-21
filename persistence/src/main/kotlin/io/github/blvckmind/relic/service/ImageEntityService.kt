package io.github.blvckmind.relic.service

import io.github.blvckmind.relic.domain.entity.ImageEntity
import io.github.blvckmind.relic.repository.ImageEntityRepository
import org.springframework.stereotype.Service

@Service
class ImageEntityService(private val imageEntityRepository: ImageEntityRepository) {

    fun getFirstByChecksum(checksum: String): ImageEntity? = imageEntityRepository.getFirstByChecksum(checksum)

    fun existsByChecksum(checksum: String): Boolean = imageEntityRepository.existsByChecksum(checksum)

    fun deleteByChecksum(checksum: String) = imageEntityRepository.deleteImageEntityByChecksum(checksum)

}