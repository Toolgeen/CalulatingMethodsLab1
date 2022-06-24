import kotlin.math.*

var count: Int = 0
private const val BASE_LEFT_BORDER = -10.0
val BASE_RIGHT_BORDER = 10.0
val BASE_STEP = 0.3

fun main() {

    printMatrix(findIntervals())

}

fun baseFunction(x: Double): Double {
    count++
    return (10 * cos(x) - 0.1 * x.pow(2))
}

private fun findIntervals(): Array<DoubleArray> {
    println("Finding borders:")
    val answer = mutableListOf<DoubleArray>()
    var leftBorder = BASE_LEFT_BORDER
    var rightBorder = BASE_LEFT_BORDER + BASE_STEP
    while (rightBorder < BASE_RIGHT_BORDER) {
        if (baseFunction(leftBorder) * baseFunction(rightBorder) <= 0) {
            answer.add(doubleArrayOf(leftBorder, rightBorder))

        }
        leftBorder = rightBorder
        rightBorder += BASE_STEP
    }
    return answer.toTypedArray()
}

private fun printMatrix(matrix: Array<DoubleArray>) {
    matrix.map {
        println("[${it.joinToString(", ")}]")
    }
}

