package ru.tbank.education.school.lesson1

/**
 * Метод для вычисления простых арифметических операций.1234123
 */
fun calculate(a: Double?, b: Double?, operation: OperationType = OperationType.ADD): Double? {

    a?.let {
        b?.let{
            when (operation) {
                OperationType.ADD -> {
                    return a+b
                }
                OperationType.SUBTRACT -> {
                    return a-b
                }
                OperationType.DIVIDE -> {
                    return if (b==0.0) {
                        null
                    } else{
                        a/b
                    }
                }
                OperationType.MULTIPLY -> {
                    return a*b
                }
            }
        }
        b?: {
            println("Error")
        }
    }
    a?: {
        println("Error")
    }
    return null
}

/**
 * Функция вычисления выражения, представленного строкой
 * @return результат вычисления строки или null, если вычисление невозможно
 * @sample "5 * 2".calculate()
 */
@Suppress("ReturnCount")
fun String.calculate(): Double? {
    TODO()
}
