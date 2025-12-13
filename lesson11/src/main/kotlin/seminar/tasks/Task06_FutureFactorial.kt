package seminar.tasks

import java.math.BigInteger
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Задание 6. Future
 *
 * Используя ExecutorService и Callable, параллельно вычислите факториалы чисел от 1 до 10.
 * Соберите результаты через Future.get().
 */
object FutureFactorial {

    fun factorial(n: Int): BigInteger {
        if (n < 1) {
            return BigInteger.ONE
        }
        var result = 1.toBigInteger()
        for (i in 2..n) {
            result*=i.toBigInteger()
        }
        return result
    }
    /**
     * @return Map<Int, BigInteger> где ключ - число, значение - его факториал
     */
    fun run(): Map<Int, BigInteger> {
        val executorService = Executors.newFixedThreadPool(4)
        val future = (1..10).associateWith { executorService.submit(Callable {factorial(it)}) }

        val results = future.mapValues { it.value.get() }
        executorService.shutdown()
        executorService.awaitTermination(5, TimeUnit.SECONDS)
        return results
    }
}
