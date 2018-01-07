package pro.bigbro.jdbc.total;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Lazy
public class GeneralJdbcTemplate {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GeneralJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_LAST_YEAR = "SELECT max(extract(YEAR FROM rt.datetime))\n" +
            "FROM record_transaction rt";

    private String SQL_LAST_MONTH = "SELECT max(extract(MONTH FROM rt.datetime))\n" +
            "FROM record_transaction rt\n" +
            "WHERE extract(YEAR FROM rt.datetime) = ?";

    public int getLastYear() {
        return jdbcTemplate.queryForObject(SQL_LAST_YEAR, Integer.class).intValue();
    }

    public int getLastMonth(int year) {
        return jdbcTemplate.queryForObject(SQL_LAST_MONTH, Integer.class, year).intValue();
    }
}
