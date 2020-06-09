package fromscratch.utils

import java.text.CharacterIterator
import java.text.StringCharacterIterator
import java.lang.Long.signum
import java.lang.management.ManagementFactory

inline fun <T> tryOrNull(f: () -> T) =
    try {
        f()
    } catch (_: Exception) {
        null
    }

fun logMsg(msg: Any?) = println("${threadName()}$msg")

fun threadName() = "[${Thread.currentThread().name}] "

fun String.firstLine(): String =
    lines().first { it.isNotEmpty() }.trim()

fun setOptionToShowCoroutineNames() {
    System.setProperty("kotlinx.coroutines.debug", "")
}

// See https://programming.guide/java/formatting-byte-size-to-human-readable-format.html
fun humanReadableByteCountBin(bytes: Long): String {
    val absB = if (bytes == Long.MIN_VALUE) Long.MAX_VALUE else Math.abs(bytes)
    if (absB < 1024) {
        return "$bytes B"
    }
    var value = absB
    val ci: CharacterIterator = StringCharacterIterator("KMGTPE")
    var i = 40
    while (i >= 0 && absB > 0xfffccccccccccccL shr i) {
        value = value shr 10
        ci.next()
        i -= 10
    }
    value *= signum(bytes)
    return String.format("%.1f %ciB", value / 1024.0, ci.current())
}

fun usedMemory() = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()

fun humanReadableUsedMemory() = humanReadableByteCountBin(usedMemory())

fun getProcessID() = ManagementFactory.getRuntimeMXBean().getName()
