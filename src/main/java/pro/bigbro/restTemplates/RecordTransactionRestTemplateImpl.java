package pro.bigbro.restTemplates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.bigbro.models.cities.City;
import pro.bigbro.models.records.RecordTransaction;
import pro.bigbro.models.records.RecordTransactionContainer;
import pro.bigbro.repositories.CityRepository;
import pro.bigbro.repositories.RecordTransactionRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RecordTransactionRestTemplateImpl implements RecordTransactionRestTemplate {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RecordTransactionRepository recordTransactionRepository;
    @Autowired
    private CityRepository cityRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<RecordTransaction> getAllRecordTransactions(LocalDate startDate, LocalDate endDate) {
        System.out.println("Starting downloading record transactions\n");
        List<City> cityList = (List<City>) cityRepository.findAll();
        int i = 1;
        for (City city : cityList) {
            System.out.printf("[%d/%d] downloading record transactions of %s\n", i, cityList.size(), city.getName());
            List<RecordTransaction> result = getRecordTransactionsForCity(city.getId(), startDate, endDate);
            for (RecordTransaction recordTransaction : result) {
                recordTransactionRepository.save(recordTransaction);
            }
            i++;
        }
        System.out.println();
        return null;
    }


    private List<RecordTransaction> getRecordTransactionsForCity(int cityId, LocalDate startDate, LocalDate endDate) {
        ResponseEntity<RecordTransactionContainer> responseEntity = restTemplate.getForEntity("http://api.yclients.com/api/v1/records/{cityId}?page=1&count=10&start_date={startDate}&end_date={endDate}&c_start_date=2015-03-01&c_end_date={endDate}",
                RecordTransactionContainer.class, cityId, formatter.format(startDate), formatter.format(endDate), formatter.format(endDate));
        RecordTransactionContainer recordTransactionContainer = responseEntity.getBody();
        int recordTransactionCount = recordTransactionContainer.getCount();
        int pages = recordTransactionCount / 300
                + (recordTransactionCount % 300 == 0 ? 0 : 1);
        List<RecordTransaction> result = new ArrayList<>();
        for (int i = 1; i <= pages; i++) {
            ResponseEntity<RecordTransactionContainer> responseEntityPage = restTemplate.getForEntity("http://api.yclients.com/api/v1/records/{cityId}?page={page}&count=300&start_date={startDate}&end_date={endDate}&c_start_date=2015-03-01&c_end_date={endDate}",
                    RecordTransactionContainer.class, cityId, i, formatter.format(startDate), formatter.format(endDate), formatter.format(endDate));
            RecordTransactionContainer recordTransactionContainer1 = responseEntityPage.getBody();
            result.addAll(Arrays.asList(recordTransactionContainer1.getData()));
        }
        result.forEach(client -> client.setCityId(cityId));
        return result;
    }

}
