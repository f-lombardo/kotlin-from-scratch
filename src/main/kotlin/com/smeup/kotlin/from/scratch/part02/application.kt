package com.smeup.kotlin.from.scratch.part02

data class Database(val composers: List<Composer>)
data class Composer(
    val name: String,
    val nation: String,
    val operas: List<Opera>
)

data class Opera(val name: String, val yearOfComposition: Int)

fun openDatabase(user: String, password: String): Database? =
    if ("franco" == user && "secret" == password) {
        val verdi =
            Composer("Giuseppe Verdi", "Italy",
                listOf(Opera("Aida",1871),
                       Opera("La traviata",1853)))
        val puccini =
            Composer("Giacomo Puccini", "Italy",
                listOf(Opera("Turandot",1926),
                       Opera("Tosca",1900)))
        Database((listOf(verdi, puccini)))
    } else {
        null
    }

fun exactName(name: String): (Composer) -> Boolean = { it.name == name }

fun similarName(name: String): (Composer) -> Boolean = { it.name.contains(name, true) }

fun exactMatchToGiuseppeVerdi(composer: Composer): Boolean = composer.name == "Giuseppe Verdi"

val exactMatchToGiacomoPuccini = exactName("Giacomo Puccini")

val exactMatchToItaly = {
    composer: Composer ->
        composer.nation == "Italy"
}

fun Database.findComposerByName(name: String): Composer? {
    return findComposerBy(exactName(name))
}

private fun Database.findComposerBy(predicate: (Composer) -> Boolean) =
    this.composers.firstOrNull(predicate)

fun Composer.findOperaByYear(year: Int): Opera? =
    this.operas.firstOrNull { it.yearOfComposition == year }

fun Opera?.displayResult(): Unit =
    if (this == null) {
        println("No result")
    } else {
        println(this)
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
        ?.findOperaByYear(1901)
        .run(::println)

    openDatabase("franco","secret")
        ?.findComposerBy(exactMatchToGiacomoPuccini)
        ?.findOperaByYear(1901)
        .run(::printResultOrNotFoundMessage)
}

