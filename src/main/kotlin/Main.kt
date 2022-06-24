import kotlin.math.*

var count: Int = 0

fun main() {
    val a = -10.0
    val b = 10.0
    val h = 0.3
    printMatrix(findIntervals(a, b, h))

}

fun baseFunction(x: Double): Double {
    count++
    return (10 * cos(x) - 0.1 * x.pow(2))
}

private fun findIntervals(a: Double, b: Double, h: Double): Array<DoubleArray> {
    println("Finding borders:")
    val answer = mutableListOf<DoubleArray>()
    var leftBorder = a
    var rightBorder = a + h
    while (rightBorder < b) {
        if (baseFunction(leftBorder) * baseFunction(rightBorder) <= 0) {
            answer.add(doubleArrayOf(leftBorder, rightBorder))

        }
        leftBorder = rightBorder
        rightBorder += h
    }
    return answer.toTypedArray()
}


private fun printMatrix(matrix: Array<DoubleArray>) {
    matrix.map {
        println("[${it.joinToString(", ")}]")
    }
}