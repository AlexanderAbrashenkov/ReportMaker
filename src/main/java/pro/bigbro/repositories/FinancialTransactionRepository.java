package pro.bigbro.repositories;

import org.springframework.data.repository.CrudRepository;
import pro.bigbro.models.financial.FinancialTransaction;

public interface FinancialTransactionRepository extends CrudRepository<FinancialTransaction, Long> {
}
