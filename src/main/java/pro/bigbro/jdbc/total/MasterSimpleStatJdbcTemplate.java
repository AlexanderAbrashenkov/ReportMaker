package pro.bigbro.jdbc.total;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.total.MasterSimpleStat;
import pro.bigbro.models.reportUnits.total.MasterStat;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Lazy
public class MasterSimpleStatJdbcTemplate {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public MasterSimpleStatJdbcTemplate(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private String SQL_DAILY_INCOME = "SELECT\n" +
            "  r.staff_name,\n" +
            "  (coalesce(sum(r.res), 0) + coalesce(sum(g.res), 0)) / sum(d.res) AS res\n" +
            "FROM (SELECT\n" +
            "        s.name       AS staff_name,\n" +
            "        sum(cs.cost) AS res\n" +
            "      FROM record_transaction rt\n" +
            "        LEFT JOIN record_transaction_service_list rtsl ON rt.id = rtsl.record_transaction_id\n" +
            "        LEFT JOIN concrete_service cs ON rtsl.service_list_uid = cs.uid\n" +
            "        JOIN city c ON c.id = rt.city_id\n" +
            "        LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "      WHERE extract(YEAR FROM rt.datetime) = :year\n" +
            "            AND extract(MONTH FROM rt.datetime) = :month\n" +
            "            AND rt.attendance = 1\n" +
            "            AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "            AND s.city_id in (:ids)\n" +
            "      GROUP BY 1) r\n" +
            "  LEFT JOIN (\n" +
            "              SELECT\n" +
            "                s.name       AS staff_name,\n" +
            "                sum(gt.cost) AS res\n" +
            "              FROM goods_transaction gt\n" +
            "                LEFT JOIN staff s ON gt.staff_id = s.id\n" +
            "                JOIN city c ON c.id = gt.city_id\n" +
            "              WHERE gt.type_id = 1\n" +
            "                    AND (gt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "                    AND extract(YEAR FROM gt.create_date) = :year\n" +
            "                    AND extract(MONTH FROM gt.create_date) = :month\n" +
            "                    AND s.city_id in (:ids)\n" +
            "              GROUP BY 1\n" +
            "            ) g ON g.staff_name = r.staff_name\n" +
            "  LEFT JOIN (SELECT\n" +
            "               r.staff_name       AS staff_name,\n" +
            "               count(r.date_part) AS res\n" +
            "             FROM (\n" +
            "                    SELECT\n" +
            "                      s.name AS staff_name,\n" +
            "                      extract(DAY FROM rt.date)\n" +
            "                    FROM record_transaction rt\n" +
            "                      JOIN city c ON c.id = rt.city_id\n" +
            "                      LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "                    WHERE extract(YEAR FROM rt.datetime) = :year\n" +
            "                          AND extract(MONTH FROM rt.datetime) = :month\n" +
            "                          AND rt.attendance = 1\n" +
            "                          AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "                          AND s.city_id in (:ids)\n" +
            "                    GROUP BY 1, 2) r\n" +
            "             GROUP BY 1) d ON d.staff_name = r.staff_name\n" +
            "GROUP BY 1;";

    private String SQL_MASTER_GOODS = "SELECT\n" +
            "  s.name       AS staff_name,\n" +
            "  sum(gt.cost) AS res\n" +
            "FROM goods_transaction gt\n" +
            "  LEFT JOIN staff s ON gt.staff_id = s.id\n" +
            "  JOIN city c ON c.id = gt.city_id\n" +
            "WHERE gt.type_id = 1\n" +
            "      AND (gt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND extract(YEAR FROM gt.create_date) = :year\n" +
            "      AND extract(MONTH FROM gt.create_date) = :month\n" +
            "      AND s.city_id in (:ids)\n" +
            "GROUP BY 1;";

    private String SQL_MASTER_AVERAGE_GOODS = "SELECT r.staff_name, r.res / d.res as res\n" +
            "FROM (\n" +
            "SELECT\n" +
            "  s.name       AS staff_name,\n" +
            "  sum(gt.cost) AS res\n" +
            "FROM goods_transaction gt\n" +
            "  LEFT JOIN staff s ON gt.staff_id = s.id\n" +
            "  JOIN city c ON c.id = gt.city_id\n" +
            "WHERE gt.type_id = 1\n" +
            "      AND (gt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND gt.create_date >= :from\n" +
            "      AND gt.create_date < :to\n" +
            "      AND s.city_id in (:ids)\n" +
            "GROUP BY 1 ) r\n" +
            "LEFT JOIN (\n" +
            "            SELECT\n" +
            "              r.staff_name       AS staff_name,\n" +
            "              count(r.date_part) AS res\n" +
            "            FROM (\n" +
            "                   SELECT\n" +
            "                     s.name AS staff_name,\n" +
            "                     extract(MONTH FROM rt.date)\n" +
            "                   FROM record_transaction rt\n" +
            "                     JOIN city c ON c.id = rt.city_id\n" +
            "                     LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "                   WHERE rt.datetime >= :from\n" +
            "                         AND rt.datetime < :to\n" +
            "                         AND rt.attendance = 1\n" +
            "                         AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "                         AND s.city_id in (:ids)\n" +
            "                   GROUP BY 1, 2) r\n" +
            "            GROUP BY 1\n" +
            "          ) d on d.staff_name = r.staff_name;";

    private String SQL_MASTER_MONTHES_WORKED = "SELECT\n" +
            "  a.staff_name  AS staff_name,\n" +
            "  count(a.year) AS res\n" +
            "FROM (SELECT\n" +
            "        s.name                          AS staff_name,\n" +
            "        extract(YEAR FROM rt.datetime)  AS year,\n" +
            "        extract(MONTH FROM rt.datetime) AS month\n" +
            "      FROM record_transaction rt\n" +
            "        JOIN staff s ON rt.staff_id = s.id\n" +
            "        JOIN city c ON c.id = rt.city_id\n" +
            "      WHERE s.city_id IN (:ids)\n" +
            "      GROUP BY 1, 2, 3) a\n" +
            "GROUP BY 1;";

    private String SQL_MASTER_CLIENTS_TOTAL = "SELECT\n" +
            "  s.name      AS staff_name,\n" +
            "  count(*)    AS res\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON rt.staff_id = s.id\n" +
            "  LEFT JOIN city c ON c.id = rt.city_id\n" +
            "WHERE rt.attendance = 1\n" +
            "AND s.city_id IN (:ids)\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "GROUP BY 1;";

    private String SQL_MASTER_CLIENTS_MONTH = "SELECT\n" +
            "  s.name      AS staff_name,\n" +
            "  count(*)    AS res\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON rt.staff_id = s.id\n" +
            "  LEFT JOIN city c ON c.id = rt.city_id\n" +
            "WHERE rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND extract(YEAR FROM rt.datetime) = :year\n" +
            "      AND extract(MONTH FROM rt.datetime) = :month\n" +
            "      AND s.city_id IN (:ids)\n" +
            "GROUP BY 1;";

    private String SQL_MASTER_CONVERSION_3_MONTH_ALL_TIME = "SELECT\n" +
            "  a.staff_name           AS staff_name,\n" +
            "  b.res / a.res :: FLOAT AS res\n" +
            "FROM (\n" +
            "       SELECT\n" +
            "         s.name as staff_name,\n" +
            "         count(*) AS res\n" +
            "       FROM record_transaction rt\n" +
            "         LEFT JOIN staff s ON rt.staff_id = s.id\n" +
            "       WHERE rt.attendance = 1\n" +
            "             AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "             AND (rt.datetime < :to)\n" +
            "             AND s.city_id IN (:ids)\n" +
            "       GROUP BY 1) a\n" +
            "  JOIN (SELECT\n" +
            "          s.name as staff_name,\n" +
            "          count(*) AS res\n" +
            "        FROM record_transaction rt\n" +
            "          LEFT JOIN staff s ON rt.staff_id = s.id\n" +
            "        WHERE rt.attendance = 1\n" +
            "              AND rt.client_has_next_visit = 1\n" +
            "              AND rt.days_between_visits < 90\n" +
            "              AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "              AND (rt.datetime < :to)\n" +
            "              AND s.city_id IN (:ids)\n" +
            "        GROUP BY 1) b ON a.staff_name = b.staff_name\n" +
            "ORDER BY 1;";

    private String SQL_MASTER_CONVERSION_3_MONTH_3_MONTH = "SELECT\n" +
            "  a.staff_name           AS staff_name,\n" +
            "  b.res / a.res :: FLOAT AS res\n" +
            "FROM (\n" +
            "       SELECT\n" +
            "         s.name   AS staff_name,\n" +
            "         count(*) AS res\n" +
            "       FROM record_transaction rt\n" +
            "         LEFT JOIN staff s ON rt.staff_id = s.id\n" +
            "       WHERE rt.attendance = 1\n" +
            "             AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "             AND (rt.datetime >= :from)\n" +
            "             AND (rt.datetime < :to)\n" +
            "             AND s.city_id IN (:ids)\n" +
            "       GROUP BY 1) a\n" +
            "  JOIN (SELECT\n" +
            "          s.name   AS staff_name,\n" +
            "          count(*) AS res\n" +
            "        FROM record_transaction rt\n" +
            "          LEFT JOIN staff s ON rt.staff_id = s.id\n" +
            "        WHERE rt.attendance = 1\n" +
            "              AND rt.client_has_next_visit = 1\n" +
            "              AND rt.days_between_visits < 90\n" +
            "              AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "              AND (rt.datetime >= :from)\n" +
            "              AND (rt.datetime < :to)\n" +
            "              AND s.city_id IN (:ids)\n" +
            "        GROUP BY 1) b ON a.staff_name = b.staff_name\n" +
            "ORDER BY 1;";

    private RowMapper<MasterSimpleStat> masterSimpleStatRowMapper = (resultSet, i) ->
            new MasterSimpleStat(
                    resultSet.getString("staff_name"),
                    resultSet.getDouble("res")
            );

    private List<MasterSimpleStat> performQueryWithParams(String sql, Map<String, Object> params, Map<String, List<Integer>> multiMap) {
        List<MasterSimpleStat> result = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> pair : multiMap.entrySet()) {
            params.put("ids", pair.getValue());
            List<MasterSimpleStat> list = namedParameterJdbcTemplate.query(sql, params, masterSimpleStatRowMapper);
            list.forEach(masterSimpleStat -> masterSimpleStat.setCityName(pair.getKey()));
            result.addAll(list);
        }
        return result;
    }

