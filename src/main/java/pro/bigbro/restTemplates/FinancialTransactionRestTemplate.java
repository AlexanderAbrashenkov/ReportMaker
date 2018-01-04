package pro.bigbro.restTemplates;

import pro.bigbro.models.financial.FinancialTransaction;

import java.time.LocalDate;
import java.util.List;

public interface FinancialTransactionRestTemplate {
    List<FinancialTransaction> getAllFinancialTransactions(LocalDate startDate, LocalDate endDate);
}
