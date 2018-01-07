package pro.bigbro.jdbc.cities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.jdbc.StaffJdbc;

import javax.sql.DataSource;
import java.util.List;

@Component
@Lazy
public class StaffJdbcTemplate {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public StaffJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_FIND_STAFF_WITHOUT_MASTER_MARK = "select *\n" +
            "  FROM staff s\n" +
            "WHERE s.use_in_records is NULL";

    private String SQL_FIND_WORKING_STAFF = "SELECT DISTINCT s.*\n" +
            "FROM record_transaction rt\n" +
            "LEFT JOIN staff s ON s.id = rt.staff_id\n" +
            "WHERE rt.city_id = ?\n" +
            "AND (s.use_in_records LIKE '1' OR s.id IS NULL)\n" +
            "ORDER BY s.name";

    private String SQL_UPDATE_STAFF_MASTER_MARK = "UPDATE staff SET use_in_records = ?\n" +
            "WHERE id = ?";

    private RowMapper<StaffJdbc> staffJdbcRowMapper = (resultSet, i) -> {
        return new StaffJdbc(
                resultSet.getLong("id"),
                resultSet.getLong("city_id"),
                resultSet.getString("name") == null ? "Удаленные" : resultSet.getString("name"),
                resultSet.getString("title"),
                resultSet.getString("specialization"),
                resultSet.getInt("hidden"),
                resultSet.getInt("fired"),
                resultSet.getInt("status"),
                resultSet.getString("use_in_records")
        );
    };

    public List<StaffJdbc> findAllStaffWithoutMarks() {
        return jdbcTemplate.query(SQL_FIND_STAFF_WITHOUT_MASTER_MARK, staffJdbcRowMapper);
    }

    public void updateStaffMarkById(long id, String mark) {
        jdbcTemplate.update(SQL_UPDATE_STAFF_MASTER_MARK, mark, id);
    }

    public List<StaffJdbc> findWorkingStaff(int cityId) {
        return jdbcTemplate.query(SQL_FIND_WORKING_STAFF, staffJdbcRowMapper, cityId);
    }
}
