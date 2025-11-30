package ru.tbank.education.school.lesson8.homework.payments

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class PaymentProcessorTest {
    private lateinit var processor: PaymentProcessor
    @BeforeEach
    fun setUp() {
        processor = PaymentProcessor()
    }

    @Test
    fun `-summa`() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.processPayment(-5, "12345678912345", 12, 2027, "RUB", "123zfsa")
        }
    }
    @Test
    fun `0currency`() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.processPayment(5, "12345678912345", 12, 2027, "", "123zfsa")
        }
    }
    @Test
    fun `0client`() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.processPayment(5, "12345678912345", 12, 2027, "RUB", "")
        }
    }
    @Test
    fun blankCard() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.processPayment(5, "", 12, 2027, "RUB", "dsad13")
        }
    }
    @Test
    fun fourCards() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.processPayment(5, "1234", 12, 2027, "RUB", "dsad13")
        }
    }
    @Test
    fun letterCard() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.processPayment(5, "12345xyz912345", 12, 2027, "RUB", "dsad13")
        }
    }
    @Test
    fun aLotNumbersTest() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.processPayment(5, "123459123451234591234512345912345", 12, 2027, "RUB", "dsad13")
        }
    }
    @Test
    fun littleYear() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.processPayment(5, "12345678912345", 12, 2017, "RUB", "dsad13")
        }
    }
    @Test
    fun `current year and little month`() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.processPayment(5, "12345678912345", 10, 2025, "RUB", "dsad13")
        }
    }
    @Test
    fun `suspicious card number`() {
        val x = processor.processPayment(5, "11115678912345", 10, 2027, "RUB", "dsad13")
        assertEquals(PaymentResult("REJECTED", "Payment blocked due to suspected fraud"), x)
    }
    @Test
    fun `test 1`() {
        val x = processor.processPayment(1, "4318592543487834", 10, 2027, "usd", "dsad13")
        assertEquals(PaymentResult("SUCCESS", "Payment completed"), x)
    }
    @Test
    fun `test 2`() {
        val x = processor.processPayment(2, "4318592543487834", 10, 2027, "eur", "dsad13")
        assertEquals(PaymentResult("SUCCESS", "Payment completed"), x)
    }
    @Test
    fun `test 3`() {
        val x = processor.processPayment(2, "4318592543487834", 10, 2027, "gbp", "dsad13")
        assertEquals(PaymentResult("SUCCESS", "Payment completed"), x)
    }
    @Test
    fun `test 4`() {
        val x = processor.processPayment(1, "4318592543487834", 10, 2027, "jpy", "dsad13asdsad")
        assertEquals(PaymentResult("SUCCESS", "Payment completed"), x)
    }
    @Test
    fun `test 5`() {
        val x = processor.processPayment(1, "4318592543487834", 10, 2027, "rub", "dsad13asdsad")
        assertEquals(PaymentResult("SUCCESS", "Payment completed"), x)
    }
    @Test
    fun `test 6`() {
        val x = processor.processPayment(1, "4318592543487834", 10, 2027, "cny", "dsad13asdsad")
        assertEquals(PaymentResult("SUCCESS", "Payment completed"), x)
    }
    @Test
    fun `test 7`() {
        val x = processor.processPayment(200000, "4318592543487834", 10, 2027, "eur", "dsad13")
        assertEquals(PaymentResult("FAILED", "Transaction limit exceeded"), x)
    }
    @Test
    fun `test 8`() {
        val x = processor.processPayment(1, "4318592543487834", 10, 2027, "eur", "dsad13")
        assertEquals(PaymentResult("FAILED", "Gateway timeout"), x)
    }
    @Test
    fun `test 9`() {
        val x = processor.processPayment(1, "5500129203484804", 10, 2027, "eur", "dsad13")
        assertEquals(PaymentResult("FAILED", "Insufficient funds"), x)
    }
    @Test
    fun `test 10`() {
        val x = processor.processPayment(1, "4444129203484814", 10, 2027, "eur", "dsad13")
        assertEquals(PaymentResult("FAILED", "Card is blocked"), x)
    }
    @Test
    fun `test 11`() {
        val x = processor.processPayment(1, "4444129203484814", 10, 2027, "eur", "dsad13")
        assertEquals(PaymentData(10, "4444129203484814", 10, 2025, "USD", "123981dsad"), PaymentData(10, "4444129203484814", 10, 2025, "USD", "123981dsad"))
    }
    @Test
    fun `test 12`() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.calculateLoyaltyDiscount(15, -100)
        }
    }
    @Test
    fun `test 13`() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.calculateLoyaltyDiscount(15, -100)
        }
    }
    @Test
    fun `test 14`() {
        val x = processor.calculateLoyaltyDiscount(15000, 100)
        assertEquals(20, x)
    }
    @Test
    fun `test 15`() {
        val x = processor.calculateLoyaltyDiscount(7000, 100)
        assertEquals(15, x)
    }
    @Test
    fun `test 16`() {
        val x = processor.calculateLoyaltyDiscount(3000, 100)
        assertEquals(10, x)
    }
    @Test
    fun `test 17`() {
        val x = processor.calculateLoyaltyDiscount(1000, 100)
        assertEquals(5, x)
    }
    @Test
    fun `test 18`() {
        val x = processor.calculateLoyaltyDiscount(500, 12000)
        assertEquals(500, x)
    }
    @Test
    fun `test 19`() {
        val x = processor.calculateLoyaltyDiscount(300, 12000)
        assertEquals(0, x)
    }

    @Test
    fun `test 20`() {
        val x = processor.bulkProcess(listOf<PaymentData>())
        assertEquals(emptyList(), x)
    }
    @Test
    fun `test 21`() {
        val x = processor.bulkProcess(listOf<PaymentData>(PaymentData(10, "4318592543487834", 12, 2025, "USD", "123981dsad")))
        assertEquals(listOf(PaymentResult("SUCCESS", "Payment completed")), x)
    }
    @Test
    fun `test 22`() {
        val x = processor.bulkProcess(listOf<PaymentData>(PaymentData(10, "4318592543487834", 7, 2025, "USD", "123981dsad")))
        assertEquals(listOf(PaymentResult("REJECTED", "Invalid expiry date")), x)
    }
    @Test
    fun `test 23`() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.processPayment(10, "4318592543487834", 0, 2027, "USD", "123981dsad")
        }
    }
    @Test
    fun `test 24`() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.processPayment(10, "4318592543487834", 0, 2025, "USD", "123981dsad")
        }
    }
    @Test
    fun `test 25`() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.processPayment(10, "4318592543487834", 13, 2025, "USD", "123981dsad")
        }
    }
    @Test
    fun `test 26`() {
        assertThrows(IllegalArgumentException::class.java) {
            val x = processor.processPayment(10, "4318592543487834", 13, 2027, "USD", "123981dsad")
        }
    }
    @Test
    fun `test 27`() {
        val x = processor.processPayment(10, "4318592543487834", 6, 2027, "USD", "123981dsad")
        assertEquals(PaymentResult("SUCCESS", "Payment completed"), x)
    }

    @Test
    fun `test 28`() {
        val x = processor.bulkProcess(listOf<PaymentData>(PaymentData(10, "4441592543487834", 12, 2025, "USD", "123981dsad")))
        assertEquals(listOf(PaymentResult("REJECTED", "Payment blocked due to suspected fraud")), x)
    }
    @Test
    fun `test 29`() {
        val x = processor.processPayment(15231, "4387129203484713", 10, 2027, "eur", "dsad13")
        assertEquals(PaymentResult("REJECTED", "Payment blocked due to suspected fraud"), x)
    }
}