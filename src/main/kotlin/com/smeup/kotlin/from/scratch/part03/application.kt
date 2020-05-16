package com.smeup.kotlin.from.scratch.part03
import com.github.michaelbull.result.*

/**
 * Examples using https://github.com/michaelbull/kotlin-result
 */

data class Database(val composers: List<Composer>)
data class Composer(
    val name: String,
    val nation: String,
    val operas: List<Opera>
)
data class Opera(val name: String, val yearOfComposition: Int)

class OperaHtmlDiv(val opera: Opera) {
    override fun toString(): String =
        "<div><b>${opera.name}</b> ${opera.yearOfComposition}</div>"
}

fun openDatabase(user: String, password: String): Result<Database, String> {
    if ("franco" == user && "secret" == password) {
        return sampleDatabase()
    }
    return Err("Wrong user or password")
}

private fun sampleDatabase(): Result<Database, String> {
    val verdi =
        Composer(
            "Giuseppe Verdi", "Italy",
            listOf(
                Opera("Aida", 1871),
                Opera("La traviata", 1853)
            )
        )
    val puccini =
        Composer(
            "Giacomo Puccini", "Italy",
            listOf(
                Opera("Turandot", 1926),
                Opera("Tosca", 1900)
            )
        )
    return Ok(Database((listOf(verdi, puccini))))
}

fun exactName(name: String): (Composer) -> Boolean = { it.name == name }

fun similarName(name: String): (Composer) -> Boolean = { it.name.contains(name, true) }

fun exactMatchToGiuseppeVerdi(composer: Composer): Boolean = composer.name == "Giuseppe Verdi"

val exactMatchToGiacomoPuccini = exactName("Giacomo Puccini")

fun Database.findComposerByName(name: String): Result<Composer, String> =
    findComposerBy(exactName(name))

private fun Database.findComposerBy(predicate: (Composer) -> Boolean) =
    this.composers
        .firstOrNull(predicate)
        .toResultOr { "No composer found" }

fun Composer.findOperaByYear(year: Int): Result<Opera, String> =
    this.operas
        .firstOrNull { it.yearOfComposition == year }
        .toResultOr { "No opera found" }

fun main() {
    println(
        openDatabase("franco","secret")
            .andThen { it.findComposerByName("Giuseppe Verdi") }
            .andThen { it.findOperaByYear(1853) }
    )

    println(
        openDatabase("franco","secret")
            .andThen { it.findComposerBy(similarName("verdi")) }
            .andThen { it.findOperaByYear(1853) }
    )

    println(
        openDatabase("franco","secret")
            .andThen { it.findComposerBy(::exactMatchToGiuseppeVerdi) }
            .andThen { it.findOperaByYear(1853) }
    )

    openDatabase("franco","secret")
        .andThen { it.findComposerBy(exactMatchToGiacomoPuccini) }
        .andThen { it.findOperaByYear(1931) }
        .run(::println)

    openDatabase("franco","secret")
        .andThen { it.findComposerBy(exactMatchToGiacomoPuccini) }
        .andThen { it.findOperaByYear(1931) }
        .mapBoth(::println, ::println)

    println(
        openDatabase("franco","secret")
            .andThen { it.findComposerBy(exactMatchToGiacomoPuccini) }
            .map {
                it.operas
                    .maxBy(Opera::yearOfComposition)
            }
    )

    println(
        openDatabase("franco","secret")
            .andThen { it.findComposerBy(exactMatchToGiacomoPuccini) }
            .map {
                it.operas
                    .sortedBy(Opera::yearOfComposition)
                    .map(::OperaHtmlDiv)
            }
    )

}
