package pro.bigbro.services.counting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.bigbro.apachePoi.ExcelService;
import pro.bigbro.jdbc.*;
import pro.bigbro.models.cities.City;
import pro.bigbro.models.reportUnits.*;
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
    private ClientStatJdbcTemplate clientStatJdbcTemplate;
    @Autowired
    private AverageClientVisitJdbcTemplate averageClientVisitJdbcTemplate;
    @Autowired
    private MasterConversionStatJdbcTemplate masterConversionStatJdbcTemplate;
    @Autowired
    private FrequencyStatJdbcTemplate frequencyStatJdbcTemplate;
    @Autowired
    private SpreadingStatJdbcTemplate spreadingStatJdbcTemplate;
    @Autowired
    private GoodsStatJdbcTemplate goodsStatJdbcTemplate;

    public void countAndWriteData() {
        List<City> cityList = (List<City>) cityRepository.findAll();
        for (City city : cityList) {
            System.out.println("[" + (cityList.indexOf(city) + 1) + "/" + cityList.size() + "] Рассчитываем данные для города " + city.getName());
            excelService.createWorkbook();

            CityGeneral cityGeneral = generalInformationService.getGeneralInformationForCity(city);
            excelService.writeGeneralInformation(cityGeneral);

            List<ClientStat> clientStatForCities;

            // все услуги по месяцам
            excelService.writeCityStatHeader("Динамика по городам все услуги");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());

            clientStatForCities = clientStatJdbcTemplate.findAllClients(city.getId());
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "К");

            clientStatForCities = clientStatJdbcTemplate.findClientsByVisitNum(city.getId(), 1, 2);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "НК");

            clientStatForCities = clientStatJdbcTemplate.findClientsByVisitNum(city.getId(), 2, 3);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК2");

            clientStatForCities = clientStatJdbcTemplate.findClientsByVisitNum(city.getId(), 3, 4);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК3");

            clientStatForCities = clientStatJdbcTemplate.findClientsByVisitNum(city.getId(), 4, 5);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК4");

            clientStatForCities = clientStatJdbcTemplate.findClientsByVisitNum(city.getId(), 5, 100000);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК5+");

            clientStatForCities = clientStatJdbcTemplate.findClientsByVisitNum(city.getId(), 7, 100000);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК7+");

            clientStatForCities = clientStatJdbcTemplate.findClientsByVisitNum(city.getId(), 10, 100000);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК10+");
            excelService.addEmptyRow();

            //стрижки по месяцам
            excelService.writeCityStatHeader("Динамика по городам стрижки");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());

            clientStatForCities = clientStatJdbcTemplate.findAllClientsCut(city.getId());
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "К");

            clientStatForCities = clientStatJdbcTemplate.findClientsByVisitNumCut(city.getId(), 1, 2);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "НК");

            clientStatForCities = clientStatJdbcTemplate.findClientsByVisitNumCut(city.getId(), 2, 3);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК2");

            clientStatForCities = clientStatJdbcTemplate.findClientsByVisitNumCut(city.getId(), 3, 4);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК3");

            clientStatForCities = clientStatJdbcTemplate.findClientsByVisitNumCut(city.getId(), 4, 5);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК4");

            clientStatForCities = clientStatJdbcTemplate.findClientsByVisitNumCut(city.getId(), 5, 100000);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК5+");

            clientStatForCities = clientStatJdbcTemplate.findClientsByVisitNumCut(city.getId(), 7, 100000);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК7+");

            clientStatForCities = clientStatJdbcTemplate.findClientsByVisitNumCut(city.getId(), 10, 100000);
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "ПК10+");
            excelService.addEmptyRow();

            // Анонимы по месяцам
            excelService.writeCityStatHeader("Динамика по городам анонимы");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());
            clientStatForCities = clientStatJdbcTemplate.findAnonimClients(city.getId());
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "Анонимы");
            excelService.addEmptyRow();

            // Клиенты без ссылок по месяцам
            excelService.writeCityStatHeader("Динамика по городам клиенты без ссылок");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());
            clientStatForCities = clientStatJdbcTemplate.findClientsWithoutLinks(city.getId());
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "Без ссылки");
            excelService.addEmptyRow();

            // Потенциал
            excelService.writeCityStatHeader("Потенциал");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());
            clientStatForCities = clientStatJdbcTemplate.findPotential(city.getId());
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "Потенциал");
            excelService.addEmptyRow();

            // Средний возраст клиентов по месяцам
            excelService.writeCityStatHeader("Динамика по городам средний возраст клиентов");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());
            List<AverageClientVisit> averageClientVisitList = averageClientVisitJdbcTemplate.countAverageClientVisit(city.getId());
            excelService.writeAverageClientVisit(averageClientVisitList, cityGeneral.getWorkingMonthList(), "Ср. возраст");
            excelService.addEmptyRow();


            List<MasterConversionStat> masterConversionStatList;
            // Конверсия НК
            excelService.writeCityStatHeader("Конверсия по мастерам НК");
            excelService.writeMonthesForConversion(cityGeneral.getWorkingMonthList());
            masterConversionStatList = masterConversionStatJdbcTemplate.getAllStatNk(city.getId());
            excelService.writeConversion(masterConversionStatList, cityGeneral);
            excelService.addEmptyRow();

            // Конверсия ПК
            excelService.writeCityStatHeader("Конверсия по мастерам ПК");
            excelService.writeMonthesForConversion(cityGeneral.getWorkingMonthList());
            masterConversionStatList = masterConversionStatJdbcTemplate.getAllStatPk(city.getId());
            excelService.writeConversion(masterConversionStatList, cityGeneral);
            excelService.addEmptyRow();

            // Конверсия К
            excelService.writeCityStatHeader("Конверсия по мастерам К");
            excelService.writeMonthesForConversion(cityGeneral.getWorkingMonthList());
            masterConversionStatList = masterConversionStatJdbcTemplate.getAllStatK(city.getId());
            excelService.writeConversion(masterConversionStatList, cityGeneral);
            excelService.addEmptyRow();

            // Конверсия К все
            excelService.writeCityStatHeader("Конверсия по мастерам К все");
            excelService.writeMonthesForConversion(cityGeneral.getWorkingMonthList());
            masterConversionStatList = masterConversionStatJdbcTemplate.getAllStatKAll(city.getId());
            excelService.writeConversion(masterConversionStatList, cityGeneral);
            excelService.addEmptyRow();


            // Частотность
            excelService.writeCityStatHeader("Частотность");
            excelService.writeMonthesForConversion(cityGeneral.getWorkingMonthList());
            List<FrequencyStat> frequencyStatList = frequencyStatJdbcTemplate.getAllStats(city.getId());
            excelService.writeFrequency(frequencyStatList, cityGeneral);
            excelService.addEmptyRow();


            // Распределение по дням
            excelService.writeCityStatHeader("Распределение");
            excelService.writeDaysForSpreading();
            List<SpreadingStat> spreadingStatList = spreadingStatJdbcTemplate.getSpreadingStat(city.getId());
            excelService.writeSpreadingStat(spreadingStatList, cityGeneral);
            excelService.addEmptyRow();
            excelService.writeCityStatHeader("Распределение");
            excelService.writeDaysForSpreading();
            List<SpreadingDaysCount> spreadingDaysCounts = spreadingStatJdbcTemplate.getSpreadingDaysCount(city.getId());
            excelService.writeSpreadingDaysCount(spreadingDaysCounts);
            excelService.addEmptyRow();


            // Товары по месяцам
            excelService.writeCityStatHeader("Товары по месяцам");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());
            List<GoodsStat> goodsStatList = goodsStatJdbcTemplate.getAllGoodsStats(city.getId());
            excelService.writeGoodsStat(goodsStatList, cityGeneral);
            excelService.addEmptyRow();


            // Товары по местерам
            excelService.writeCityStatHeader("Товары по мастерам");
            excelService.writeMonthesForGoodsByMasters(cityGeneral.getWorkingMonthList());
            List<GoodsMasterStat> goodsMasterStatList = goodsStatJdbcTemplate.getAllGoodsByMastersStat(city.getId());
            excelService.writeGoodsByMastersStat(goodsMasterStatList, cityGeneral);
            excelService.addEmptyRow();


            excelService.saveWorkbook(city.getName());
        }
    }
}
