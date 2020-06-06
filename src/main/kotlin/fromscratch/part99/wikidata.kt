package fromscratch.part99

// See https://github.com/BorderCloud/SPARQL-JAVA

import com.bordercloud.sparql.SparqlClient
import com.bordercloud.sparql.SparqlResult
import fromscratch.utils.tryOrNull
import kotlinx.coroutines.*
import java.net.URI
import kotlin.math.log
import kotlin.system.measureTimeMillis

val endpointUrl = URI("https://query.wikidata.org/sparql")

enum class Language(val code: String) {
    ITALIAN("Q652"),
    FRENCH("Q150"),
    GERMAN("Q188")
}

data class Composer(val code: String, val name: String, val language: Language, val description: String, val yearOfBirth: Int) {
    suspend fun operas(): List<Opera>? {
        logMsg("Finding operas for ${this.name}")
        return """
                SELECT ?opera ?operaLabel ?year
                WHERE {
                  ?opera wdt:P86 wd:${this.code};                                      #P86 composedBy
                         wdt:P571 ?year.                                               #P571 date of composition
                  SERVICE wikibase:label { bd:serviceParam wikibase:language "en". }         
                }   
             """.findEntities { record ->
            Opera(
                record["opera"]!!.toCode(),
                record["operaLabel"].toString(),
                record["year"]!!.toYear(),
                this
            )
        }
    }
}

data class Opera(val code: String, val name: String, val yearOfComposition: Int, val composer: Composer)

suspend fun <T> String.findEntities(constructor: (HashMap<String, Any>) -> T): List<T>? = tryOrNull {
    retrieveData(endpointUrl, this).model.rows.map(constructor)
}

class Database {
    suspend fun findComposerByLanguage(language: Language): List<Composer>? {
        logMsg("Retrieving composers ")
        return """
                SELECT ?composer ?composerLabel ?dateOfBirth ?composerDescription
                WHERE {
                  ?composer wdt:P1412 wd:${language.code};                            #P1412=language
                            wdt:P136 wd:Q1344;                                        #P136=genre   Q1344=Opera
                            wdt:P106 wd:Q36834;                                       #P106=occupation   Q36834=composer
                            wdt:P569 ?dateOfBirth.
                  SERVICE wikibase:label { bd:serviceParam wikibase:language "en". }
                }
                """.findEntities { record ->
            Composer(
                record["composer"]!!.toCode(),
                record["composerLabel"].toString(),
                language,
                record["composerDescription"].toString(),
                record["dateOfBirth"]!!.toYear()
            )
        }
        ?.filter {
                composer -> composer.description.toLowerCase().contains("italian")
        }
    }
}

fun Any.toCode(): String =
    toString().substringAfter("http://www.wikidata.org/entity/")

fun Any.toYear(): Int =
    toString().take(4).toInt()

fun String.firstLine(): String =
    lines().first { it.isNotEmpty() }.trim()

suspend fun retrieveData(endpointUrl: URI, query: String): SparqlResult =
    retreiveParallel(endpointUrl, query)

suspend fun retreiveParallel(endpointUrl: URI, query: String) = withContext(Dispatchers.IO) {
    retrieveDataJob(endpointUrl, query)
}

fun retrieveDataJob(endpointUrl: URI, query: String): SparqlResult {
    logMsg("Retrieving data for '${query.firstLine()}'")
    val sp = SparqlClient(false)
    sp.endpointRead = endpointUrl
    return sp.query(query)
}

fun logMsg(msg: Any?) = println("${threadName()}$msg")

fun threadName() = "[${Thread.currentThread().name}] "

suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B?): List<B?> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}

object CoroutineRunner {
    // To print more information about coroutines run with -Dkotlinx.coroutines.debug
    @JvmStatic
    fun main(args: Array<String>) {
        val millis = measureTimeMillis {
            runBlocking {
                logMsg("Starting")
                val barJOb = launch(Dispatchers.Unconfined) {
                    ConsoleUI.runningBar(::threadName)
                }

                val operas = Database()
                .findComposerByLanguage(Language.ITALIAN)
                ?.filter { composer -> composer.yearOfBirth in (1810..1860) }
                ?.pmap {
                    it.operas()?.filter {
                        it.yearOfComposition in (1900..1910)
                    }
                }
                ?.filterNotNull()
                ?.flatten()

                logMsg("${operas?.size ?: 0} results found")
                operas?.forEach(::logMsg)

                barJOb.cancel()
            }
        }
        logMsg("Done in $millis milliseconds")
    }
}

