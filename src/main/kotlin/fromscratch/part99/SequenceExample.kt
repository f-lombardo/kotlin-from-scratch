package fromscratch.part99

import fromscratch.utils.logMsg
import fromscratch.utils.setOptionToShowCoroutineNames
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
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

    fun lucas() = sequence {
        var terms = Pair(2, 1)

        // this sequence is infinite
        while (true) {
            logMsg("Lucas from $terms")
            yield(terms.first)
            terms = Pair(terms.second, terms.first + terms.second)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        setOptionToShowCoroutineNames()
        runBlocking {
            joinAll(
                async { fibonacci().take(100) },
                async { lucas().take(100)}
            )
        }
    }
}
