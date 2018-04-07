package pro.bigbro.jdbc.total;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.total.DetailedStat;
import pro.bigbro.models.reportUnits.total.DetailedTotalStat;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServiceDetailedStatJdbcTemplate {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ServiceDetailedStatJdbcTemplate(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private final String SQL_DATAILED_TOTAL_STAT = "SELECT a.name as title, a.sales, a.amount,\n" +
            "  a.sales / a.total :: FLOAT AS part\n" +
            "FROM (\n" +
            "       SELECT\n" +
            "         sg.name AS name,\n" +
            "         sum(cs.cost) AS sales,\n" +
            "         sum(cs.amount) AS amount,\n" +
            "         (SELECT sum(cs.cost)\n" +
            "          FROM record_transaction rt\n" +
            "            JOIN record_transaction_service_list list ON rt.id = list.record_transaction_id\n" +
            "            JOIN concrete_service cs ON list.service_list_uid = cs.uid\n" +
            "          WHERE extract(YEAR FROM rt.datetime) = :year\n" +
            "            AND extract(MONTH FROM rt.datetime) = :month\n" +
            "                AND rt.attendance = 1) AS total\n" +
            "       FROM record_transaction rt\n" +
            "         JOIN record_transaction_service_list list ON rt.id = list.record_transaction_id\n" +
            "         JOIN concrete_service cs ON list.service_list_uid = cs.uid\n" +
            "         JOIN service_lib sl ON sl.service_id = cs.service_id\n" +
            "         JOIN service_group sg ON sg.id = sl.service_group_id\n" +
            "         JOIN city c ON c.id = rt.city_id\n" +
            "       WHERE extract(YEAR FROM rt.datetime) = :year\n" +
            "             AND extract(MONTH FROM rt.datetime) = :month\n" +
            "             AND rt.attendance = 1\n" +
            "       GROUP BY 1) AS a\n" +
            "ORDER BY 2 DESC";

    private final String SQL_DATAILED_STAT = "SELECT a.city_id, a.city_name, a.title, p.price, a.sales, a.amount,\n" +
            "  a.sales / a.amount :: FLOAT AS fact,\n" +
            "  a.sales / t.total :: FLOAT AS part\n" +
            "FROM (\n" +
            "       SELECT\n" +
            "         c.id AS city_id, c.name AS city_name,\n" +
            "         sg.id AS group_id,\n" +
            "         sg.name AS title,\n" +
            "         sum(cs.cost) AS sales,\n" +
            "         sum(cs.amount) AS amount\n" +
            "       FROM record_transaction rt\n" +
            "         JOIN record_transaction_service_list list ON rt.id = list.record_transaction_id\n" +
            "         JOIN concrete_service cs ON list.service_list_uid = cs.uid\n" +
            "         JOIN service_lib sl ON sl.service_id = cs.service_id\n" +
            "         JOIN service_group sg ON sg.id = sl.service_group_id\n" +
            "         JOIN city c ON c.id = rt.city_id\n" +
            "       WHERE extract(YEAR FROM rt.datetime) = 2018\n" +
            "             AND extract(MONTH FROM rt.datetime) = 3\n" +
            "             AND rt.attendance = 1\n" +
            "       GROUP BY 1, 2, 3, 4) a\n" +
            "  JOIN (SELECT c.id AS city_id, c.name AS city_name, sum(cs.cost) AS total\n" +
            "        FROM record_transaction rt\n" +
            "          JOIN record_transaction_service_list list ON rt.id = list.record_transaction_id\n" +
            "          JOIN concrete_service cs ON list.service_list_uid = cs.uid\n" +
            "          JOIN city c ON c.id = rt.city_id\n" +
            "        WHERE extract(YEAR FROM rt.datetime) = 2018\n" +
            "              AND extract(MONTH FROM rt.datetime) = 3\n" +
            "              AND rt.attendance = 1\n" +
            "        GROUP BY 1, 2) AS t ON t.city_id = a.city_id\n" +
            "JOIN (SELECT s.city_id, c.name, sg.id AS group_id, max(s.price_min) AS price\n" +
            "FROM service s\n" +
            "JOIN service_category sc ON sc.category_id = s.category_id AND sc.city_id = s.city_id\n" +
            "  JOIN city c ON c.id = s.city_id\n" +
            "  JOIN service_lib sl ON sl.service_id = s.service_id\n" +
            "  JOIN service_group sg ON sg.id = sl.service_group_id\n" +
            "WHERE sc.master_category = TRUE\n" +
            "GROUP BY 3, 1, 2) AS p ON p.city_id = a.city_id AND p.group_id = a.group_id";

    private RowMapper<DetailedTotalStat> detailedTotalStatRowMapper = (resultSet, i) ->
            new DetailedTotalStat(resultSet.getString("title"),
                    resultSet.getDouble("sales"),
                    resultSet.getDouble("amount"),
                    resultSet.getDouble("part"));

    private RowMapper<DetailedStat> detailedStatRowMapper = (resultSet, i) ->
            new DetailedStat(resultSet.getInt("city_id"),
                    resultSet.getString("city_name"),
                    resultSet.getString("title"),
                    resultSet.getDouble("price"),
                    resultSet.getDouble("sales"),
                    resultSet.getDouble("amount"),
                    resultSet.getDouble("fact"),
                    resultSet.getDouble("part"));

    public List<DetailedTotalStat> getDetailesTotalStat (int year, int month) {
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
        return namedParameterJdbcTemplate.query(SQL_DATAILED_TOTAL_STAT, params, detailedTotalStatRowMapper);
    }

    public List<DetailedStat> getDetailesStat (int year, int month) {
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
        return namedParameterJdbcTemplate.query(SQL_DATAILED_STAT, params, detailedStatRowMapper);
    }
}
