package pro.bigbro.jdbc.cities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import pro.bigbro.models.jdbc.ServiceTypeJdbc;
import pro.bigbro.models.jdbc.StaffJdbc;

import javax.sql.DataSource;
import java.util.List;

@Component
@Lazy
public class ServiceTypeJdbcTemplate {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ServiceTypeJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_FIND_SERVICES_WITHOUT_MARK = "SELECT DISTINCT cs.service_id, cs.title\n" +
            "FROM concrete_service cs\n" +
            "WHERE cs.cut_type is NULL or cs.cut_type = 0";

    private String SQL_UPDATE_SERVICE_MARK = "UPDATE concrete_service SET cut_type = ?\n" +
            "WHERE service_id = ?";

    private RowMapper<ServiceTypeJdbc> serviceTypeJdbcRowMapper = (resultSet, i) -> {
        return new ServiceTypeJdbc(
                resultSet.getLong("service_id"),
                resultSet.getString("title")
        );
    };

    public List<ServiceTypeJdbc> findAllServicesWithoutMarks() {
        return jdbcTemplate.query(SQL_FIND_SERVICES_WITHOUT_MARK, serviceTypeJdbcRowMapper);
    }

    public void updateServicesMarksById(long id, int mark) {
        jdbcTemplate.update(SQL_UPDATE_SERVICE_MARK, mark, id);
    }
}
