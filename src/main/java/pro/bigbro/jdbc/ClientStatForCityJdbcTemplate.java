package pro.bigbro.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.ClientStatForCity;

import javax.sql.DataSource;
import java.util.List;

@Component
public class ClientStatForCityJdbcTemplate {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ClientStatForCityJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_FIND_TOTAL_CLIENTS_FOR_CITY = "SELECT date_part('year', rt.datetime) as year, date_part('month', rt.datetime) as mon, count(*) as clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  WHERE rt.city_id = ?\n" +
            "  and rt.attendance = 1\n" +
            "  AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "GROUP BY 1, 2\n" +
            "ORDER BY 1, 2";

    private String SQL_FIND_CLIENTS_FOR_CITY_BY_VISIT = "SELECT\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  count(*)                        AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "WHERE rt.city_id = ?\n" +
            "      AND rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND rt.visit_number >= ? AND rt.visit_number < ?\n" +
            "GROUP BY 1, 2\n" +
            "ORDER BY 1, 2";

    private String SQL_FIND_TOTAL_CLIENTS_FOR_CITY_CUT = "SELECT\n" +
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
            "GROUP BY 1, 2\n" +
            "ORDER BY 1, 2";

    private String SQL_FIND_CLIENTS_FOR_CITY_BY_VISIT_CUT = "SELECT\n" +
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
            "GROUP BY 1, 2\n" +
            "ORDER BY 1, 2";

    private String SQL_FIND_ANONIM_CLIENTS_FOR_CITY = "SELECT\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  count(*)                        AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "WHERE rt.city_id = ?\n" +
            "      AND rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND rt.client_id IS NULL\n" +
            "GROUP BY 1, 2\n" +
            "ORDER BY 1, 2";

    private String SQL_FIND_CLIENTS_WITHOUT_LINKS_FOR_CITY = "SELECT\n" +
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
            "GROUP BY 1, 2\n" +
            "ORDER BY 1, 2";

    private RowMapper<ClientStatForCity> clientStatForCityRowMapper = (resultSet, i) ->
            new ClientStatForCity(
                    resultSet.getInt("mon"),
                    resultSet.getInt("year"),
                    resultSet.getInt("clients")
            );

    public List<ClientStatForCity> findAllClientsForCity(int cityId) {
        return jdbcTemplate.query(SQL_FIND_TOTAL_CLIENTS_FOR_CITY, clientStatForCityRowMapper, cityId);
    }

    public List<ClientStatForCity> findClientsForCityByVisitNum(int cityId, int visitFrom, int visitTo) {
        return jdbcTemplate.query(SQL_FIND_CLIENTS_FOR_CITY_BY_VISIT, clientStatForCityRowMapper,
                cityId, visitFrom, visitTo);
    }

    public List<ClientStatForCity> findAllClientsForCityCut(int cityId) {
        return jdbcTemplate.query(SQL_FIND_TOTAL_CLIENTS_FOR_CITY_CUT, clientStatForCityRowMapper, cityId);
    }

    public List<ClientStatForCity> findClientsForCityByVisitNumCut(int cityId, int visitFrom, int visitTo) {
        return jdbcTemplate.query(SQL_FIND_CLIENTS_FOR_CITY_BY_VISIT_CUT, clientStatForCityRowMapper,
                cityId, visitFrom, visitTo);
    }

    public List<ClientStatForCity> findAnonimClientsForCity(int cityId) {
        return jdbcTemplate.query(SQL_FIND_ANONIM_CLIENTS_FOR_CITY, clientStatForCityRowMapper, cityId);
    }

    public List<ClientStatForCity> findClientsWithoutLinksForCity(int cityId) {
        return jdbcTemplate.query(SQL_FIND_CLIENTS_WITHOUT_LINKS_FOR_CITY, clientStatForCityRowMapper, cityId);
    }
}
