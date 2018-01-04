package pro.bigbro.restTemplates;

import pro.bigbro.models.goods.GoodsTransaction;

import java.time.LocalDate;
import java.util.List;

public interface GoodsTransactionRestTemplate {
    List<GoodsTransaction> getAllGoodsTransactions(LocalDate startDate, LocalDate endDate);
}
