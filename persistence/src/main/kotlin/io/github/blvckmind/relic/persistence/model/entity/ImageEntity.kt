package io.github.blvckmind.relic.persistence.model.entity

import jakarta.validation.constraints.NotNull
import javax.persistence.*

@Entity
@Table(name = "images")
data class ImageEntity(
    @Id
    @Column(length = 64)
    @NotNull
    var checksum: String,

    @Lob
    @Column(name = "bytes", columnDefinition = "BLOB", nullable = false)
    @NotNull
    var bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (javaClass != other?.javaClass) return false

        other as ImageEntity

        if (checksum === other.checksum) return true

        if (hashCode() != other.hashCode()) return false

        return checksum == other.checksum
    }

    override fun hashCode() = checksum.hashCode()

}
