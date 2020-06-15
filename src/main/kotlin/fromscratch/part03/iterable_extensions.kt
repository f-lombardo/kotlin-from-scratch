package fromscratch.part03

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

//See https://jivimberg.io/blog/2018/05/04/parallel-map-in-kotlin/

suspend fun <A, B> Iterable<A>.concurrentMap(f: suspend (A) -> B?): List<B?> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}

suspend fun <A, B> Iterable<A>.parallelMap(context: CoroutineContext = Dispatchers.Default, f: suspend (A) -> B?): List<B?> =
    withContext(context) {
        map { async { f(it) } }.awaitAll()
    }
