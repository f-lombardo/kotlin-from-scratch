package fromscratch.part03

import fromscratch.utils.logMsg
import fromscratch.utils.setOptionToShowCoroutineNames
import kotlinx.coroutines.*
import java.math.BigInteger
import java.util.*

object Coroutines02Working {
    @JvmStatic
    fun main(args: Array<String>) {
        setOptionToShowCoroutineNames()
        runBlocking {
            async(CoroutineName("Calandrino")) {
                while (true) {
                    val deferredNumber: Deferred<BigInteger> = async { bigPrime(2048) }
                    logMsg("Bischerata numero ${deferredNumber.await()}")
                }
            }
            async(CoroutineName("Buffalmacco")) {
                while (true) {
                    val deferredNumber: Deferred<BigInteger> = async { bigPrime(2048) }
                    logMsg("Clandrino è bischero ${deferredNumber.await()} volte!")
                }
            }
        }
    }
}

object Coroutines0Not2Working {
    @JvmStatic
    fun main(args: Array<String>) {
        setOptionToShowCoroutineNames()
        runBlocking {
            async(CoroutineName("Calandrino")) {
                while (true) {
                    logMsg("Bischerata numero ${bigPrime(1024)}")
                }
            }
            async(CoroutineName("Buffalmacco")) {
                while (true) {
                    logMsg("Calandrino è bischero ${bigPrime(2048)} volte!")
                }
            }
        }
    }
}

private fun bigPrime(bitLength: Int) = BigInteger.probablePrime(bitLength, Random())
