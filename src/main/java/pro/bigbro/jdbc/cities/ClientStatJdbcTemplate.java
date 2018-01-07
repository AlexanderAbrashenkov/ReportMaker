package pro.bigbro.jdbc.cities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.cities.ClientStat;

import javax.sql.DataSource;
import java.util.List;

@Component
@Lazy
public class ClientStatJdbcTemplate {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ClientStatJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_FIND_TOTAL_CLIENTS = "SELECT date_part('year', rt.datetime) as year, date_part('month', rt.datetime) as mon, count(*) as clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  WHERE rt.city_id = ?\n" +
            "  and rt.attendance = 1\n" +
            "  AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "GROUP BY 1, 2";

    private String SQL_FIND_CLIENTS_BY_VISIT = "SELECT\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  count(*)                        AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "WHERE rt.city_id = ?\n" +
            "      AND rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND rt.visit_number >= ? AND rt.visit_number < ?\n" +
            "GROUP BY 1, 2";

    private String SQL_FIND_TOTAL_CLIENTS_CUT = "SELECT\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  count(*)                        AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  JOIN (\n" +
            "    SELECT DISTINCT rt.id, max(cs.cut_type) as max\n" +
            "    FROM record_transaction rt\n" +
            "      LEFT JOIN record_transaction_service_list rtsl on rt.id = rtsl.record_transaction_id\n" +
            "      LEFT JOIN concrete_service cs on rtsl.service_list_uid = cs.uid\n" +
            "    GROUP BY 1\n" +
            "    ) r on r.id = rt.id\n" +
            "WHERE rt.city_id = ?\n" +
            "      AND rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "  and r.max = 1\n" +
            "GROUP BY 1, 2";

    private String SQL_FIND_CLIENTS_BY_VISIT_CUT = "SELECT\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  count(*)                        AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  JOIN (\n" +
            "    SELECT DISTINCT rt.id, max(cs.cut_type) as max\n" +
            "    FROM record_transaction rt\n" +
            "      LEFT JOIN record_transaction_service_list rtsl on rt.id = rtsl.record_transaction_id\n" +
            "      LEFT JOIN concrete_service cs on rtsl.service_list_uid = cs.uid\n" +
            "    GROUP BY 1\n" +
            "    ) r on r.id = rt.id\n" +
            "WHERE rt.city_id = ?\n" +
            "      AND rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND rt.visit_number >= ? AND rt.visit_number < ?\n" +
            "  and r.max = 1\n" +
            "GROUP BY 1, 2";

    private String SQL_FIND_ANONIM_CLIENTS = "SELECT\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  count(*)                        AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "WHERE rt.city_id = ?\n" +
            "      AND rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND rt.client_id IS NULL\n" +
            "GROUP BY 1, 2";

    private String SQL_FIND_CLIENTS_WITHOUT_LINKS = "SELECT\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  count(*)                        AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  LEFT JOIN client c ON c.id = rt.client_id\n" +
            "WHERE rt.city_id = ?\n" +
            "      AND rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND (rt.client_id is null or c.has_link = 0)\n" +
            "GROUP BY 1, 2";

    private String SQL_COUNT_POTENTIALS = "SELECT q.year, q.mon,\n" +
            "  round(sum(q.potential), 0) as clients\n" +
            "FROM (\n" +
            "  SELECT\n" +
            "    rec.id,\n" +
            "    rec.name,\n" +
            "    rec.year,\n" +
            "    rec.mon,\n" +
            "    avg(rec.clients),\n" +
            "    max(rec.clients),\n" +
            "    count(rec.clients),\n" +
            "    (avg(rec.clients) + max(rec.clients)) / 2 * count(rec.clients) AS potential\n" +
            "  FROM (\n" +
            "         SELECT\n" +
            "           s.id,\n" +
            "           s.name,\n" +
            "           extract(YEAR FROM rt.datetime)  AS year,\n" +
            "           extract(MONTH FROM rt.datetime) AS mon,\n" +
            "           extract(DAY FROM rt.datetime)   AS day,\n" +
            "           count(*)                        AS clients\n" +
            "         FROM record_transaction rt\n" +
            "           LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "         WHERE rt.city_id = ?\n" +
            "               AND rt.attendance = 1\n" +
            "               AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "         GROUP BY 1, 2, 3, 4, 5\n" +
            "       ) rec\n" +
            "  GROUP BY 1, 2, 3, 4\n" +
            ") q\n" +
            "GROUP BY 1, 2";

    private RowMapper<ClientStat> clientStatRowMapper = (resultSet, i) ->
            new ClientStat(
                    resultSet.getInt("mon"),
                    resultSet.getInt("year"),
                    resultSet.getInt("clients")
            );

    public List<ClientStat> findAllClients(int cityId) {
        return jdbcTemplate.query(SQL_FIND_TOTAL_CLIENTS, clientStatRowMapper, cityId);
    }

    public List<ClientStat> findClientsByVisitNum(int cityId, int visitFrom, int visitTo) {
        return jdbcTemplate.query(SQL_FIND_CLIENTS_BY_VISIT, clientStatRowMapper,
                cityId, visitFrom, visitTo);
    }

    public List<ClientStat> findAllClientsCut(int cityId) {
        return jdbcTemplate.query(SQL_FIND_TOTAL_CLIENTS_CUT, clientStatRowMapper, cityId);
    }

    public List<ClientStat> findClientsByVisitNumCut(int cityId, int visitFrom, int visitTo) {
        return jdbcTemplate.query(SQL_FIND_CLIENTS_BY_VISIT_CUT, clientStatRowMapper,
                cityId, visitFrom, visitTo);
    }

    public List<ClientStat> findAnonimClients(int cityId) {
        return jdbcTemplate.query(SQL_FIND_ANONIM_CLIENTS, clientStatRowMapper, cityId);
    }

    public List<ClientStat> findClientsWithoutLinks(int cityId) {
        return jdbcTemplate.query(SQL_FIND_CLIENTS_WITHOUT_LINKS, clientStatRowMapper, cityId);
    }

    public List<ClientStat> findPotential(int cityId) {
        return jdbcTemplate.query(SQL_COUNT_POTENTIALS, clientStatRowMapper, cityId);
    }
}
