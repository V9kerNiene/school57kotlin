package ru.tbank.education.school.lesson6.creditriskanalyzer.rules

import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Client
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.PaymentRisk
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.ScoringResult
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.TransactionCategory
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.TransactionRepository
import java.time.LocalDateTime

/**
 * Анализирует соотношение доходов и расходов клиента за последние 3 месяца.
 *
 * Идея:
 * - Получить все транзакции клиента за последние 3 месяца.
 * - Разделить их на доходы (категория SALARY) и расходы (все остальные).
 * - Посчитать общую сумму доходов и расходов.
 * - Определить финансовое равновесие клиента.
 *
 * Как считать score:
 * - Если расходы > доходов → HIGH (клиент тратит больше, чем зарабатывает)
 * - Если расходы примерно равны доходам (±20% включительно) → MEDIUM
 * - Если доходы значительно больше расходов → LOW
 *
 */
class IncomeExpenseRatioRule(
    private val transactionRepo: TransactionRepository
) : ScoringRule {

    override val ruleName: String = "Loan Count"

    override fun evaluate(client: Client): ScoringResult {
        val trann = transactionRepo.getTransactions(client.id)
        var r = 0.0F
        var d = 0.0F
        var co = 0
        for (i in trann) {
            if (LocalDateTime.now().minusMonths(3) <= i.date) {
                co++
                if (i.category == TransactionCategory.SALARY) d += i.amount
                else r += i.amount
            }
        }
        val res = when {
            (co == 0) or (r > d) -> PaymentRisk.HIGH
            ((r/d >= 0.8F) and (r/d <= 1.2F)) -> PaymentRisk.MEDIUM
            else -> PaymentRisk.LOW
        }


        return ScoringResult(ruleName, res)

    }
}