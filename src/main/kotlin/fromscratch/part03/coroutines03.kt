package fromscratch.part03

import fromscratch.utils.bigPrime
import kotlinx.coroutines.*
import java.math.BigInteger
import java.util.concurrent.Executors

object Coroutines03 {
    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        val job: Job = launch(Dispatchers.Unconfined) {
            ConsoleProgressBar().showContinuously()
        }
        println("Here is a big prime number: ${bigPrime(2048)}")
        job.cancel()
    }
}
