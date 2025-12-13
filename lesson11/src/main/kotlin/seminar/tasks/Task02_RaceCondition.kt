package seminar.tasks

import kotlin.random.Random

/**
 * Задание 2. Race condition
 *
 * Создайте переменную counter = 0.
 * Запустите 10 потоков, каждый из которых увеличивает counter на 1000.
 * Выведите финальное значение и объясните результат.
 */
object RaceCondition {

    /**
     * @return финальное значение counter (может быть меньше 10000 из-за race condition)
     */
    fun run(): Int {
        var counter = 0
        val threads = mutableListOf<Thread>()
        val lock = Any()



        for(i in 1..10) {
            threads.add(Thread(
                {
                    repeat(1000) {
                        synchronized(lock) {
                            counter++
                        }
                    }
                }, "Thread-$i"
            ))
        }
        threads.forEach { it.start() }
        threads.forEach { it.join() }
        return counter
    }
}
