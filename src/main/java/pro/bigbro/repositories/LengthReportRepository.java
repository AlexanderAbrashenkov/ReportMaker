package pro.bigbro.repositories;

import org.springframework.data.repository.CrudRepository;
import pro.bigbro.models.reportUnits.avglength.LengthReport;

import java.util.List;

public interface LengthReportRepository extends CrudRepository<LengthReport, Long> {
    List<LengthReport> findAllByMonthAndYear(int month, int year);
}
