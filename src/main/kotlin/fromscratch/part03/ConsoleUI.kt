package fromscratch.part03

import fromscratch.utils.logMsg
import fromscratch.utils.threadName
import kotlinx.coroutines.*

class ConsoleProgressBar {
    private val res = StringBuilder()

    private fun progress(pct: Int, barLength: Int): String? {
        res.delete(0, res.length)
        val numPounds = pct * barLength / 100
        for (i in 0 until numPounds) {
            res.append('#')
        }
        while (res.length != barLength) {
            res.append(' ')
        }
        return res.toString()
    }

    private fun progressBar(percentage: Int, showPercentage: Boolean = true, prefix: () -> String) {
        if (showPercentage) {
            print(String.format("${prefix()}[%s]%d%%\r", progress(percentage, 74), percentage))
        } else {
            print(String.format("${prefix()}[%s]\r", progress(percentage, 74)))
        }
    }

    private fun emptyString() = ""

    suspend fun runningBar(prefix: () -> String = ::emptyString) {
        while (true) {
            for (i in 0..100) {
                delay(20)
                progressBar(i, false, prefix)
            }
        }
    }

    suspend fun showContinuously() = runningBar(::threadName)
}

fun CoroutineScope.startUIJob(): Job {
    logMsg("Starting UI")
    return launch(Dispatchers.Unconfined) {
        ConsoleProgressBar().showContinuously()
    }
}

object ConsoleUI {
    @Throws(Exception::class)
    @JvmStatic
    // This is just a simple application to show how the running bar looks like
    fun main(args: Array<String>) = runBlocking {
        ConsoleProgressBar().runningBar()
    }
}


