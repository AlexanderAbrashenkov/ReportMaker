package pro.bigbro.restTemplates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.bigbro.models.cities.City;
import pro.bigbro.models.financial.FinancialTransaction;
import pro.bigbro.repositories.CityRepository;
import pro.bigbro.repositories.FinancialTransactionRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FinancialTransactionRestTemplateImpl implements FinancialTransactionRestTemplate {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;
    @Autowired
    private CityRepository cityRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<FinancialTransaction> getAllFinancialTransactions(LocalDate startDate, LocalDate endDate) {
        System.out.println("Starting downloading financial transactions\n");
        List<City> cityList = (List<City>) cityRepository.findAll();
        int i = 1;
        for (City city : cityList) {
            System.out.printf("[%d/%d] download financial transactions of %s\n", i, cityList.size(), city.getName());
            List<FinancialTransaction> result = getFinancialTransactionForCity(city.getId(), startDate, endDate);
            for (FinancialTransaction transaction : result) {
                financialTransactionRepository.save(transaction);
            }
            i++;
        }
        System.out.println();
        return null;
    }

    private List<FinancialTransaction> getFinancialTransactionForCity(int cityId, LocalDate startDate, LocalDate endDate) {
        boolean everythingDownloaded = false;
        int page = 1;
        List<FinancialTransaction> result = new ArrayList<>();
        while (!everythingDownloaded) {
            ResponseEntity<FinancialTransaction[]> responseEntity = restTemplate.getForEntity("http://api.yclients.com/api/v1/transactions/{cityId}?page={page}&count=300&start_date={startDate}&end_date={endDate}",
                    FinancialTransaction[].class, cityId, page, formatter.format(startDate), formatter.format(endDate));
            List<FinancialTransaction> financialTransactions = Arrays.asList(responseEntity.getBody());
            if (financialTransactions == null || financialTransactions.size() == 0) {
                everythingDownloaded = true;
                continue;
            }
            financialTransactions.forEach(financialTransaction -> financialTransaction.setCityId(cityId));
            result.addAll(financialTransactions);
            page++;
        }
        return result;
    }
}
