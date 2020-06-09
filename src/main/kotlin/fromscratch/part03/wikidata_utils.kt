package fromscratch.part03

import com.bordercloud.sparql.SparqlClient
import com.bordercloud.sparql.SparqlResult
import fromscratch.utils.firstLine
import fromscratch.utils.logMsg
import java.net.URI

val endpointUrl = URI("https://query.wikidata.org/sparql")

fun retrieveFromWikidata(query: String): SparqlResult {
    logMsg("Retrieving data for '${query.firstLine()}'")
    val sp = SparqlClient(false)
    sp.endpointRead = endpointUrl
    return sp.query(query)
}

fun Any.toCode(): String =
    toString().substringAfter("http://www.wikidata.org/entity/")

fun Any.toYear(): Int =
    toString().take(4).toInt()
