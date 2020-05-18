package com.smeup.kotlin.from.scratch.part02

import java.io.PrintStream

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

fun openDatabase(user: String, password: String): Database? {
    if ("franco" == user && "secret" == password) {
        return sampleDatabase()
    }
    return null
}

private fun sampleDatabase(): Database {
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
    return Database((listOf(verdi, puccini)))
}

fun exactName(name: String): (Composer) -> Boolean = { it.name == name }

fun similarName(name: String): (Composer) -> Boolean = { it.name.contains(name, true) }

fun exactMatchToGiuseppeVerdi(composer: Composer): Boolean = composer.name == "Giuseppe Verdi"

val exactMatchToGiacomoPuccini = exactName("Giacomo Puccini")

val exactMatchToItaly = {
    composer: Composer ->
        composer.nation == "Italy"
}

inline fun Database.findComposerByName(name: String): Composer? =
    findComposerBy(exactName(name))

// inline here could improve performances
inline fun Database.findComposerBy(predicate: (Composer) -> Boolean) =
    this.composers.firstOrNull(predicate)

fun Composer.findOperaByYear(year: Int): Opera? =
    this.operas.firstOrNull { it.yearOfComposition == year }

fun Opera?.displayResult(): Unit =
    if (this == null) {
        println("No result")
    } else {
        println(this)
    }

infix fun Opera?.displayResultTo(printStream: PrintStream): Unit =
    if (this == null) {
        printStream.println("No result")
    } else {
        printStream.println(this)
    }

fun printResultOrNotFoundMessage(opera: Opera?): Unit = println(opera ?: "No results")

fun main() {
    println(
        openDatabase("franco","secret")
        ?.findComposerByName("Giuseppe Verdi")
        ?.findOperaByYear(1853)
        ?: "No results"
    )

    println(
        openDatabase(password = "secret", user = "franco")
        ?.findComposerByName("Giuseppe Verdi")
        ?.findOperaByYear(1853)
        ?: "No results"
    )

    println(
        openDatabase("franco","secret")
        ?.findComposerBy(similarName("verdi"))
        ?.findOperaByYear(1853)
        ?: "No results"
    )

    println(
        openDatabase("franco","secret")
            ?.findComposerBy(::exactMatchToGiuseppeVerdi)
            ?.findOperaByYear(1853)
            ?: "No results"
    )

    println(
        openDatabase("franco","secret")
            ?.findComposerBy(exactMatchToGiacomoPuccini)
            ?.findOperaByYear(1900)
            ?: "No results"
    )

    println(
        openDatabase("franco","secret")
            ?.findComposerBy(exactMatchToItaly)
            ?.findOperaByYear(1853)
            ?: "No results"
    )

    openDatabase("franco","secret")
        ?.findComposerBy(exactMatchToGiacomoPuccini)
        ?.findOperaByYear(1901)
        .displayResult()

    openDatabase("franco","secret")
        ?.findComposerBy(exactMatchToGiacomoPuccini)
        ?.findOperaByYear(1901)
        .displayResult()

    openDatabase("franco","secret")
        ?.findComposerBy(exactMatchToGiacomoPuccini)
        ?.findOperaByYear(1901) displayResultTo System.err

    openDatabase("franco","secret")
        ?.findComposerBy(exactMatchToGiacomoPuccini)
        ?.findOperaByYear(1901)
        .run(::println)

    openDatabase("franco","secret")
        ?.findComposerBy(exactMatchToGiacomoPuccini)
        ?.findOperaByYear(1901)
        .run(::printResultOrNotFoundMessage)

    println(
        openDatabase("franco","secret")
            ?.findComposerBy(exactMatchToGiacomoPuccini)
            ?.operas
            ?.maxBy (Opera::yearOfComposition)
            ?: "No results"
    )

    println(
        openDatabase("franco","secret")
            ?.findComposerBy(exactMatchToGiacomoPuccini)
            ?.operas
            ?.sortedBy (Opera::yearOfComposition)
            ?.map (::OperaHtmlDiv)
            ?: "No results"
    )

    println(
        openDatabase("franco", "secret")
            ?.findComposerBy(exactMatchToGiacomoPuccini)
            ?.run {
                operas
                    .sortedBy(Opera::yearOfComposition)
                    .map(::OperaHtmlDiv)
            }
            ?: "No results"
    )
}

