import kotlin.math.*

var count : Int = 0

fun main() {

}

fun baseFunction(x: Double) : Double {
    count++
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

private fun findIntervals (function: (Double) -> Double, a: Double, b: Double, h: Double) : Array<DoubleArray> {
    val answer = mutableListOf<DoubleArray>()
    var rightBorder = a
    var leftBorder = a + h
    while (rightBorder < b) {
        if (function(leftBorder)*function(rightBorder) <= 0) {
            answer.add(doubleArrayOf(leftBorder,rightBorder))
        } else {
            leftBorder = rightBorder
            rightBorder += h
        }
    }
    return answer.toTypedArray()
}
