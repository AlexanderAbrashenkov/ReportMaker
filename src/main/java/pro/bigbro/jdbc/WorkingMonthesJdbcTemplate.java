package pro.bigbro.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.WorkingMonth;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

@Component
public class WorkingMonthesJdbcTemplate {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public WorkingMonthesJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_FIND_PERIODS_FOR_CITY = "SELECT DISTINCT date_part('year', datetime) as year, date_part('month', datetime) as mon\n" +
            "from record_transaction\n" +
            "WHERE city_id = ?\n" +
            "ORDER BY 1, 2";

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
