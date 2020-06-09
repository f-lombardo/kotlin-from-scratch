package fromscratch.part03

import fromscratch.utils.humanReadableUsedMemory
import fromscratch.utils.getProcessID
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

object MemoryThreadsExample {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Process ${getProcessID()}")
        val elapsed = measureTimeMillis {
            (1..50_000).forEach {
                thread(start = true, name = "MyThread$it") {
                    while (true) Thread.sleep(100)
                }
            }
        }
        println("Threads - Elapsed millis: $elapsed - Memory used: ${humanReadableUsedMemory()}")
    }
}

object MemoryCoroutinesExample {
    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        println("Process ${getProcessID()}")
        val elapsed = measureTimeMillis {
            (1..50_000).forEach {
                launch {
                    while (true) delay(100)
                }
            }
        }
        println("Coroutines - Elapsed millis: $elapsed - Memory used: ${humanReadableUsedMemory()}")
    }
}
