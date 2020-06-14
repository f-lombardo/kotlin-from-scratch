package fromscratch.part03

// See https://github.com/BorderCloud/SPARQL-JAVA
// To test your queries use https://query.wikidata.org/

import fromscratch.utils.logMsg
import fromscratch.utils.setOptionToShowCoroutineNames
import fromscratch.utils.tryOrNull
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

data class Composer(val code: String, val name: String, val language: Language, val description: String, val yearOfBirth: Int)
data class Opera(val code: String, val name: String, val yearOfComposition: Int, val composer: Composer)

suspend fun findComposerByLanguage(language: Language): List<Composer>? {
    logMsg("Retrieving composers ")
    val composers =
        """
            SELECT ?composer ?composerLabel ?dateOfBirth ?composerDescription
            WHERE {
              ?composer wdt:P1412 wd:${language.code};                            #P1412=language
                        wdt:P136 wd:Q1344;                                        #P136=genre   Q1344=Opera
                        wdt:P106 wd:Q36834;                                       #P106=occupation   Q36834=composer
                        wdt:P569 ?dateOfBirth.
              SERVICE wikibase:label { bd:serviceParam wikibase:language "en". }
            }
            """
            .findEntities { record ->
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
    logMsg("Composers retrieved")
    return composers
}

enum class Language(val code: String) {
    ITALIAN("Q652"),
    FRENCH("Q150"),
    GERMAN("Q188")
}

suspend fun Composer.operas(): List<Opera>? {
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

suspend fun <T> String.findEntities(constructor: (Map<String, Any>) -> T): List<T>? = tryOrNull {
    retrieveFromWikidata(this).model.rows.map(constructor)
//  parallelRetrieve(this).model.rows.map(constructor)
}

suspend fun parallelRetrieve(query: String) = withContext(Dispatchers.IO) {
    retrieveFromWikidata(query)
}

suspend fun <A, B> Iterable<A>.concurrentMap(f: suspend (A) -> B?): List<B?> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}

suspend fun <A, B> Iterable<A>.parallelMap(f: suspend (A) -> B?): List<B?> = withContext(Dispatchers.Default) {
    map { async { f(it) } }.awaitAll()
}

object CoroutineRunner {
    @JvmStatic
    fun main(args: Array<String>) {
        setOptionToShowCoroutineNames()
        logMsg("Program started")
        val millis = measureTimeMillis {
            runBlocking {
                val uiJob = startUIJob()

                val operas = findComposerByLanguage(Language.ITALIAN)
                ?.filter { composer -> composer.yearOfBirth in (1810..1860) }
                ?.map { composer ->
                    composer.operas()?.filter { opera -> opera.yearOfComposition in (1900..1910) }
                }
                ?.filterNotNull()
                ?.flatten()

                logMsg("${operas?.size ?: 0} results found")
                operas?.forEach(::logMsg)

                uiJob.cancel()
            }
        }
        logMsg("Done in $millis milliseconds")
    }

}

