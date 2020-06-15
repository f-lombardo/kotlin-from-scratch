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

    private suspend fun bigIntegerResult(): BigInteger {
        Dispatchers.Default {

        }
        val deferred1: Deferred<BigInteger> =
            CoroutineScope( Dispatchers.Unconfined + CoroutineName("Calandrino")).async {
                bigPrime(1024)
            }
        val deferred2: Deferred<BigInteger> =
            CoroutineScope(Dispatchers.Unconfined + CoroutineName("Buffalmacco")).async {
                bigPrime(1024)
            }
        return deferred1.await() + deferred2.await()
    }
}
