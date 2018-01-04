package pro.bigbro.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class ClientJdbcTemplate {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ClientJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private String SQL_UPDATE_CLIENTS_HAVE_LINKS = "UPDATE client\n" +
            "SET has_link = 1\n" +
            "WHERE comment LIKE '%http%vk.com%'\n" +
            "      OR email LIKE '%http%vk.com%'";

    public void updateClientHasLink() {
        jdbcTemplate.execute(SQL_UPDATE_CLIENTS_HAVE_LINKS);
    }
}
