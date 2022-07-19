import kotlin.math.*
import kotlin.time.Duration.Companion.milliseconds

var countOfFunctionCalculation: Int = 0
private const val BASE_LEFT_BORDER = -10.0
private const val BASE_RIGHT_BORDER = 10.0
private const val LEFT_BORDER_INDEX = 0
private const val RIGHT_BORDER_INDEX = 1
private const val BASE_STEP = 1.2
private const val ARGUMENT_ACCURACY = 0.005
private const val FUNCTION_ACCURACY = 0.001

fun main() {

    val startTime = System.currentTimeMillis()
    val intervals = findIntervals()
    printMatrix(intervals)
    calculateRootsViaGoldenRatio(intervals)
    println("Function counted $countOfFunctionCalculation times")
    val spentTime = (System.currentTimeMillis() - startTime).milliseconds
    println("Spent time: $spentTime")
}

//функция согласно заданию
private fun baseFunction(x: Double): Double {
    countOfFunctionCalculation++
    return (10 * cos(x) - 0.1 * x.pow(2))
}

//расчет по методу золотого сечения
private fun calculateRootsViaGoldenRatio(intervals: Array<DoubleArray>) {
    println("Calculating roots using Golden Ratio.\n")
    val answer = mutableListOf<Double>()
    val accuracies = mutableListOf<Double>()
    val functionValue = mutableListOf<Double>()
    for (interval in intervals) {
        val convergences = mutableListOf<Double>()
        val roots = mutableListOf<Double>()
        var a = interval[LEFT_BORDER_INDEX]
        var b = interval[RIGHT_BORDER_INDEX]
        var iteration = 0
        while ((((b - a) / 2).absoluteValue >= ARGUMENT_ACCURACY) ||
            ((baseFunction((b + a) / 2)).absoluteValue >= FUNCTION_ACCURACY)) {
            val d = a + ((b - a) / goldenRatio())
            val c = a + ((b - a) / goldenRatio().pow(2))
            if (baseFunction(a) * baseFunction(d) <= 0) {
                b = d
            } else if (baseFunction(c) * baseFunction(b) < 0) {
                a = c
            }
            roots.add((a + b) / 2)
            if (roots.size >= 3) {
                convergences.add(
                    ((roots[iteration - 1] - roots[iteration]) /
                            (roots[iteration - 2] - roots[iteration - 1])).absoluteValue
                )
            }
            iteration++
        }
        println("Convergences of root №${intervals.indexOf(interval) + 1}:")
        printArray(convergences.toDoubleArray())
        println("Iterations of root №${intervals.indexOf(interval) + 1}: $iteration\n")

        val x = (a + b) / 2
        answer.add(x)
        accuracies.add(((b - a) / 2).absoluteValue)
        functionValue.add(baseFunction(x))
    }
    println("Roots:")
    printArray(answer.toDoubleArray())
    println("Argument accuracy:")
    printArray(accuracies.toDoubleArray())
    println("function value:")
    printArray(functionValue.toDoubleArray())

}

//золотое сечение
private fun goldenRatio(): Double {
    return (1 + sqrt(5.0)) / 2
}


//интервалы, содержащие корни
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

//вывод на экран матрицы
private fun printMatrix(matrix: Array<DoubleArray>) {
    matrix.map { it ->
        val newArray = mutableListOf<Double>()
        it.map {
            newArray.add((it * 1000.0).roundToInt() / 1000.0)
        }
        println("[${newArray.toDoubleArray().joinToString(", ")}]")
    }
}

//вывод  на экран вектора
private fun printArray(array: DoubleArray) {
    val newArray = mutableListOf<Double>()
    array.map {
        newArray.add((it * 1000.0).roundToInt() / 1000.0)
    }
    println("[${newArray.toDoubleArray().joinToString(", ")}]")
}

