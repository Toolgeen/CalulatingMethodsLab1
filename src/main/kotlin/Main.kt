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
private const val VALUE_ONE = 1.0

fun main() {

    var leftBorder: Double = BASE_LEFT_BORDER
    var rightBorder: Double = BASE_RIGHT_BORDER
    var step: Double = BASE_STEP
    var argumentAccuracy: Double = ARGUMENT_ACCURACY
    var functionAccuracy: Double = FUNCTION_ACCURACY

    if (isInputNeeded()) {
        val borders = insertBorders()
        leftBorder = borders[LEFT_BORDER_INDEX]
        rightBorder = borders[RIGHT_BORDER_INDEX]
        step = insertStep(leftBorder, rightBorder)
        println("Введите точность по аргументу")
        argumentAccuracy = insertAccuracy()
        println("Введите точность по функции")
        functionAccuracy = insertAccuracy()
    }

    val startTime = System.currentTimeMillis()
    val intervals = findIntervals(leftBorder, rightBorder, step)
    printMatrix(intervals)
    calculateRootsViaGoldenRatio(intervals, argumentAccuracy, functionAccuracy)
    println("Function counted $countOfFunctionCalculation times")
    val spentTime = (System.currentTimeMillis() - startTime).milliseconds
    println("Spent time: $spentTime")
}

//ввод границ интервала
private fun insertBorders(): DoubleArray {
    println("Введите левую границу интервала:")
    var left = readLine()
    println("Введите правую границу интервалаЖ")
    var right = readLine()
    while (!(validateInsert(left) && validateInsert(right))) {
        println("Введены неверные данные, введите левую границу интервала еще раз:")
        left = readLine()
        println("Введите правую границу интервала:")
        right = readLine()
    }
    while (left!!.toDouble() > +right!!.toDouble()) {
        println("Левая граница интервала не может иметь значение больше правой.")
        insertBorders()
    }
    return doubleArrayOf(left.toDouble(), right.toDouble())
}

//ввод шага интервала
private fun insertStep(leftBorder: Double, rightBorder: Double): Double {
    println("Введите шаг интервала:")
    var step = readLine()
    while (!(validateInsert(step))) {
        println("Введены неверные данные, введите значение шага еще раз:")
        step = readLine()
    }
    while (step!!.toDouble() > (rightBorder - leftBorder)) {
        println("Шаг интервала не может быть больше или равен длине самого интервала.")
        insertStep(leftBorder, rightBorder)
    }
    return step.toDouble()
}

//ввод точности
private fun insertAccuracy() : Double {
    var accuracy = readLine()
    while (!(validateInsert(accuracy))) {
        println("Введены неверные данные, введите значение точности еще раз:")
        accuracy = readLine()
    }

    return accuracy!!.toDouble()
}

//требуется ли ввод условий с клавиатуры
private fun isInputNeeded(): Boolean {
    println(
        "Входные данные по умолчанию: \n" +
                "Левая граница интервала: $BASE_LEFT_BORDER \n" +
                "Правая граница интервала: $BASE_RIGHT_BORDER \n" +
                "Шаг интервала: $BASE_STEP \n" +
                "Точность по аргументу: $ARGUMENT_ACCURACY \n" +
                "Точность по функции: $FUNCTION_ACCURACY"
    )
    println("Хотите ли ввести свои входные данные для выполнения?")
    println("Введите ${VALUE_ONE.toInt()}, если да, и любое другое число, если нет.")
    var result = readLine()
    while (!(validateInsert(result))) {
        println("Неизвестный вариант, пожалуйста, введите ${VALUE_ONE.toInt()}, если да, и любое другое число, если нет.")
        result = readLine()
    }
    return when (result?.toInt()) {
        VALUE_ONE.toInt() -> true
        else -> false
    }
}

//проверка ввода
private fun validateInsert(insert: String?): Boolean {
    return if (insert == null) {
        false
    } else {
        if (insert.trim().toDoubleOrNull() is Double) {
            return true
        } else false
    }
}

//функция согласно заданию
private fun baseFunction(x: Double): Double {
    countOfFunctionCalculation++
    return (10 * cos(x) - 0.1 * x.pow(2))
}

//расчет по методу золотого сечения
private fun calculateRootsViaGoldenRatio(intervals: Array<DoubleArray>, argAccuracy: Double, funAccuracy: Double) {
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
        while ((((b - a) / 2).absoluteValue >= argAccuracy) ||
            ((baseFunction((b + a) / 2)).absoluteValue >= funAccuracy)
        ) {
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
private fun findIntervals(left: Double, right: Double, step: Double): Array<DoubleArray> {
    println("Finding borders:")
    val answer = mutableListOf<DoubleArray>()
    var leftBorder = left
    var rightBorder = left + step
    while (rightBorder < right) {
        if (baseFunction(leftBorder) * baseFunction(rightBorder) <= 0) {
            answer.add(doubleArrayOf(leftBorder, rightBorder))

        }
        leftBorder = rightBorder
        rightBorder += step
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

