package pro.bigbro.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pro.bigbro.models.clients.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
