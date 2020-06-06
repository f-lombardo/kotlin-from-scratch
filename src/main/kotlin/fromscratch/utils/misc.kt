package fromscratch.utils

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
