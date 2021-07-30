package io.github.blvckmind.relic.util

val colors = arrayOf("ed4245", "62D0f6", "faa619", "1cb491")

fun <T : Any> T?.notNull(f: (it: T) -> Unit) {
    if (this != null) f(this)
}
