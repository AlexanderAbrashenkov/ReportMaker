package pro.bigbro.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.MasterConversionStat;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class MasterConversionStatJdbcTemplate {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MasterConversionStatJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_CLIENTS_ALL_CUT = "SELECT\n" +
            "  s.name,\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  count(*)                        AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  JOIN (\n" +
            "         SELECT DISTINCT\n" +
            "           rt.id,\n" +
            "           max(cs.cut_type) AS max\n" +
            "         FROM record_transaction rt\n" +
            "           LEFT JOIN record_transaction_service_list rtsl ON rt.id = rtsl.record_transaction_id\n" +
            "           LEFT JOIN concrete_service cs ON rtsl.service_list_uid = cs.uid\n" +
            "         GROUP BY 1\n" +
            "       ) r ON r.id = rt.id AND r.max = 1\n" +
            "WHERE rt.city_id = ?\n" +
            "      AND rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND rt.visit_number >= ? AND rt.visit_number < ?\n" +
            "GROUP BY 1, 2, 3";

    private String SQL_CLIENTS_RETURN_CUT = "SELECT\n" +
            "  s.name,\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  count(*)                        AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "  JOIN (\n" +
            "         SELECT DISTINCT\n" +
            "           rt.id,\n" +
            "           max(cs.cut_type) AS max\n" +
            "         FROM record_transaction rt\n" +
            "           LEFT JOIN record_transaction_service_list rtsl ON rt.id = rtsl.record_transaction_id\n" +
            "           LEFT JOIN concrete_service cs ON rtsl.service_list_uid = cs.uid\n" +
            "         GROUP BY 1\n" +
            "       ) r ON r.id = rt.id AND r.max = 1\n" +
            "WHERE rt.city_id = ?\n" +
            "      AND rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND rt.visit_number >= ? AND rt.visit_number < ?\n" +
            "      AND rt.client_has_next_visit = 1\n" +
            "      AND rt.days_between_visits >= ? AND rt.days_between_visits < ?\n" +
            "GROUP BY 1, 2, 3";

    private String SQL_CLIENTS_ALL = "SELECT\n" +
            "  s.name,\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  count(*)                        AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "WHERE rt.city_id = ?\n" +
            "      AND rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "GROUP BY 1, 2, 3";

    private String SQL_CLIENTS_RETURN = "SELECT\n" +
            "  s.name,\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  count(*)                        AS clients\n" +
            "FROM record_transaction rt\n" +
            "  LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "WHERE rt.city_id = ?\n" +
            "      AND rt.attendance = 1\n" +
            "      AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            "      AND rt.client_has_next_visit = 1\n" +
            "      AND rt.days_between_visits >= ? AND rt.days_between_visits < ?\n" +
            "GROUP BY 1, 2, 3";

    private RowMapper<MasterConversionStat> masterConversionStatRowMapper = (resultSet, i) ->
            new MasterConversionStat(
                    resultSet.getString("name") == null ? "Удаленные" : resultSet.getString("name"),
                    resultSet.getInt("mon"),
                    resultSet.getInt("year"),
                    resultSet.getInt("clients")
            );

    public List<MasterConversionStat> getAllStatNk(int cityId) {
        List<MasterConversionStat> result = new ArrayList<>();
        result.addAll(getAllClientsCut(cityId, 1, 2));
        result.addAll(getReturnMiddleResultsCut(cityId, 1, 2, 0, 100000, 1));
        result.addAll(getReturnMiddleResultsCut(cityId, 1, 2, 0, 14, 14));
        result.addAll(getReturnMiddleResultsCut(cityId, 1, 2, 14, 30, 30));
        result.addAll(getReturnMiddleResultsCut(cityId, 1, 2, 30, 60, 60));
        result.addAll(getReturnMiddleResultsCut(cityId, 1, 2, 60, 90, 90));
        result.addAll(getReturnMiddleResultsCut(cityId, 1, 2,  90, 120, 120));
        result.addAll(getReturnMiddleResultsCut(cityId, 1, 2, 120, 100000, 1000));
        return result;
    }

    public List<MasterConversionStat> getAllStatPk(int cityId) {
        List<MasterConversionStat> result = new ArrayList<>();
        result.addAll(getAllClientsCut(cityId, 2, 100000));
        result.addAll(getReturnMiddleResultsCut(cityId, 2, 100000, 0, 100000, 1));
        result.addAll(getReturnMiddleResultsCut(cityId, 2, 100000, 0, 14, 14));
        result.addAll(getReturnMiddleResultsCut(cityId, 2, 100000, 14, 30, 30));
        result.addAll(getReturnMiddleResultsCut(cityId, 2, 100000, 30, 60, 60));
        result.addAll(getReturnMiddleResultsCut(cityId, 2, 100000, 60, 90, 90));
        result.addAll(getReturnMiddleResultsCut(cityId, 2, 100000,  90, 120, 120));
        result.addAll(getReturnMiddleResultsCut(cityId, 2, 100000, 120, 100000, 1000));
        return result;
    }

    public List<MasterConversionStat> getAllStatK(int cityId) {
        List<MasterConversionStat> result = new ArrayList<>();
        result.addAll(getAllClientsCut(cityId, 1, 100000));
        result.addAll(getReturnMiddleResultsCut(cityId, 1, 100000, 0, 100000, 1));
        result.addAll(getReturnMiddleResultsCut(cityId, 1, 100000, 0, 14, 14));
        result.addAll(getReturnMiddleResultsCut(cityId, 1, 100000, 14, 30, 30));
        result.addAll(getReturnMiddleResultsCut(cityId, 1, 100000, 30, 60, 60));
        result.addAll(getReturnMiddleResultsCut(cityId, 1, 100000, 60, 90, 90));
        result.addAll(getReturnMiddleResultsCut(cityId, 1, 100000,  90, 120, 120));
        result.addAll(getReturnMiddleResultsCut(cityId, 1, 100000, 120, 100000, 1000));
        return result;
    }

    public List<MasterConversionStat> getAllStatKAll(int cityId) {
        List<MasterConversionStat> result = new ArrayList<>();
        result.addAll(getAllClients(cityId));
        result.addAll(getReturnMiddleResults(cityId,0, 100000, 1));
        result.addAll(getReturnMiddleResults(cityId,0, 14, 14));
        result.addAll(getReturnMiddleResults(cityId,14, 30, 30));
        result.addAll(getReturnMiddleResults(cityId,30, 60, 60));
        result.addAll(getReturnMiddleResults(cityId,60, 90, 90));
        result.addAll(getReturnMiddleResults(cityId,90, 120, 120));
        result.addAll(getReturnMiddleResults(cityId,120, 100000, 1000));
        return result;
    }

    public List<MasterConversionStat> getAllClientsCut(int cityId, int visitFrom, int visitTo) {
        List<MasterConversionStat> clientsList = jdbcTemplate.query(SQL_CLIENTS_ALL_CUT, masterConversionStatRowMapper,
                cityId,
                visitFrom, visitTo);
        clientsList.forEach(masterConversionStat -> masterConversionStat.setComebackTimeType(0));
        return clientsList;
    }

    public List<MasterConversionStat> getReturnMiddleResultsCut(int cityId, int visitFrom, int visitTo,
                                                                int returnFrom, int returnTo, int type) {
        List<MasterConversionStat> clientsList = jdbcTemplate.query(SQL_CLIENTS_RETURN_CUT, masterConversionStatRowMapper,
                cityId,
                visitFrom, visitTo, //номер посещения
                returnFrom, returnTo); //дней до возврата
        clientsList.forEach(masterConversionStat -> masterConversionStat.setComebackTimeType(type));
        return clientsList;
    }

    public List<MasterConversionStat> getAllClients(int cityId) {
        List<MasterConversionStat> clientsList = jdbcTemplate.query(SQL_CLIENTS_ALL, masterConversionStatRowMapper, cityId);
        clientsList.forEach(masterConversionStat -> masterConversionStat.setComebackTimeType(0));
        return clientsList;
    }

    public List<MasterConversionStat> getReturnMiddleResults(int cityId, int returnFrom, int returnTo, int type) {
        List<MasterConversionStat> clientsList = jdbcTemplate.query(SQL_CLIENTS_RETURN, masterConversionStatRowMapper,
                cityId, returnFrom, returnTo); //дней до возврата
        clientsList.forEach(masterConversionStat -> masterConversionStat.setComebackTimeType(type));
        return clientsList;
    }
}
