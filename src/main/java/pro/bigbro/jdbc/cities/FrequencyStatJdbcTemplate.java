package pro.bigbro.jdbc.cities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.cities.FrequencyStat;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
@Lazy
public class FrequencyStatJdbcTemplate {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public FrequencyStatJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_RETURN_TIME_BY_MASTERS_CUT = "SELECT\n" +
            "  s.name,\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  avg(rt.days_between_visits)     AS avgTime\n" +
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
            "      AND rt.client_has_next_visit = 1\n" +
            "      AND rt.visit_number >= ? AND rt.visit_number < ?\n" +
            "      AND rt.days_between_visits >= ? AND rt.days_between_visits < ?\n" +
            "GROUP BY 1, 2, 3";

    private String SQL_RETURN_CLIENT_BY_MASTERS_CUT = "SELECT\n" +
            "  s.name,\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  count(*)     AS avgTime\n" +
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
            "      AND rt.client_has_next_visit = 1\n" +
            "      AND rt.visit_number >= ? AND rt.visit_number < ?\n" +
            "      AND rt.days_between_visits >= ? AND rt.days_between_visits < ?\n" +
            "GROUP BY 1, 2, 3";

    private String SQL_RETURN_TIME_ALL_CUT = "SELECT\n" +
            "'По всем мастерам' as name,\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  avg(rt.days_between_visits)     AS avgTime\n" +
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
            "      AND rt.client_has_next_visit = 1\n" +
            "      AND rt.visit_number >= ? AND rt.visit_number < ?\n" +
            "      AND rt.days_between_visits >= ? AND rt.days_between_visits < ?\n" +
            "GROUP BY 1, 2, 3";

    private String SQL_RETURN_CLIENT_ALL_CUT = "SELECT\n" +
            "'По всем мастерам' as name,\n" +
            "  date_part('year', rt.datetime)  AS year,\n" +
            "  date_part('month', rt.datetime) AS mon,\n" +
            "  count(*)     AS avgTime\n" +
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
            "      AND rt.client_has_next_visit = 1\n" +
            "      AND rt.visit_number >= ? AND rt.visit_number < ?\n" +
            "      AND rt.days_between_visits >= ? AND rt.days_between_visits < ?\n" +
            "GROUP BY 1, 2, 3";

    private RowMapper<FrequencyStat> returnTimeStatRowMapper = (resultSet, i) ->
            new FrequencyStat(
                    resultSet.getString("name") == null ? "Удаленные" : resultSet.getString("name"),
                    resultSet.getInt("mon"),
                    resultSet.getInt("year"),
                    resultSet.getDouble("avgTime")
            );

    public List<FrequencyStat> getAllStats(int cityId) {
        List<FrequencyStat> result = new ArrayList<>();

        result.addAll(getReturnClientTimeAllCut(cityId, 1, 100000, 0, 100000, 1, 3, 1));
        result.addAll(getReturnClientTimeAllCut(cityId, 1, 2, 0, 100000, 1, 1, 1));
        result.addAll(getReturnClientTimeAllCut(cityId, 2, 100000, 0, 100000, 1, 2, 1));
        result.addAll(getReturnClientTimeAllCut(cityId, 1, 100000, 90, 100000, 2, 3, 1));
        result.addAll(getReturnClientTimeAllCut(cityId, 1, 2, 90, 100000, 2, 1, 1));
        result.addAll(getReturnClientTimeAllCut(cityId, 2, 100000, 90, 100000, 2, 2, 1));

        result.addAll(getReturnClientTimeAllCut(cityId, 1, 100000, 0, 90, 1, 3, 2));
        result.addAll(getReturnClientTimeAllCut(cityId, 1, 2, 0, 90, 1, 1, 2));
        result.addAll(getReturnClientTimeAllCut(cityId, 2, 100000, 0, 90, 1, 2, 2));
        result.addAll(getReturnClientTimeAllCut(cityId, 1, 100000, 0, 100000, 2, 3, 2));
        result.addAll(getReturnClientTimeAllCut(cityId, 1, 2, 0, 100000, 2, 1, 2));
        result.addAll(getReturnClientTimeAllCut(cityId, 2, 100000, 0, 100000, 2, 2, 2));

        result.addAll(getReturnClientTimeMiddleResultsCut(cityId, 1, 100000, 0, 100000, 1, 3, 1));
        result.addAll(getReturnClientTimeMiddleResultsCut(cityId, 1, 2, 0, 100000, 1, 1, 1));
        result.addAll(getReturnClientTimeMiddleResultsCut(cityId, 2, 100000, 0, 100000, 1, 2, 1));
        result.addAll(getReturnClientTimeMiddleResultsCut(cityId, 1, 100000, 90, 100000, 2, 3, 1));
        result.addAll(getReturnClientTimeMiddleResultsCut(cityId, 1, 2, 90, 100000, 2, 1, 1));
        result.addAll(getReturnClientTimeMiddleResultsCut(cityId, 2, 100000, 90, 100000, 2, 2, 1));

        result.addAll(getReturnClientTimeMiddleResultsCut(cityId, 1, 100000, 0, 90, 1, 3, 2));
        result.addAll(getReturnClientTimeMiddleResultsCut(cityId, 1, 2, 0, 90, 1, 1, 2));
        result.addAll(getReturnClientTimeMiddleResultsCut(cityId, 2, 100000, 0, 90, 1, 2, 2));
        result.addAll(getReturnClientTimeMiddleResultsCut(cityId, 1, 100000, 0, 100000, 2, 3, 2));
        result.addAll(getReturnClientTimeMiddleResultsCut(cityId, 1, 2, 0, 100000, 2, 1, 2));
        result.addAll(getReturnClientTimeMiddleResultsCut(cityId, 2, 100000, 0, 100000, 2, 2, 2));

        return result;
    }

    private List<FrequencyStat> getReturnClientTimeMiddleResultsCut(int cityId, int visitFrom, int visitTo,
                                                             int returnFrom, int returnTo, int type, int clientType, int reportType) {
        String sql = reportType == 1 ? SQL_RETURN_CLIENT_BY_MASTERS_CUT : SQL_RETURN_TIME_BY_MASTERS_CUT;
        List<FrequencyStat> frequencyStatList = jdbcTemplate.query(sql, returnTimeStatRowMapper,
                cityId,
                visitFrom, visitTo, //номер посещения
                returnFrom, returnTo); //дней до возврата
        frequencyStatList.forEach(frequencyStat -> frequencyStat.setComebackTimeType(type));
        frequencyStatList.forEach(frequencyStat -> frequencyStat.setClientType(clientType));
        frequencyStatList.forEach(frequencyStat -> frequencyStat.setReportType(reportType));
        return frequencyStatList;
    }

    private List<FrequencyStat> getReturnClientTimeAllCut(int cityId, int visitFrom, int visitTo,
                                                                   int returnFrom, int returnTo, int type, int clientType, int reportType) {
        String sql = reportType == 1 ? SQL_RETURN_CLIENT_ALL_CUT : SQL_RETURN_TIME_ALL_CUT;
        List<FrequencyStat> frequencyStatList = jdbcTemplate.query(sql, returnTimeStatRowMapper,
                cityId,
                visitFrom, visitTo, //номер посещения
                returnFrom, returnTo); //дней до возврата
        frequencyStatList.forEach(frequencyStat -> frequencyStat.setComebackTimeType(type));
        frequencyStatList.forEach(frequencyStat -> frequencyStat.setClientType(clientType));
        frequencyStatList.forEach(frequencyStat -> frequencyStat.setReportType(reportType));
        return frequencyStatList;
    }
}
