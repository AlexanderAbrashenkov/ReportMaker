package pro.bigbro.jdbc.total;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.total.ClientTotal;

import javax.sql.DataSource;
import java.util.List;

@Component
@Lazy
public class ClientTotalJdbcTemplate {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ClientTotalJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_K = "SELECT\n" +
            "  c.id,\n" +
            "  c.name,\n" +
            "  count(*) AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  JOIN city c ON c.id = rt.city_id\n" +
            "WHERE rt.attendance = 1\n" +
            "      AND extract(YEAR FROM rt.datetime) = ?\n" +
            "      AND extract(MONTH FROM rt.datetime) = ?\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND rt.visit_number >= ? AND rt.visit_number < ?" +
            "GROUP BY 1";

    private String SQL_ANONIM = "SELECT\n" +
            "  c.id,\n" +
            "  c.name,\n" +
            "  count(*) AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  JOIN city c ON c.id = rt.city_id\n" +
            "WHERE rt.attendance = 1\n" +
            "      AND extract(YEAR FROM rt.datetime) = ?\n" +
            "      AND extract(MONTH FROM rt.datetime) = ?\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND rt.client_id IS NULL\n" +
            "GROUP BY 1";

    private String SQL_WITHOUT_LINKS = "SELECT\n" +
            "  c.id,\n" +
            "  c.name,\n" +
            "  count(*) AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  JOIN city c ON c.id = rt.city_id\n" +
            "  LEFT JOIN client cl ON rt.client_id = cl.id\n" +
            "WHERE rt.attendance = 1\n" +
            "      AND extract(YEAR FROM rt.datetime) = ?\n" +
            "      AND extract(MONTH FROM rt.datetime) = ?\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND (rt.client_id IS NULL OR cl.has_link = 0)\n" +
            "GROUP BY 1";

    private RowMapper<ClientTotal> clientTotalRowMapper = (resultSet, i) ->
            new ClientTotal(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("clients")
            );

    public List<ClientTotal> getK(int year, int month) {
        return jdbcTemplate.query(SQL_K, clientTotalRowMapper, year, month, 1, 100000);
    }

    public List<ClientTotal> getNk(int year, int month) {
        return jdbcTemplate.query(SQL_K, clientTotalRowMapper, year, month, 1, 2);
    }

    public List<ClientTotal> getPk(int year, int month) {
        return jdbcTemplate.query(SQL_K, clientTotalRowMapper, year, month, 2, 100000);
    }

    public List<ClientTotal> getAnonims(int year, int month) {
        return jdbcTemplate.query(SQL_ANONIM, clientTotalRowMapper, year, month);
    }

    public List<ClientTotal> getWithoutLinks(int year, int month) {
        return jdbcTemplate.query(SQL_WITHOUT_LINKS, clientTotalRowMapper, year, month);
    }
}
