package pro.bigbro.repositories;

import org.springframework.data.repository.CrudRepository;
import pro.bigbro.models.services.ConcreteService;

public interface ConcreteServiceRepository extends CrudRepository<ConcreteService, Long> {
}
