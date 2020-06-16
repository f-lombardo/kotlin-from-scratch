package fromscratch.part03

import fromscratch.utils.logMsg
import fromscratch.utils.setOptionToShowCoroutineNames
import kotlinx.coroutines.*
import java.math.BigInteger
import java.util.*

object Coroutines01NotWorking {
    // This example doesn't work as expected
    @JvmStatic
    fun main(args: Array<String>) {
        setOptionToShowCoroutineNames()
        runBlocking {
            async(CoroutineName("Calandrino")) {
                while (true) {
                    logMsg("So' bischero!")
                    BigInteger.probablePrime(1024, Random())
                }
            }
            async(CoroutineName("Buffalmacco")) {
                while (true) {
                    logMsg("Io pitto!")
                    BigInteger.probablePrime(1024, Random())
                }
            }
        }
    }
}

object Coroutines01Working {
    @JvmStatic
    fun main(args: Array<String>) {
        setOptionToShowCoroutineNames()
        runBlocking {
            async(CoroutineName("Calandrino")) {
                while (true) {
                    logMsg("So' bischero!")
                    BigInteger.probablePrime(1024, Random())
                    yield()
                }
            }
            async(CoroutineName("Buffalmacco")) {
                while (true) {
                    logMsg("Io pitto!")
                    BigInteger.probablePrime(1024, Random())
                    yield()
                }
            }
        }
    }
}
