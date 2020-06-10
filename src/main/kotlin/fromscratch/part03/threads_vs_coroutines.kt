package fromscratch.part03

import fromscratch.utils.humanReadableUsedMemory
import fromscratch.utils.getProcessID
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

//Pass the number of items to create as first parameter

private fun readItemsNrFromParms(args: Array<String>) = if (args.size > 0) args[0].toInt() else 10_000

object MemoryThreadsExample {

    @JvmStatic
    fun main(args: Array<String>) {
        val items = readItemsNrFromParms(args)
        println("Process ID ${getProcessID()} creating $items threads")
        val elapsed = measureTimeMillis {
            (1..items).forEach {
                thread(start = true, name = "MyThread$it") {
                    while (true) Thread.sleep(10_000)
                }
            }
        }
        println("Threads - Elapsed millis: $elapsed - Memory used: ${humanReadableUsedMemory()}")
    }
}

object MemoryCoroutinesExample {
    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        val items = readItemsNrFromParms(args)
        println("Process ID ${getProcessID()} creating $items coroutines")
        val elapsed = measureTimeMillis {
            (1..items).forEach {
                launch {
                    while (true) delay(10_000)
                }
            }
        }
        println("Coroutines - Elapsed millis: $elapsed - Memory used: ${humanReadableUsedMemory()}")
    }
}
