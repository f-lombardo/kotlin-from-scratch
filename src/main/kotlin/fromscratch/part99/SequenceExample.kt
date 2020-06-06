package fromscratch.part99

import fromscratch.utils.logMsg
import fromscratch.utils.setOptionToShowCoroutineNames
import kotlinx.coroutines.runBlocking

object SequenceExample {
    fun fibonacci() = sequence {
        var terms = Pair(0, 1)

        // this sequence is infinite
        while (true) {
            logMsg("Fibonacci from $terms")
            yield(terms.first)
            terms = Pair(terms.second, terms.first + terms.second)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        setOptionToShowCoroutineNames()
        runBlocking {
            val uiJob =startUIJob()
            logMsg(fibonacci().take(400).toList())
            uiJob.cancel()
        }
    }
}