    public List<MasterSimpleStat> getMastersDailyIncome(int year, int month, Map<String, List<Integer>> multiMap) {
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
        return performQueryWithParams(SQL_DAILY_INCOME, params, multiMap);
    }

    public List<MasterSimpleStat> getMastersGoods(int year, int month, Map<String, List<Integer>> multiMap) {
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
        return performQueryWithParams(SQL_MASTER_GOODS, params, multiMap);
    }

    public List<MasterSimpleStat> getMastersAverageGoods(int year, int month, Map<String, List<Integer>> multiMap) {
        LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0, 0).minusMonths(2);
        LocalDateTime to = LocalDateTime.of(year, month, 1, 0, 0, 0).plusMonths(1);
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
        params.put("from", from);
        params.put("to", to);
        return performQueryWithParams(SQL_MASTER_AVERAGE_GOODS, params, multiMap);
    }

    public List<MasterSimpleStat> getMastersMonthesWorked(Map<String, List<Integer>> multiMap) {
        Map<String, Object> params = new HashMap<>();
        return performQueryWithParams(SQL_MASTER_MONTHES_WORKED, params, multiMap);
    }

    public List<MasterSimpleStat> getMastersClientsTotal(Map<String, List<Integer>> multiMap) {
        Map<String, Object> params = new HashMap<>();
        return performQueryWithParams(SQL_MASTER_CLIENTS_TOTAL, params, multiMap);
    }

    public List<MasterSimpleStat> getMastersClientsMonth(int year, int month, Map<String, List<Integer>> multiMap) {
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
        return performQueryWithParams(SQL_MASTER_CLIENTS_MONTH, params, multiMap);
    }

    public List<MasterSimpleStat> getMastersConversion3MonthAllTime(int year, int month, Map<String, List<Integer>> multiMap) {
        LocalDateTime to = LocalDateTime.of(year, month, 1, 0, 0, 0).minusMonths(2);
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
        params.put("to", to);
        return performQueryWithParams(SQL_MASTER_CONVERSION_3_MONTH_ALL_TIME, params, multiMap);
    }

    public List<MasterSimpleStat> getMastersConversion3Month3Month(int year, int month, Map<String, List<Integer>> multiMap) {
        LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0, 0).minusMonths(5);
        LocalDateTime to = LocalDateTime.of(year, month, 1,  0, 0, 0).minusMonths(2);
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
        params.put("from", from);
        params.put("to", to);
        return performQueryWithParams(SQL_MASTER_CONVERSION_3_MONTH_3_MONTH, params, multiMap);
    }
}
