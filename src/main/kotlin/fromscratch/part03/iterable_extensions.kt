package fromscratch.part03

import kotlinx.coroutines.*

suspend fun <A, B> Iterable<A>.concurrentMap(f: suspend (A) -> B?): List<B?> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}

suspend fun <A, B> Iterable<A>.parallelMap(f: suspend (A) -> B?): List<B?> = withContext(Dispatchers.Default) {
    map { async { f(it) } }.awaitAll()
}
