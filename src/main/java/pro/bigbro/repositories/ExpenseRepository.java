package pro.bigbro.repositories;

import org.springframework.data.repository.CrudRepository;
import pro.bigbro.models.financial.Expense;

public interface ExpenseRepository extends CrudRepository<Expense, Long> {
}
