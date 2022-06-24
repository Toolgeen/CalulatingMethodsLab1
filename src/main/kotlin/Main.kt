import kotlin.math.*

fun main() {

}

fun baseFunction(x: Double) : Double {
    return (10 * cos(x) - 0.1 * x.pow(2))
}

private fun validateInsert(insert: String?): Boolean {
    return if (insert == null) {
        false
    } else {
        if (insert.trim().toDoubleOrNull() is Double) {
            insert.trim().toDouble() >= 0
        } else false
    }
}