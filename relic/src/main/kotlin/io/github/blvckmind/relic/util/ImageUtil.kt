package io.github.blvckmind.relic.util

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.IOException


//@kotlin.jvm.Throws(IOException::class)
fun resizeImage(originalImage: BufferedImage?, targetWidth: Int, targetHeight: Int): BufferedImage? {
    val resizedImage = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB)
    val graphics2D = resizedImage.createGraphics()
    graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null)
    graphics2D.dispose()
    return resizedImage
}

//@kotlin.jvm.Throws(IOException::class)
fun resizeImage2(originalImage: BufferedImage, targetWidth: Int, targetHeight: Int): BufferedImage? {
    val resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT)
    val outputImage = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB)
    outputImage.graphics.drawImage(resultingImage, 0, 0, null)
    return outputImage
}

