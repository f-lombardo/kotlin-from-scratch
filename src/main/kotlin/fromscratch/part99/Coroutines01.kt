package fromscratch.part99

import fromscratch.utils.logMsg
import fromscratch.utils.setOptionToShowCoroutineNames
import kotlinx.coroutines.*
import java.math.BigInteger
import java.util.*
import kotlin.system.measureTimeMillis

object Coroutines01 {
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

