package pro.bigbro.repositories;

import org.springframework.data.repository.CrudRepository;
import pro.bigbro.models.goods.GoodsTransaction;

public interface GoodsTransactionRepository extends CrudRepository<GoodsTransaction, Long> {
}
