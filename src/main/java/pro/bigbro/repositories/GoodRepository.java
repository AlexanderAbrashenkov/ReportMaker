package pro.bigbro.repositories;

import org.springframework.data.repository.CrudRepository;
import pro.bigbro.models.goods.Good;

public interface GoodRepository extends CrudRepository<Good, Long> {
}
