package pro.bigbro.jdbc.total;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.total.DataTotal;

import javax.sql.DataSource;
import java.util.List;

@Component
@Lazy
public class DataTotalJdbcTemplate {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public DataTotalJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_FINANCE_ALL = "SELECT\n" +
            "  c.id,\n" +
            "  c.name,\n" +
            "  sum(ft.amount) as res\n" +
            "FROM financial_transaction ft\n" +
            "  JOIN city c ON c.id = ft.city_id\n" +
            "WHERE extract(YEAR FROM ft.date) = ?\n" +
            "      AND extract(MONTH FROM ft.date) = ?\n" +
            "      AND (ft.expense_id = 5 OR ft.expense_id = 7)\n" +
            "GROUP BY 1\n" +
            "ORDER BY 1";

    private String SQL_FINANCE_CATEGORY = "SELECT\n" +
            "  c.id,\n" +
            "  c.name,\n" +
            "  sum(ft.amount) as res\n" +
            "FROM financial_transaction ft\n" +
            "  JOIN city c ON c.id = ft.city_id\n" +
            "WHERE extract(YEAR FROM ft.date) = ?\n" +
            "      AND extract(MONTH FROM ft.date) = ?\n" +
            "      AND ft.expense_id = ?\n" +
            "GROUP BY 1\n" +
            "ORDER BY 1";

