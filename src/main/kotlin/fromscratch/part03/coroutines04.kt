package fromscratch.part03

import fromscratch.utils.bigPrime
import fromscratch.utils.logMsg
import fromscratch.utils.setOptionToShowCoroutineNames
import kotlinx.coroutines.*

object Coroutines04 {
    @JvmStatic
    fun main(args: Array<String>) {
        setOptionToShowCoroutineNames()
        runBlocking {
            val job: Job = launch(Dispatchers.Unconfined) {
                launch {
                    launch {
                        delayAndLog()
                    }
                    delayAndLog()
                }
                delayAndLog()
            }
            println("Here is a big prime number: ${bigPrime(2048)}")
            job.cancel()
        }
    }

    private suspend fun delayAndLog() {
        while (true) {
            delay(200)
            logMsg("Hello")
        }
    }
}
