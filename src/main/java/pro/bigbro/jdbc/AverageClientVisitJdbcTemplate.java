package pro.bigbro.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.AverageClientVisit;
import pro.bigbro.models.reportUnits.ClientStat;

import javax.sql.DataSource;
import java.util.List;

@Component
public class AverageClientVisitJdbcTemplate {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AverageClientVisitJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_AVERAGE_CLIENT_VISIT = "SELECT\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  avg(rt.visit_number)                        AS average\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "WHERE rt.city_id = ?\n" +
            "      AND rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "GROUP BY 1, 2\n" +
            "ORDER BY 1, 2";

    private RowMapper<AverageClientVisit> averageClientVisitRowMapper = (resultSet, i) ->
            new AverageClientVisit(
                    resultSet.getInt("mon"),
                    resultSet.getInt("year"),
                    resultSet.getDouble("average")
            );

    public List<AverageClientVisit> countAverageClientVisit(int cityId) {
        return jdbcTemplate.query(SQL_AVERAGE_CLIENT_VISIT, averageClientVisitRowMapper, cityId);
    }
}
