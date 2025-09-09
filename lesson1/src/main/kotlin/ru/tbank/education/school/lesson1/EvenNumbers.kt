package ru.tbank.education.school.lesson1

/**
 * Сумма четных чисел.s112341432
 */
fun sumEvenNumbers(numbers: Array<Int>): Int {
    var u = 0
    for (i in numbers) {
        if (i%2==0) {
            u += i
        }
    }
    return u
}