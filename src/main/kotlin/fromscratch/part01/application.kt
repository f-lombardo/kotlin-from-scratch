package fromscratch.part01

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
        Database((listOf(verdi, puccini)))
    } else {
        null
    }

fun Database.findComposerByName(name: String): Composer? =
    this.composers.firstOrNull { it.name == name }

fun Composer.findOperaByYear(year: Int): Opera? =
    this.operas.firstOrNull { it.yearOfComposition == year }

fun main() {
    val result =
        openDatabase("franco", "secret")
        ?.findComposerByName("Giuseppe Verdi")
        ?.findOperaByYear(1853)
        ?: "No results"
    println(result)
}
