package pro.bigbro.repositories;

import org.springframework.data.repository.CrudRepository;
import pro.bigbro.models.goods.Unit;

public interface UnitRepository extends CrudRepository<Unit, Long> {
}
