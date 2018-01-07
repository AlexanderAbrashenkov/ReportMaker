package pro.bigbro.repositories;

import org.springframework.data.repository.CrudRepository;
import pro.bigbro.models.services.Service;

public interface ServiceRepository extends CrudRepository<Service, Long> {
}
