package pro.bigbro.restTemplates;

import pro.bigbro.models.cities.City;
import pro.bigbro.models.goods.GoodsTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.bigbro.repositories.CityRepository;
import pro.bigbro.repositories.GoodsTransactionRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class GoodsTransactionsRestTemplateImpl implements GoodsTransactionRestTemplate {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GoodsTransactionRepository goodsTransactionRepository;
    @Autowired
    private CityRepository cityRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<GoodsTransaction> getAllGoodsTransactions(LocalDate startDate, LocalDate endDate) {
        System.out.println("Starting downloading goods transactions\n");
        List<City> cityList = (List<City>) cityRepository.findAll();
        int i = 1;
        for (City city : cityList) {
            System.out.printf("[%d/%d] downloading goods transactions of %s\n", i, cityList.size(), city.getName());
            List<GoodsTransaction> result = getGoodsTransactionForCity(city.getId(), startDate, endDate);
            for (GoodsTransaction transaction : result) {
                goodsTransactionRepository.save(transaction);
            }
            i++;
        }
        System.out.println();
        return null;
    }

    private List<GoodsTransaction> getGoodsTransactionForCity(int cityId, LocalDate startDate, LocalDate endDate) {
        ResponseEntity<GoodsTransaction[]> responseEntity = restTemplate.getForEntity("http://api.yclients.com/api/v1/storages/transactions/{cityId}?start_date={startDate}&end_date={endDate}",
                GoodsTransaction[].class, cityId, formatter.format(startDate), formatter.format(endDate));
        List<GoodsTransaction> goodsTransactions = Arrays.asList(responseEntity.getBody());
        goodsTransactions.forEach(goodsTransaction -> goodsTransaction.setCityId(cityId));
        return goodsTransactions;
    }
}
