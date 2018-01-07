package pro.bigbro.jdbc.cities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.cities.WorkingMonth;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

@Component
@Lazy
public class WorkingMonthesJdbcTemplate {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public WorkingMonthesJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_FIND_PERIODS_FOR_CITY = "SELECT DISTINCT\n" +
            "  extract(year FROM rt.datetime)  AS year,\n" +
            "  extract(month FROM rt.datetime) AS mon\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "WHERE rt.city_id = ?\n" +
            "  and rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')";

    private RowMapper<WorkingMonth> workingMonthesRowMapper = (resultSet, i) ->
        new WorkingMonth(
                resultSet.getInt("mon"),
                resultSet.getInt("year")
        );

    public List<WorkingMonth> findAllWorkingPeriodsByCity(int cityId) {
        List<WorkingMonth> workingMonthList = jdbcTemplate.query(SQL_FIND_PERIODS_FOR_CITY, workingMonthesRowMapper, cityId);
        Collections.sort(workingMonthList);
        return workingMonthList;
    }
}
