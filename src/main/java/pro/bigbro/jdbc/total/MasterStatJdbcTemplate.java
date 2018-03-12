package pro.bigbro.jdbc.total;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.total.MasterStat;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Lazy
public class MasterStatJdbcTemplate {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public MasterStatJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_DAILY_INCOME = "SELECT\n" +
            "  r.city_id,\n" +
            "  r.city_name,\n" +
            "  r.staff_id,\n" +
            "  r.staff_name,\n" +
            "  (coalesce(sum(r.res), 0) + coalesce(sum(g.res), 0)) / sum(d.res) AS res\n" +
            "FROM (SELECT\n" +
            "        c.id         AS city_id,\n" +
            "        c.name       AS city_name,\n" +
            "        s.id         AS staff_id,\n" +
            "        s.name       AS staff_name,\n" +
            "        sum(cs.cost) AS res\n" +
            "      FROM record_transaction rt\n" +
            "        LEFT JOIN record_transaction_service_list rtsl ON rt.id = rtsl.record_transaction_id\n" +
            "        LEFT JOIN concrete_service cs ON rtsl.service_list_uid = cs.uid\n" +
            "        JOIN city c ON c.id = rt.city_id\n" +
            "        LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "      WHERE extract(YEAR FROM rt.datetime) = ?\n" +
            "            AND extract(MONTH FROM rt.datetime) = ?\n" +
            "            AND rt.attendance = 1\n" +
            "            AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      GROUP BY 1, 2, 3, 4) r\n" +
            "  LEFT JOIN (\n" +
            "              SELECT\n" +
            "                c.id         AS city_id,\n" +
            "                c.name       AS city_name,\n" +
            "                s.id         AS staff_id,\n" +
            "                s.name       AS staff_name,\n" +
            "                sum(gt.cost) AS res\n" +
            "              FROM goods_transaction gt\n" +
            "                LEFT JOIN staff s ON gt.staff_id = s.id\n" +
            "                JOIN city c ON c.id = gt.city_id\n" +
            "              WHERE gt.type_id = 1\n" +
            "                    AND (gt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "                    AND extract(YEAR FROM gt.create_date) = ?\n" +
            "                    AND extract(MONTH FROM gt.create_date) = ?\n" +
            "              GROUP BY 1, 2, 3, 4\n" +
            "            ) g ON g.city_id = r.city_id AND g.staff_id = r.staff_id\n" +
            "  LEFT JOIN (SELECT\n" +
            "               r.city_id          AS city_id,\n" +
            "               r.city_name        AS city_name,\n" +
            "               r.staff_id         AS staff_id,\n" +
            "               r.staff_name       AS staff_name,\n" +
            "               count(r.date_part) AS res\n" +
            "             FROM (\n" +
            "                    SELECT\n" +
            "                      c.id   AS city_id,\n" +
            "                      c.name AS city_name,\n" +
            "                      s.id   AS staff_id,\n" +
            "                      s.name AS staff_name,\n" +
            "                      extract(DAY FROM rt.date)\n" +
            "                    FROM record_transaction rt\n" +
            "                      JOIN city c ON c.id = rt.city_id\n" +
            "                      LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "                    WHERE extract(YEAR FROM rt.datetime) = ?\n" +
            "                          AND extract(MONTH FROM rt.datetime) = ?\n" +
            "                          AND rt.attendance = 1\n" +
            "                          AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "                    GROUP BY 1, 2, 3, 4, 5) r\n" +
            "             GROUP BY 1, 2, 3, 4) d ON d.city_id = r.city_id AND d.staff_id = r.staff_id\n" +
            "GROUP BY 1, 2, 3, 4";

    private String SQL_MASTER_GOODS = "SELECT\n" +
            "  c.id         AS city_id,\n" +
            "  c.name       AS city_name,\n" +
            "  s.id         AS staff_id,\n" +
            "  s.name       AS staff_name,\n" +
            "  sum(gt.cost) AS res\n" +
            "FROM goods_transaction gt\n" +
            "  LEFT JOIN staff s ON gt.staff_id = s.id\n" +
            "  JOIN city c ON c.id = gt.city_id\n" +
            "WHERE gt.type_id = 1\n" +
            "      AND (gt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND extract(YEAR FROM gt.create_date) = ?\n" +
            "      AND extract(MONTH FROM gt.create_date) = ?\n" +
            "GROUP BY 1, 2, 3, 4";

    private String SQL_MASTER_AVERAGE_GOODS = "SELECT r.city_id, r.city_name, r.staff_id, r.staff_name, r.res / d.res as res\n" +
            "FROM (\n" +
            "SELECT\n" +
            "  c.id         AS city_id,\n" +
            "  c.name       AS city_name,\n" +
            "  s.id         AS staff_id,\n" +
            "  s.name       AS staff_name,\n" +
            "  sum(gt.cost) AS res\n" +
            "FROM goods_transaction gt\n" +
            "  LEFT JOIN staff s ON gt.staff_id = s.id\n" +
            "  JOIN city c ON c.id = gt.city_id\n" +
            "WHERE gt.type_id = 1\n" +
            "      AND (gt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND gt.create_date >= ?\n" +
            "      AND gt.create_date < ?\n" +
            "GROUP BY 1, 2, 3, 4 ) r\n" +
            "LEFT JOIN (\n" +
            "            SELECT\n" +
            "              r.city_id          AS city_id,\n" +
            "              r.city_name        AS city_name,\n" +
            "              r.staff_id         AS staff_id,\n" +
            "              r.staff_name       AS staff_name,\n" +
            "              count(r.date_part) AS res\n" +
            "            FROM (\n" +
            "                   SELECT\n" +
            "                     c.id   AS city_id,\n" +
            "                     c.name AS city_name,\n" +
            "                     s.id   AS staff_id,\n" +
            "                     s.name AS staff_name,\n" +
            "                     extract(MONTH FROM rt.date)\n" +
            "                   FROM record_transaction rt\n" +
            "                     JOIN city c ON c.id = rt.city_id\n" +
            "                     LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "                   WHERE rt.datetime >= ?\n" +
            "                         AND rt.datetime < ?\n" +
            "                         AND rt.attendance = 1\n" +
            "                         AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "                   GROUP BY 1, 2, 3, 4, 5) r\n" +
            "            GROUP BY 1, 2, 3, 4\n" +
            "          ) d on d.city_id = r.city_id and d.staff_id = r.staff_id";

    private String SQL_MASTER_MONTHES_WORKED = "SELECT c.id as city_id, c.name as city_name, s.id as staff_id, s.name as staff_name, count(a.year) as res\n" +
            "FROM (SELECT rt.staff_id, rt.city_id, extract(YEAR FROM rt.datetime) as year, extract(MONTH FROM rt.datetime) as month\n" +
            "FROM record_transaction rt\n" +
            "GROUP BY 1, 2, 3, 4) a\n" +
            "  JOIN city c on c.id = a.city_id\n" +
            "  JOIN staff s on s.id = a.staff_id\n" +
            "GROUP BY 1, 2, 3, 4";

    private String SQL_MASTER_CLIENTS_TOTAL = "SELECT\n" +
            "  rt.city_id as city_id,   c.name as city_name, rt.staff_id as staff_id, s.name as staff_name, count(*) AS res\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON rt.staff_id = s.id\n" +
            "  LEFT JOIN city c on c.id = rt.city_id\n" +
            "WHERE rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "GROUP BY 1, 2, 3, 4";

    private String SQL_MASTER_CLIENTS_MONTH = "SELECT\n" +
            "  rt.city_id as city_id,   c.name as city_name, rt.staff_id as staff_id, s.name as staff_name, count(*) AS res\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON rt.staff_id = s.id\n" +
            "  LEFT JOIN city c on c.id = rt.city_id\n" +
            "WHERE rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "  and extract(YEAR FROM rt.datetime) = ?\n" +
            "  and extract(MONTH FROM rt.datetime) = ?\n" +
            "GROUP BY 1, 2, 3, 4";

    private String SQL_MASTER_CONVERSION_3_MONTH_ALL_TIME = "SELECT a.city_id as city_id, " +
            "c.name as city_name, a.staff_id as staff_id, s.name as staff_name,\n" +
            "  b.res / a.res :: FLOAT as res\n" +
            "  from (\n" +
            "SELECT\n" +
            "  rt.city_id, rt.staff_id, count(*) AS res\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON rt.staff_id = s.id\n" +
            "WHERE rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "  and (rt.datetime < ?)\n" +
            "GROUP BY 1, 2) a\n" +
            "JOIN (SELECT\n" +
            "  rt.city_id, rt.staff_id, count(*) AS res\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON rt.staff_id = s.id\n" +
            "WHERE rt.attendance = 1\n" +
            "  and rt.client_has_next_visit = 1\n" +
            "  and rt.days_between_visits < 90\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      and (rt.datetime < ?)\n" +
            "GROUP BY 1, 2) b on a.city_id = b.city_id and a.staff_id = b.staff_id\n" +
            "JOIN staff s on s.id = a.staff_id\n" +
            "JOIN city c on c.id = a.city_id\n" +
            "ORDER BY 2, 4";

    private String SQL_MASTER_CONVERSION_3_MONTH_3_MONTH = "SELECT a.city_id as city_id, " +
            "c.name as city_name, a.staff_id as staff_id, s.name as staff_name,\n" +
            "  b.res / a.res :: FLOAT as res\n" +
            "  from (\n" +
            "SELECT\n" +
            "  rt.city_id, rt.staff_id, count(*) AS res\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON rt.staff_id = s.id\n" +
            "WHERE rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "  and (rt.datetime >= ?)\n" +
            "  and (rt.datetime < ?)\n" +
            "GROUP BY 1, 2) a\n" +
            "JOIN (SELECT\n" +
            "  rt.city_id, rt.staff_id, count(*) AS res\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON rt.staff_id = s.id\n" +
            "WHERE rt.attendance = 1\n" +
            "  and rt.client_has_next_visit = 1\n" +
            "  and rt.days_between_visits < 90\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      and (rt.datetime >= ?)\n" +
            "      and (rt.datetime < ?)\n" +
            "GROUP BY 1, 2) b on a.city_id = b.city_id and a.staff_id = b.staff_id\n" +
            "JOIN staff s on s.id = a.staff_id\n" +
            "JOIN city c on c.id = a.city_id\n" +
            "ORDER BY 2, 4";

    private RowMapper<MasterStat> masterStatRowMapper = (resultSet, i) ->
            new MasterStat(
                    resultSet.getInt("city_id"),
                    resultSet.getString("city_name"),
                    resultSet.getInt("staff_id"),
                    resultSet.getString("staff_name"),
                    resultSet.getDouble("res")
            );

    public List<MasterStat> getMastersDailyIncome(int year, int month) {
        return jdbcTemplate.query(SQL_DAILY_INCOME, masterStatRowMapper, year, month, year, month, year, month);
    }

    public List<MasterStat> getMastersGoods(int year, int month) {
        return jdbcTemplate.query(SQL_MASTER_GOODS, masterStatRowMapper, year, month);
    }

    public List<MasterStat> getMastersAverageGoods(int year, int month) {
        LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0, 0)
                .minusMonths(2);
        LocalDateTime to = LocalDateTime.of(year, month, 1, 0, 0, 0)
                .plusMonths(1);
        return jdbcTemplate.query(SQL_MASTER_AVERAGE_GOODS, masterStatRowMapper, from, to, from, to);
    }

    public List<MasterStat> getMastersMonthesWorked() {
        return jdbcTemplate.query(SQL_MASTER_MONTHES_WORKED, masterStatRowMapper);
    }

    public List<MasterStat> getMastersClientsTotal() {
        return jdbcTemplate.query(SQL_MASTER_CLIENTS_TOTAL, masterStatRowMapper);
    }

    public List<MasterStat> getMastersClientsMonth(int year, int month) {
        return jdbcTemplate.query(SQL_MASTER_CLIENTS_MONTH, masterStatRowMapper, year, month);
    }

    public List<MasterStat> getMastersConversion3MonthAllTime(int year, int month) {
        LocalDateTime to = LocalDateTime.of(year, month, 1, 0, 0, 0)
                .minusMonths(2);
        return jdbcTemplate.query(SQL_MASTER_CONVERSION_3_MONTH_ALL_TIME, masterStatRowMapper, to, to);
    }

    public List<MasterStat> getMastersConversion3Month3Month(int year, int month) {
        LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0, 0)
                .minusMonths(5);
        LocalDateTime to = LocalDateTime.of(year, month, 1,  0, 0, 0)
                .minusMonths(2);
        return jdbcTemplate.query(SQL_MASTER_CONVERSION_3_MONTH_3_MONTH, masterStatRowMapper, from, to, from, to);
    }
}
