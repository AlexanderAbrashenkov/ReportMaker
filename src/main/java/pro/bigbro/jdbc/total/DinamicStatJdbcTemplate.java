package pro.bigbro.jdbc.total;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.total.DinamicStat;

import javax.sql.DataSource;
import java.util.List;

@Component
@Lazy
public class DinamicStatJdbcTemplate {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public DinamicStatJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_FINANCE_ALL = "SELECT\n" +
            "  c.id,\n" +
            "  c.name,\n" +
            "  extract(YEAR FROM ft.date) as year,\n" +
            "  extract(MONTH FROM ft.date) as month,\n" +
            "  sum(ft.amount) AS res\n" +
            "FROM financial_transaction ft\n" +
            "  JOIN city c ON c.id = ft.city_id\n" +
            "    WHERE (ft.expense_id = 5 OR ft.expense_id = 7)\n" +
            "GROUP BY 1, 2, 3, 4";

    private String SQL_CLIENT_ALL = "SELECT\n" +
            "  c.id,\n" +
            "  c.name,\n" +
            "  extract(YEAR FROM rt.datetime) as year,\n" +
            "  extract(MONTH FROM rt.datetime) as month,\n" +
            "  count(*) AS res\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  JOIN city c ON c.id = rt.city_id\n" +
            "WHERE rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "GROUP BY 1, 2, 3, 4";

    private RowMapper<DinamicStat> dinamicStatRowMapper = (resultSet, i) ->
            new DinamicStat(
                    resultSet.getInt("month"),
                    resultSet.getInt("year"),
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("res")
            );

    public List<DinamicStat> getFinancialDinamic() {
        return jdbcTemplate.query(SQL_FINANCE_ALL, dinamicStatRowMapper);
    }

    public List<DinamicStat> getClientsDinamic() {
        return jdbcTemplate.query(SQL_CLIENT_ALL, dinamicStatRowMapper);
    }
}
