package pro.bigbro.restTemplates;

import pro.bigbro.models.records.RecordTransaction;

import java.time.LocalDate;
import java.util.List;

public interface RecordTransactionRestTemplate {
    List<RecordTransaction> getAllRecordTransactions(LocalDate startDate, LocalDate endDate);
}
