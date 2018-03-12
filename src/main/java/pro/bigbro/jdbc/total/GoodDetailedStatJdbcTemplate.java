package pro.bigbro.jdbc.total;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.total.GoodDetailedStat;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GoodDetailedStatJdbcTemplate {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public GoodDetailedStatJdbcTemplate(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private final String SQL_DATAILED_STAT = "SELECT a.city_id, a.city_name, a.title, a.price, a.sales, a.amount,\n" +
            "  a.sales / a.amount :: FLOAT as fact,\n" +
            "  a.sales / b.sales :: FLOAT as part\n" +
            "  from (\n" +
            "SELECT\n" +
            "  rt.city_id,\n" +
            "  c.name                AS city_name,\n" +
            "  CASE WHEN lower(g.title) LIKE '%сертификат%'\n" +
            "    THEN 'Сертификат'\n" +
            "  ELSE g.title END as title,\n" +
            "  max(rt.cost_per_unit) AS price,\n" +
            "  sum(rt.cost)          AS sales,\n" +
            "  sum(rt.amount * -1)   AS amount\n" +
            "FROM goods_transaction rt\n" +
            "  JOIN good g ON rt.good_id = g.id\n" +
            "  JOIN city c ON c.id = rt.city_id\n" +
            "WHERE rt.type_id = 1\n" +
            "      AND extract(YEAR FROM rt.create_date) = :year\n" +
            "      AND extract(MONTH FROM rt.create_date) = :month\n" +
            "GROUP BY 1, 2, 3) a\n" +
            "JOIN (SELECT rt.city_id, sum(rt.cost) as sales\n" +
            "FROM goods_transaction rt\n" +
            "WHERE rt.type_id = 1\n" +
            "      AND extract(YEAR FROM rt.create_date) = :year\n" +
            "      AND extract(MONTH FROM rt.create_date) = :month\n" +
            "GROUP BY 1) b on a.city_id = b.city_id";

    private RowMapper<GoodDetailedStat> goodStatRowMapper = (resultSet, i) ->
            new GoodDetailedStat(resultSet.getInt("city_id"),
                    resultSet.getString("city_name"),
                    resultSet.getString("title"),
                    resultSet.getDouble("price"),
                    resultSet.getDouble("sales"),
                    resultSet.getDouble("amount"),
                    resultSet.getDouble("fact"),
                    resultSet.getDouble("part"));

    public List<GoodDetailedStat> getDetailesStat (int year, int month) {
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
        return namedParameterJdbcTemplate.query(SQL_DATAILED_STAT, params, goodStatRowMapper);
    }
}
