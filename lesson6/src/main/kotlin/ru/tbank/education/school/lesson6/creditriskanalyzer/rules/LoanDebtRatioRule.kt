package ru.tbank.education.school.lesson6.creditriskanalyzer.rules

import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Client
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.PaymentRisk
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.ScoringResult
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.AccountRepository
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.LoanRepository

/**
 * Проверяет соотношение между суммарной задолженностью и балансом на счетах.
 *
 * Идея:
 * - Получить все активные кредиты клиента (isClosed == false)
 * - Посчитать общий долг (sumOf(debt))
 * - Посчитать общий баланс на всех счетах
 * 
 * Как считать score:
 * - Если долг > 3 * баланс → HIGH
 * - Если долг > баланс, но < 3 * баланс → MEDIUM
 * - Если долг <= баланс → LOW
 *
 */
class LoanDebtRatioRule(
    private val loanRepo: LoanRepository,
    private val accountRepo: AccountRepository,
) : ScoringRule {

    override val ruleName: String = "Loan Debt Ratio"

    override fun evaluate(client: Client): ScoringResult {
        val lo = loanRepo.getLoans(client.id)
        val accs = accountRepo.getAccounts(client.id)
        var sm = 0L
        var ssm = 0L
        for (i in lo) {
             if (!i.isClosed) {
                 sm += i.debt
             }
        }
        for (i in accs) {
            ssm += i.balance
        }

        return ScoringResult(ruleName, when {
            sm > 3*ssm -> PaymentRisk.HIGH
            sm > ssm -> PaymentRisk.MEDIUM
            sm <= ssm -> PaymentRisk.LOW
            else -> PaymentRisk.LOW
        })
    }
}