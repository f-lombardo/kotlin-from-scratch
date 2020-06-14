package fromscratch.part03

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

suspend fun <A, B> Iterable<A>.concurrentMap(f: suspend (A) -> B?): List<B?> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}

suspend fun <A, B> Iterable<A>.parallelMap(context: CoroutineContext = Dispatchers.Default, f: suspend (A) -> B?): List<B?> =
    withContext(context) {
        map { async { f(it) } }.awaitAll()
    }