    private String SQL_AGE = "select r.id, r.name, count(*) as res\n" +
            "from (\n" +
            "  SELECT DISTINCT\n" +
            "    c.id,\n" +
            "    c.name,\n" +
            "    extract(YEAR FROM rt.datetime)  AS year,\n" +
            "    extract(MONTH FROM rt.datetime) AS mon\n" +
            "  FROM record_transaction rt\n" +
            "    LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "    LEFT JOIN city c ON c.id = rt.city_id\n" +
            "  WHERE rt.attendance = 1\n" +
            "        AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            ") r\n" +
            "GROUP BY 1, 2";

    private String SQL_MASTERS_COUNT = "SELECT r.id, r.name, count(r.name2) as res FROM (\n" +
            "  SELECT\n" +
            "  c.id,\n" +
            "  c.name,\n" +
            "  s.name AS name2\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  JOIN city c ON c.id = rt.city_id\n" +
            "WHERE rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND extract(YEAR FROM rt.datetime) = ?\n" +
            "      AND extract(MONTH FROM rt.datetime) = ?\n" +
            "GROUP BY 1, 2, 3) r\n" +
            "GROUP BY 1, 2";

    private String SQL_POTENTIAL = "SELECT q.cityId as id, q.cityName as name,\n" +
            "              round(sum(q.potential), 0) as res\n" +
            "            FROM (\n" +
            "              SELECT\n" +
            "                rec.cityId,\n" +
            "                rec.cityName,\n" +
            "                rec.id,\n" +
            "                rec.name,\n" +
            "                avg(rec.clients),\n" +
            "                max(rec.clients),\n" +
            "                count(rec.clients),\n" +
            "                (avg(rec.clients) + max(rec.clients)) / 2 * count(rec.clients) AS potential\n" +
            "              FROM (\n" +
            "                     SELECT\n" +
            "                       c.id as cityId,\n" +
            "                       c.name as cityName,\n" +
            "                       s.id,\n" +
            "                       s.name,\n" +
            "                       extract(DAY FROM rt.datetime)   AS day,\n" +
            "                       count(*)                        AS clients\n" +
            "                     FROM record_transaction rt\n" +
            "                       LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "                       join city c on c.id = rt.city_id\n" +
            "                     WHERE rt.attendance = 1\n" +
            "                           AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "                       and extract(YEAR FROM rt.datetime) = ?\n" +
            "                       and extract(MONTH FROM rt.datetime) = ?\n" +
            "                     GROUP BY 1, 2, 3, 4, 5\n" +
            "                   ) rec\n" +
            "              GROUP BY 1, 2, 3, 4\n" +
            "            ) q\n" +
            "            GROUP BY 1, 2";

    private String SQL_SERVICE_PRICE = "SELECT c.id, c.name, max(s.price_min) as res\n" +
            "  FROM service s\n" +
            "JOIN city c ON c.id = s.city_id\n" +
            "WHERE s.title LIKE 'Мужская стрижка'\n" +
            "GROUP BY 1, 2";

    private String SQL_AVERAGE_CUT_PRICE = "SELECT sum(r.sum) / sum(r.sum2) AS res\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  JOIN (\n" +
            "         SELECT\n" +
            "           rt.id,\n" +
            "           max(cs.cut_type) AS max,\n" +
            "           sum(cs.cost)     AS sum,\n" +
            "           sum(cs.amount)   AS sum2\n" +
            "         FROM record_transaction rt\n" +
            "           LEFT JOIN record_transaction_service_list rtsl ON rt.id = rtsl.record_transaction_id\n" +
            "           LEFT JOIN concrete_service cs ON rtsl.service_list_uid = cs.uid\n" +
            "         GROUP BY 1\n" +
            "       ) r ON r.id = rt.id\n" +
            "WHERE rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND extract(YEAR FROM rt.datetime) = ?\n" +
            "      AND extract(MONTH FROM rt.datetime) = ?\n" +
            "      AND r.max = 1";

    private String SQL_SERVICE_PART_IN_REVENUE = "SELECT q1.res1 / q2.res2 AS res\n" +
            "FROM (\n" +
            "       SELECT\n" +
            "         1          AS id,\n" +
            "         sum(r.sum) AS res1\n" +
            "       FROM record_transaction rt\n" +
            "         LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "         JOIN (\n" +
            "                SELECT DISTINCT\n" +
            "                  rt.id,\n" +
            "                  max(cs.cut_type) AS max,\n" +
            "                  sum(cs.cost)     AS sum\n" +
            "                FROM record_transaction rt\n" +
            "                  LEFT JOIN record_transaction_service_list rtsl ON rt.id = rtsl.record_transaction_id\n" +
            "                  LEFT JOIN concrete_service cs ON rtsl.service_list_uid = cs.uid\n" +
            "                GROUP BY 1\n" +
            "              ) r ON r.id = rt.id\n" +
            "       WHERE rt.attendance = 1\n" +
            "             AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "             AND extract(YEAR FROM rt.datetime) = ?\n" +
            "             AND extract(MONTH FROM rt.datetime) = ?\n" +
            "             AND r.max = 1) q1\n" +
            "  JOIN (\n" +
            "         SELECT\n" +
            "           1              AS id,\n" +
            "           sum(ft.amount) AS res2\n" +
            "         FROM financial_transaction ft\n" +
            "           JOIN city c ON c.id = ft.city_id\n" +
            "         WHERE extract(YEAR FROM ft.date) = ?\n" +
            "               AND extract(MONTH FROM ft.date) = ?\n" +
            "               AND (ft.expense_id = 5 OR ft.expense_id = 7)\n" +
            "       ) q2 ON q1.id = q2.id";

    private String SQL_AVERAGE_WORKING_MASTERS = "SELECT q.id, q.name, avg(res) as res\n" +
            "FROM (\n" +
            "SELECT r.id, r.name, r.date_part, count(r.name2) as res FROM (\n" +
            "SELECT\n" +
            "  c.id,\n" +
            "  c.name,\n" +
            "  extract(day FROM rt.datetime),\n" +
            "  s.name AS name2\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  JOIN city c ON c.id = rt.city_id\n" +
            "WHERE rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND extract(YEAR FROM rt.datetime) = ?\n" +
            "      AND extract(MONTH FROM rt.datetime) = ?\n" +
            "GROUP BY 1, 2, 3, 4) r\n" +
            "GROUP BY 1, 2, 3 ) q\n" +
            "GROUP BY 1, 2";

    private String SQL_AVERAGE_CLIENTS_PER_MASTER_PER_DAY = "SELECT r.id, r.name, avg(r.count) as res FROM (\n" +
            "SELECT\n" +
            "  c.id,\n" +
            "  c.name,\n" +
            "  extract(day FROM rt.datetime),\n" +
            "  s.name AS name2,\n" +
            "  count(*)\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  JOIN city c ON c.id = rt.city_id\n" +
            "WHERE rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND extract(YEAR FROM rt.datetime) = ?\n" +
            "      AND extract(MONTH FROM rt.datetime) = ?\n" +
            "GROUP BY 1, 2, 3, 4) r\n" +
            "GROUP BY 1, 2";

    private RowMapper<DataTotal> dataTotalRowMapper = (resultSet, i) ->
            new DataTotal(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("res")
            );

    public List<DataTotal> getFinanceAll(int year, int month) {
        return jdbcTemplate.query(SQL_FINANCE_ALL, dataTotalRowMapper, year, month);
    }

    public List<DataTotal> getFinanceServices(int year, int month) {
        return jdbcTemplate.query(SQL_FINANCE_CATEGORY, dataTotalRowMapper, year, month, 5);
    }

    public List<DataTotal> getFinanceGoods(int year, int month) {
        return jdbcTemplate.query(SQL_FINANCE_CATEGORY, dataTotalRowMapper, year, month, 7);
    }

    public List<DataTotal> getAge(int year, int month) {
        return jdbcTemplate.query(SQL_AGE, dataTotalRowMapper);
    }

    public List<DataTotal> getMastersCount(int year, int month) {
        return jdbcTemplate.query(SQL_MASTERS_COUNT, dataTotalRowMapper, year, month);
    }

    public List<DataTotal> getPotential(int year, int month) {
        return jdbcTemplate.query(SQL_POTENTIAL, dataTotalRowMapper, year, month);
    }

    public List<DataTotal> getServicePrice() {
        return jdbcTemplate.query(SQL_SERVICE_PRICE, dataTotalRowMapper);
    }

    public double getAverageCutPrice(int year, int month) {
        return jdbcTemplate.queryForObject(SQL_AVERAGE_CUT_PRICE, Double.class, year, month);
    }

    public double getServicePartInRevenue(int year, int month) {
        return jdbcTemplate.queryForObject(SQL_SERVICE_PART_IN_REVENUE, Double.class, year, month, year, month).doubleValue();
    }

    public List<DataTotal> getAverageWorkingMaster(int year, int month) {
        return jdbcTemplate.query(SQL_AVERAGE_WORKING_MASTERS, dataTotalRowMapper, year, month);
    }

    public List<DataTotal> getAverageClientPerMasterPerDay(int year, int month) {
        return jdbcTemplate.query(SQL_AVERAGE_CLIENTS_PER_MASTER_PER_DAY, dataTotalRowMapper, year, month);
    }
}
