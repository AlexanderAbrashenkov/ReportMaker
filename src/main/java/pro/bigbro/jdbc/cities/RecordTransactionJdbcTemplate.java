package pro.bigbro.jdbc.cities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Lazy
public class RecordTransactionJdbcTemplate {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RecordTransactionJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_UPDATE_VISIT_ORDINAL_NUMBER = "WITH numbered AS (\n" +
            "    SELECT\n" +
            "      rt.datetime,\n" +
            "      rt.client_id,\n" +
            "      row_number()\n" +
            "      OVER (\n" +
            "        PARTITION BY rt.client_id\n" +
            "        ORDER BY rt.datetime ) AS rn,\n" +
            "      rt.ctid                  AS id\n" +
            "    FROM record_transaction rt\n" +
            "      LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "    WHERE rt.attendance = 1\n" +
            "          AND (rt.staff_id IS NULL OR s.use_in_records LIKE '1')\n" +
            ")\n" +
            "UPDATE record_transaction\n" +
            "SET visit_number = n.rn\n" +
            "FROM numbered n\n" +
            "WHERE n.id = record_transaction.ctid;";

    private String SQL_UPDATE_VISIT_ANONIM_CLIENT = "UPDATE record_transaction rt\n" +
            "SET visit_number = 1\n" +
            "WHERE rt.client_id IS NULL";

    private String SQL_UPDATE_CLIENT_RETURN = "UPDATE record_transaction\n" +
            "SET client_has_next_visit = 1\n" +
            "FROM (\n" +
            "       SELECT\n" +
            "         r.client_id,\n" +
            "         max(r.visit_number) AS max_visit\n" +
            "       FROM record_transaction r\n" +
            "       WHERE r.client_id IS NOT NULL\n" +
            "       GROUP BY r.client_id\n" +
            "     ) a\n" +
            "WHERE record_transaction.client_id = a.client_id\n" +
            "      AND record_transaction.visit_number < a.max_visit\n" +
            "      AND record_transaction.visit_number > 0";

    private String SQL_UPDATE_RETURN_TIME = "UPDATE record_transaction AS rt\n" +
            "SET days_between_visits = res.days\n" +
            "FROM (\n" +
            "       SELECT\n" +
            "         r.id,\n" +
            "         date_part('day', r2.datetime - r.datetime) AS days\n" +
            "       FROM record_transaction r\n" +
            "         JOIN record_transaction r2 ON r.client_id = r2.client_id AND r.visit_number = r2.visit_number - 1\n" +
            "       WHERE r.client_has_next_visit = 1\n" +
            "     ) res\n" +
            "WHERE rt.id = res.id";


    public void updateVisitOrdinalNumber() {
        jdbcTemplate.execute(SQL_UPDATE_VISIT_ORDINAL_NUMBER);
    }

    public void updateVisitAnonimNumber() {
        jdbcTemplate.execute(SQL_UPDATE_VISIT_ANONIM_CLIENT);
    }

    public void updateClientsWereReturned() {
        jdbcTemplate.execute(SQL_UPDATE_CLIENT_RETURN);
    }

    public void updateReturnTime() {
        jdbcTemplate.execute(SQL_UPDATE_RETURN_TIME);
    }
}
