package ru.tbank.education.school.lesson8.practise

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 *
 * Сценарии для тестирования:
 *
 * 1. Позитивные сценарии (happy path):
 *    - Обычный случай: basePrice = 1000, discount = 10%, tax = 20% → проверить корректность формулы.
 *    - Без скидки: discountPercent = 0 → итог = basePrice + налог.
 *    - Без налога: taxPercent = 0 → итог = basePrice минус скидка.
 *    - Без скидки и без налога: итог = basePrice.
 *
 * 2. Негативные сценарии (исключения):
 *    - Отрицательная цена: basePrice < 0 → IllegalArgumentException.
 *    - Скидка вне диапазона: discountPercent < 0 или > 100 → IllegalArgumentException.
 *    - Налог вне диапазона: taxPercent < 0 или > 30 → IllegalArgumentException.
 */
class CalculateFinalPriceTest {

    @Test
    @DisplayName("Base situation")
    fun `happy path test`() {
        assertEquals(1080.0, calculateFinalPrice(1000.0, 10, 20))
    }

    @Test
    @DisplayName("Without discount")
    fun `zero discount`() {
        for (price in listOf(100.0, 200.0, 300.0, 400.0, 500.0, 1080.0, 1000.0, 600.0)) {
            assertEquals(price * 1.2, calculateFinalPrice(price, 0, 20))
        }
    }

    @Test
    @DisplayName("Without tax")
    fun `zero tax`() {
        for (price in listOf(100.0, 200.0, 300.0, 400.0, 500.0, 1080.0, 1000.0, 600.0)) {
            assertEquals(price *0.84, calculateFinalPrice(price, 16, 0))
        }
    }

    @Test
    @DisplayName("No tax+discount")
    fun yahoo() {
        for (price in listOf(100.0, 200.0, 300.0, 400.0, 500.0, 1080.0, 1000.0, 600.0)) {
            assertEquals(price, calculateFinalPrice(price, 0, 0))
        }
    }

    @Test
    @DisplayName("Negative price")
    fun shouldBe() {
        for (price in listOf(-100.0, -200.0, -300.0, -400.0, -500.0, -1080.0, -1000.0, -600.0)) {
            assertFailsWith(IllegalArgumentException::class) {
                val i = calculateFinalPrice(price, 12, 15)
            }
        }
    }

    @Test
    @DisplayName("Illegal discount")
    fun shouldBe1() {
        for (discount in listOf(120, -5, -100, -120, 101, -1)) {
            assertFailsWith(IllegalArgumentException::class) {
                val i = calculateFinalPrice(1000.0, discount, 15)
            }
        }
    }

    @Test
    @DisplayName("Illegal tax")
    fun shouldBe2() {
        for (tax in listOf(340, -5, -30, -31, 35, -1)) {
            assertFailsWith(IllegalArgumentException::class) {
                val i = calculateFinalPrice(1000.0, 12, tax)
            }
        }
    }
}