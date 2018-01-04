package pro.bigbro.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pro.bigbro.models.records.RecordTransaction;

public interface RecordTransactionRepository extends CrudRepository<RecordTransaction, Long> {
}
