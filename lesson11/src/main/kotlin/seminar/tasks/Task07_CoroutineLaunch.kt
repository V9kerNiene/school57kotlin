package seminar.tasks

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Задание 8. Первая корутина
 *
 * Используя runBlocking и launch, запустите 3 корутины,
 * каждая из которых выводит своё имя 5 раз с delay(500).
 */
object CoroutineLaunch {

    /**
     * @return список имён корутин в порядке их запуска
     */
    fun run(): List<String> = runBlocking {
        val names = listOf("Coroutine-A", "Coroutine-B", "Coroutine-C")
        names.forEach { name ->
            launch(CoroutineName(name)) {
                repeat(5) {
                    println(name)
                    delay(500)
                }
            }
        }
        names
    }
}
