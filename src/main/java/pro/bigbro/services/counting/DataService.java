package pro.bigbro.services.counting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.bigbro.apachePoi.ExcelService;
import pro.bigbro.jdbc.ClientStatForCityJdbcTemplate;
import pro.bigbro.models.cities.City;
import pro.bigbro.models.reportUnits.CityGeneral;
import pro.bigbro.models.reportUnits.ClientStatForCity;
import pro.bigbro.repositories.CityRepository;

import java.util.List;

@Service
public class DataService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ExcelService excelService;
    @Autowired
    private GeneralInformationService generalInformationService;
    @Autowired
    private ClientStatForCityJdbcTemplate clientStatForCityJdbcTemplate;

    public void countAndWriteData() {
        List<City> cityList = (List<City>) cityRepository.findAll();
        for (City city : cityList) {
            System.out.println("Рассчитываем данные для города " + city.getName());
            excelService.createWorkbook();

            CityGeneral cityGeneral = generalInformationService.getGeneralInformationForCity(city);
            excelService.writeGeneralInformation(cityGeneral);

            List<ClientStatForCity> clientStatForCities;

            // все услуги по месяцам
            excelService.writeCityStatHeader("Динамика по городам все услуги");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());

            clientStatForCities = clientStatForCityJdbcTemplate.findAllClientsForCity(city.getId());
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "К");

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsForCityByVisitNum(city.getId(), 1, 2);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "НК");

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsForCityByVisitNum(city.getId(), 2, 3);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК2");

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsForCityByVisitNum(city.getId(), 3, 4);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК3");

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsForCityByVisitNum(city.getId(), 4, 5);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК4");

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsForCityByVisitNum(city.getId(), 5, 100000);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК5+");

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsForCityByVisitNum(city.getId(), 7, 100000);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК7+");

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsForCityByVisitNum(city.getId(), 10, 100000);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК10+");
            excelService.addEmptyRow();

            //стрижки по месяцам
            excelService.writeCityStatHeader("Динамика по городам стрижки");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());

            clientStatForCities = clientStatForCityJdbcTemplate.findAllClientsForCityCut(city.getId());
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "К");

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsForCityByVisitNumCut(city.getId(), 1, 2);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "НК");

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsForCityByVisitNumCut(city.getId(), 2, 3);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК2");

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsForCityByVisitNumCut(city.getId(), 3, 4);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК3");

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsForCityByVisitNumCut(city.getId(), 4, 5);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК4");

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsForCityByVisitNumCut(city.getId(), 5, 100000);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК5+");

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsForCityByVisitNumCut(city.getId(), 7, 100000);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК7+");

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsForCityByVisitNumCut(city.getId(), 10, 100000);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК10+");
            excelService.addEmptyRow();

            // Анонимы по месяцам
            excelService.writeCityStatHeader("Динамика по городам анонимы");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());

            clientStatForCities = clientStatForCityJdbcTemplate.findAnonimClientsForCity(city.getId());
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "Анонимы");

            // Клиенты без ссылок по месяцам
            excelService.writeCityStatHeader("Динамика по городам клиенты без ссылок");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsWithoutLinksForCity(city.getId());
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "Без ссылки");

            // Средний возраст клиентов по месяцам
            excelService.writeCityStatHeader("Динамика по городам клиенты без ссылок");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());

            clientStatForCities = clientStatForCityJdbcTemplate.findClientsWithoutLinksForCity(city.getId());
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "Без ссылки");



            excelService.saveWorkbook(city.getName());
        }
    }
}
