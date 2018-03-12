package pro.bigbro.jdbc.total;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.total.GoodDetailedTotalStat;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GoodDetailedTotalStatJdbcTemplate {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public GoodDetailedTotalStatJdbcTemplate(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private final String SQL_DATAILED_STAT = "SELECT a.title, a.sales, a.amount,\n" +
            "a.sales / a.total :: FLOAT AS part\n" +
            "FROM (\n" +
            "SELECT\n" +
            "CASE WHEN lower(g.title) LIKE '%сертификат%'\n" +
            "THEN 'Сертификат'\n" +
            "ELSE g.title END AS title,\n" +
            "sum(rt.cost)          AS sales,\n" +
            "sum(rt.amount * -1)   AS amount,\n" +
            "  (SELECT sum(rt.cost)\n" +
            "   FROM goods_transaction rt\n" +
            "   WHERE rt.type_id = 1\n" +
            "         AND extract(YEAR FROM rt.create_date) = :year\n" +
            "         AND extract(MONTH FROM rt.create_date) = :month) AS total\n" +
            "FROM goods_transaction rt\n" +
            "JOIN good g ON rt.good_id = g.id\n" +
            "WHERE rt.type_id = 1\n" +
            "AND extract(YEAR FROM rt.create_date) = :year\n" +
            "AND extract(MONTH FROM rt.create_date) = :month\n" +
            "GROUP BY 1) a\n" +
            "ORDER BY a.sales DESC\n" +
            "LIMIT 100;";

    private RowMapper<GoodDetailedTotalStat> goodDetailedTotalStatRowMapper = (resultSet, i) ->
            new GoodDetailedTotalStat(resultSet.getString("title"),
                    resultSet.getDouble("sales"),
                    resultSet.getDouble("amount"),
                    resultSet.getDouble("part"));

    public List<GoodDetailedTotalStat> getDetailesTotalStat (int year, int month) {
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
        return namedParameterJdbcTemplate.query(SQL_DATAILED_STAT, params, goodDetailedTotalStatRowMapper);
    }
}
