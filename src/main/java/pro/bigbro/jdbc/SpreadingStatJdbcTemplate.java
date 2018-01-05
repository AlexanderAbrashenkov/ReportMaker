package pro.bigbro.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.SpreadingDaysCount;
import pro.bigbro.models.reportUnits.SpreadingStat;

import javax.sql.DataSource;
import java.util.List;

@Component
public class SpreadingStatJdbcTemplate {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SpreadingStatJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_FIND_SPREADING = "SELECT\n" +
            "  date_part('year', rt.datetime)      AS year,\n" +
            "  date_part('month', rt.datetime)     AS mon,\n" +
            "  CASE WHEN extract(day FROM rt.datetime) < 8\n" +
            "    THEN 1\n" +
            "  WHEN extract(day from rt.datetime) >= 8 AND extract(day FROM rt.datetime) < 15\n" +
            "    THEN 2\n" +
            "  WHEN extract(day FROM rt.datetime) >= 15 AND extract(day FROM rt.datetime) < 22\n" +
            "    THEN 3\n" +
            "  ELSE 4\n" +
            "  END                                 AS week,\n" +
            "  extract(DOW FROM rt.datetime) AS dow,\n" +
            "  count(*)                            AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "WHERE rt.city_id = ?\n" +
            "      AND rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "GROUP BY 1, 2, 3, 4";

    private String SQL_COUNT_WORKING_DAYS = "SELECT\n" +
            "  CASE WHEN extract(day FROM rt.datetime) < 8\n" +
            "    THEN 1\n" +
            "  WHEN extract(day from rt.datetime) >= 8 AND extract(day FROM rt.datetime) < 15\n" +
            "    THEN 2\n" +
            "  WHEN extract(day FROM rt.datetime) >= 15 AND extract(day FROM rt.datetime) < 22\n" +
            "    THEN 3\n" +
            "  ELSE 4\n" +
            "  END                                 AS week,\n" +
            "  extract(DOW FROM rt.datetime) AS dow,\n" +
            "  count(DISTINCT rt.datetime::DATE)                            AS days\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "WHERE rt.city_id = ?\n" +
            "      AND rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "GROUP BY 1, 2";

    private RowMapper<SpreadingStat> spreadingStatRowMapper = (resultSet, i) ->
            new SpreadingStat(
                    resultSet.getInt("mon"),
                    resultSet.getInt("year"),
                    resultSet.getInt("week"),
                    resultSet.getInt("dow"),
                    resultSet.getInt("clients")
            );

    private RowMapper<SpreadingDaysCount> spreadingDaysCountRowMapper = (resultSet, i) ->
            new SpreadingDaysCount(
                    resultSet.getInt("week"),
                    resultSet.getInt("dow"),
                    resultSet.getInt("days")
            );

    public List<SpreadingStat> getSpreadingStat (int cityId) {
        List<SpreadingStat> result = jdbcTemplate.query(SQL_FIND_SPREADING, spreadingStatRowMapper, cityId);
        result.stream()
                .filter(spreadingStat -> spreadingStat.getDayOfWeek() == 0)
                .forEach(spreadingStat -> spreadingStat.setDayOfWeek(7));
        return result;
    }

    public List<SpreadingDaysCount> getSpreadingDaysCount (int cityId) {
        List<SpreadingDaysCount> result = jdbcTemplate.query(SQL_COUNT_WORKING_DAYS, spreadingDaysCountRowMapper, cityId);
        result.stream()
                .filter(spreadingDaysCount -> spreadingDaysCount.getDayOfWeek() == 0)
                .forEach(spreadingDaysCount -> spreadingDaysCount.setDayOfWeek(7));
        return result;
    }
}
