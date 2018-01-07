package pro.bigbro.jdbc.cities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.cities.GoodsMasterStat;
import pro.bigbro.models.reportUnits.cities.GoodsStat;

import javax.sql.DataSource;
import java.util.List;

@Component
@Lazy
public class GoodsStatJdbcTemplate {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GoodsStatJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_FIND_GOODS_STAT = "SELECT\n" +
            "  date_part('year', gt.create_date)      AS year,\n" +
            "  date_part('month', gt.create_date)     AS mon,\n" +
            "  g.id,\n" +
            "  g.title,\n" +
            "  sum(gt.cost) as sales\n" +
            "FROM goods_transaction gt\n" +
            "  JOIN good g ON g.id = gt.good_id\n" +
            "WHERE gt.type_id = 1\n" +
            "and gt.city_id = ?\n" +
            "GROUP BY 1, 2, 3";

    private String SQL_FIND_GOODS_BY_MASTERS_STAT = "SELECT\n" +
            "  s.id,\n" +
            "  s.name,\n" +
            "  extract(YEAR FROM gt.create_date)  AS year,\n" +
            "  extract(MONTH FROM gt.create_date) AS month,\n" +
            "  sum(gt.cost),\n" +
            "  max(cl.clients),\n" +
            "  sum(gt.cost) / max(cl.clients) as spc\n" +
            "FROM goods_transaction gt\n" +
            "  LEFT JOIN staff s ON gt.staff_id = s.id\n" +
            "  LEFT JOIN (\n" +
            "              SELECT\n" +
            "                s.id,\n" +
            "                date_part('year', rt.datetime)  AS year,\n" +
            "                date_part('month', rt.datetime) AS mon,\n" +
            "                count(*)                        AS clients\n" +
            "              FROM record_transaction rt\n" +
            "                LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "              WHERE rt.city_id = ?\n" +
            "                    AND rt.attendance = 1\n" +
            "                    AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "              GROUP BY 1, 2, 3\n" +
            "            ) cl ON cl.year = extract(YEAR FROM gt.create_date) AND cl.mon = extract(MONTH FROM gt.create_date)\n" +
            "                    AND cl.id = gt.staff_id\n" +
            "WHERE gt.type_id = 1\n" +
            "      AND gt.city_id = ?\n" +
            "      AND (gt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "GROUP BY 1, 3, 4";

    private RowMapper<GoodsStat> goodsStatRowMapper = (resultSet, i) ->
            new GoodsStat(
                    resultSet.getInt("mon"),
                    resultSet.getInt("year"),
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getDouble("sales")
            );

    private RowMapper<GoodsMasterStat> goodsMasterStatRowMapper = (resultSet, i) ->
            new GoodsMasterStat(
                    resultSet.getInt("id"),
                    resultSet.getString("name") == null ? "Удаленные" : resultSet.getString("name"),
                    resultSet.getInt("month"),
                    resultSet.getInt("year"),
                    resultSet.getDouble("sum"),
                    resultSet.getDouble("spc")
            );

    public List<GoodsStat> getAllGoodsStats (int cityId) {
        return jdbcTemplate.query(SQL_FIND_GOODS_STAT, goodsStatRowMapper, cityId);
    }

    public List<GoodsMasterStat> getAllGoodsByMastersStat (int cityId) {
        return jdbcTemplate.query(SQL_FIND_GOODS_BY_MASTERS_STAT, goodsMasterStatRowMapper, cityId, cityId);
    }
}
