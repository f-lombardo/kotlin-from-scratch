package fromscratch.part99

import fromscratch.utils.logMsg
import java.math.BigInteger
import java.util.*
import kotlin.concurrent.thread

object ThreadsExample {
    @JvmStatic
    fun main(args: Array<String>) {
        thread(start = true, name = "Calandrino", isDaemon = true) {
            while(true) {
                logMsg("So' bischero!")
                BigInteger.probablePrime(3072, Random())
            }
        }
        Thread {
            while(true) {
                logMsg("Io pitto!")
                BigInteger.probablePrime(3072, Random())
            }
        }.apply { name = "Buffalmacco" }.start()
    }
}
