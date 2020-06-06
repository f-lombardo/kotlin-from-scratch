package fromscratch.part99

import kotlinx.coroutines.*
import java.math.BigInteger
import java.util.*
import kotlin.system.measureTimeMillis

fun findBigPrimeBlocking(): BigInteger {
    println("Running in ${Thread.currentThread().name}")
    return BigInteger.probablePrime(4096, Random())
}

suspend fun findBigPrime(): BigInteger =
    withContext(Dispatchers.Default) {
        findBigPrimeBlocking()
    }

suspend fun threadSwitchingCoroutine(number: Int, delay: Long) {
    println("Coroutine $number starts work on ${Thread.currentThread().name}")
    delay(delay)
    withContext(Dispatchers.IO) {
        println("Coroutine $number has finished on ${Thread.currentThread().name}")
    }
}

object Coroutines01 {
    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        println("Starting...")
        joinAll(
            async { threadSwitchingCoroutine(1, 500) },
            async { threadSwitchingCoroutine(2, 300) }
        )
        println("main ends")

        val sequence = sequence {
            val start = 0
            // yielding a single value
            yield(start)
            // yielding an iterable
            yieldAll(1..5 step 2)
            // yielding an infinite sequence
            yieldAll(generateSequence(8) { it * 3 })
        }
    }
}

