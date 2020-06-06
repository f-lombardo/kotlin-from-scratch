package fromscratch.part99

// See https://github.com/BorderCloud/SPARQL-JAVA

import com.bordercloud.sparql.SparqlClient
import com.bordercloud.sparql.SparqlResult
import fromscratch.utils.tryOrNull
import kotlinx.coroutines.*
import java.net.URI

val endpointUrl = URI("https://query.wikidata.org/sparql")

enum class Language(val code: String) {
    ITALIAN("Q652"),
    FRENCH("Q150"),
    GERMAN("Q188")
}

data class Composer(val code: String, val name: String, val language: Language) {
    suspend fun operas(): List<Opera>? =
        tryOrNull {
            logMsg("Finding operas for ${this.name}")
            val querySelect = """
        SELECT ?opera ?operaLabel ?year
        WHERE
        {
          ?opera wdt:P86 wd:${this.code};
                 wdt:P571 ?year.
          SERVICE wikibase:label { bd:serviceParam wikibase:language "en". }         
        }
    """
            val data = retrieveData(
                endpointUrl,
                querySelect
            ).model
            data.rows.map { record ->
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

class Database {
    suspend fun findComposerByLanguage(language: Language): List<Composer>? =
        tryOrNull {
            val querySelect = """
            SELECT ?composer ?composerLabel ?composerDescription
            WHERE
            {
              #P1412=language   
              ?composer wdt:P1412 wd:${language.code};
                        #P136=genre   Q1344=Opera
                        wdt:P136 wd:Q1344;
                        #P106=occupation   Q36834=composer
                        wdt:P106 wd:Q36834.  
              SERVICE wikibase:label { bd:serviceParam wikibase:language "en". }
            }
        """
            logMsg("Retrieving composers ")
            val data = retrieveData(
                endpointUrl,
                querySelect
            ).model
            logMsg("Composers retreived")
            data.rows.map { record ->
                Composer(
                    record["composer"]!!.toCode(),
                    record["composerLabel"].toString(),
                    language
                )
            }
        }
}

fun Any.toCode(): String =
    toString().substringAfter("http://www.wikidata.org/entity/")

fun Any.toYear(): Int =
    toString().take(4).toInt()

fun String.firstLine(): String =
    lines().first { it.isNotEmpty() }.trim()

suspend fun retrieveData(endpointUrl: URI, query: String): SparqlResult = withContext(Dispatchers.IO) {
    logMsg("Retrieving data for '${query.firstLine()}'")
    val sp = SparqlClient(false)
    sp.endpointRead = endpointUrl
    sp.query(query)
}

fun logMsg(msg: Any) {
    println("${threadName()}$msg")
}

fun threadName() = "[${Thread.currentThread().name}] "

object CouroutineRunner {
    // To print more information about coroutines run with -Dkotlinx.coroutines.debug
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            logMsg("Starting")
            val barJOb = launch(Dispatchers.Unconfined) {
                ConsoleUI.runningBar(::threadName)
            }

            val italianOperasOfStartingOf20thCentury =
                Database()
                    .findComposerByLanguage(Language.ITALIAN)?.mapNotNull {
                it.operas()?.filter {
                    it.yearOfComposition in (1900..1910)
                }
            }
            ?.flatten()
            ?.forEach(::logMsg)

            barJOb.cancel()
        }
        logMsg("Done!")
    }
}

