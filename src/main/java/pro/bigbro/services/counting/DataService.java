package pro.bigbro.services.counting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pro.bigbro.apachePoi.ExcelService;
import pro.bigbro.jdbc.cities.*;
import pro.bigbro.jdbc.total.*;
import pro.bigbro.models.cities.City;
import pro.bigbro.models.jdbc.StaffJdbc;
import pro.bigbro.models.masters.MasterMultiCity;
import pro.bigbro.models.reportUnits.avglength.LengthReport;
import pro.bigbro.models.reportUnits.cities.*;
import pro.bigbro.models.reportUnits.total.*;
import pro.bigbro.repositories.CityRepository;
import pro.bigbro.repositories.LengthReportRepository;
import pro.bigbro.repositories.MasterMultiCityRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataService {

    @Autowired @Lazy
    private CityRepository cityRepository;
    @Autowired @Lazy
    private LengthReportRepository lengthReportRepository;
    @Autowired @Lazy
    private CountingService countingService;
    @Autowired @Lazy
    private MasterMultiCityRepository masterMultiCityRepository;

    @Autowired @Lazy
    private WorkingMonthesJdbcTemplate workingMonthesJdbcTemplate;
    @Autowired @Lazy
    private StaffJdbcTemplate staffJdbcTemplate;

    @Autowired @Lazy
    private ExcelService excelService;
    @Autowired @Lazy
    private ClientStatJdbcTemplate clientStatJdbcTemplate;
    @Autowired @Lazy
    private AverageClientVisitJdbcTemplate averageClientVisitJdbcTemplate;
    @Autowired @Lazy
    private MasterConversionStatJdbcTemplate masterConversionStatJdbcTemplate;
    @Autowired @Lazy
    private FrequencyStatJdbcTemplate frequencyStatJdbcTemplate;
    @Autowired @Lazy
    private SpreadingStatJdbcTemplate spreadingStatJdbcTemplate;
    @Autowired @Lazy
    private GoodsStatJdbcTemplate goodsStatJdbcTemplate;

    @Autowired @Lazy
    private DinamicStatJdbcTemplate dinamicStatJdbcTemplate;

    @Autowired @Lazy
    private GeneralJdbcTemplate generalJdbcTemplate;
    @Autowired @Lazy
    private ClientTotalJdbcTemplate clientTotalJdbcTemplate;
    @Autowired @Lazy
    private DataTotalJdbcTemplate dataTotalJdbcTemplate;

    @Autowired @Lazy
    private SeleniumService seleniumService;

    @Autowired @Lazy
    private MasterStatJdbcTemplate masterStatJdbcTemplate;
    @Autowired @Lazy
    private MasterSimpleStatJdbcTemplate masterSimpleStatJdbcTemplate;
    @Autowired @Lazy
    private GoodDetailedStatJdbcTemplate goodDetailedStatJdbcTemplate;
    @Autowired @Lazy
    private GoodDetailedTotalStatJdbcTemplate goodDetailedTotalStatJdbcTemplate;
    @Autowired @Lazy
    private ServiceDetailedStatJdbcTemplate serviceDetailedStatJdbcTemplate;

    public void countAndWriteDataForCity() {
        List<City> cityList = (List<City>) cityRepository.findAll();
        for (City city : cityList) {
            System.out.println("[" + (cityList.indexOf(city) + 1) + "/" + cityList.size() + "] Рассчитываем данные для города " + city.getName());
            excelService.createWorkbook();

            List<WorkingMonth> workingMonthList = workingMonthesJdbcTemplate.findAllWorkingPeriodsByCity(city.getId());
            List<StaffJdbc> workingStaffList = staffJdbcTemplate.findWorkingStaff(city.getId());
            CityGeneral cityGeneral = new CityGeneral(city.getId(), city.getName(), workingMonthList, workingStaffList);
            excelService.writeGeneralInformation(cityGeneral);

            List<ClientStat> clientStatForCities;

            // все услуги по месяцам
            excelService.writeHeader("Динамика по городам все услуги");
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
            excelService.writeHeader("Динамика по городам стрижки");
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
            excelService.writeHeader("Динамика по городам анонимы");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());
            clientStatForCities = clientStatJdbcTemplate.findAnonimClients(city.getId());
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "Анонимы");
            excelService.addEmptyRow();

            // Клиенты без ссылок по месяцам
            excelService.writeHeader("Динамика по городам клиенты без ссылок");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());
            clientStatForCities = clientStatJdbcTemplate.findClientsWithoutLinks(city.getId());
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "Без ссылки");
            excelService.addEmptyRow();

            // Потенциал
            excelService.writeHeader("Потенциал");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());
            clientStatForCities = clientStatJdbcTemplate.findPotential(city.getId());
            excelService.writeCityClientsStat(clientStatForCities, cityGeneral.getWorkingMonthList(), "Потенциал");
            excelService.addEmptyRow();

            // Средний возраст клиентов по месяцам
            excelService.writeHeader("Динамика по городам средний возраст клиентов");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());
            List<AverageClientVisit> averageClientVisitList = averageClientVisitJdbcTemplate.countAverageClientVisit(city.getId());
            excelService.writeAverageClientVisit(averageClientVisitList, cityGeneral.getWorkingMonthList(), "Ср. возраст");
            excelService.addEmptyRow();


            List<MasterConversionStat> masterConversionStatList;
            // Конверсия НК
            excelService.writeHeader("Конверсия по мастерам НК");
            excelService.writeMonthesForConversion(cityGeneral.getWorkingMonthList());
            masterConversionStatList = masterConversionStatJdbcTemplate.getAllStatNk(city.getId());
            excelService.writeConversion(masterConversionStatList, cityGeneral);
            excelService.addEmptyRow();

            // Конверсия ПК
            excelService.writeHeader("Конверсия по мастерам ПК");
            excelService.writeMonthesForConversion(cityGeneral.getWorkingMonthList());
            masterConversionStatList = masterConversionStatJdbcTemplate.getAllStatPk(city.getId());
            excelService.writeConversion(masterConversionStatList, cityGeneral);
            excelService.addEmptyRow();

            // Конверсия К
            excelService.writeHeader("Конверсия по мастерам К");
            excelService.writeMonthesForConversion(cityGeneral.getWorkingMonthList());
            masterConversionStatList = masterConversionStatJdbcTemplate.getAllStatK(city.getId());
            excelService.writeConversion(masterConversionStatList, cityGeneral);
            excelService.addEmptyRow();

            // Конверсия К все
            excelService.writeHeader("Конверсия по мастерам К все");
            excelService.writeMonthesForConversion(cityGeneral.getWorkingMonthList());
            masterConversionStatList = masterConversionStatJdbcTemplate.getAllStatKAll(city.getId());
            excelService.writeConversion(masterConversionStatList, cityGeneral);
            excelService.addEmptyRow();


            // Частотность
            excelService.writeHeader("Частотность");
            excelService.writeMonthesForConversion(cityGeneral.getWorkingMonthList());
            List<FrequencyStat> frequencyStatList = frequencyStatJdbcTemplate.getAllStats(city.getId());
            excelService.writeFrequency(frequencyStatList, cityGeneral);
            excelService.addEmptyRow();


            // Распределение по дням
            excelService.writeHeader("Распределение");
            excelService.writeDaysForSpreading();
            List<SpreadingStat> spreadingStatList = spreadingStatJdbcTemplate.getSpreadingStat(city.getId());
            excelService.writeSpreadingStat(spreadingStatList, cityGeneral);
            excelService.addEmptyRow();
            excelService.writeHeader("Распределение кол-во дней");
            excelService.writeDaysForSpreading();
            List<SpreadingDaysCount> spreadingDaysCounts = spreadingStatJdbcTemplate.getSpreadingDaysCount(city.getId());
            excelService.writeSpreadingDaysCount(spreadingDaysCounts);
            excelService.addEmptyRow();


            // Товары по месяцам
            excelService.writeHeader("Товары по месяцам");
            excelService.writeMonthes(cityGeneral.getWorkingMonthList());
            List<GoodsStat> goodsStatList = goodsStatJdbcTemplate.getAllGoodsStats(city.getId());
            excelService.writeGoodsStat(goodsStatList, cityGeneral);
            excelService.addEmptyRow();


            // Товары по мастерам
            excelService.writeHeader("Товары по мастерам");
            excelService.writeMonthesForGoodsByMasters(cityGeneral.getWorkingMonthList());
            List<GoodsMasterStat> goodsMasterStatList = goodsStatJdbcTemplate.getAllGoodsByMastersStat(city.getId());
            excelService.writeGoodsByMastersStat(goodsMasterStatList, cityGeneral);
            excelService.addEmptyRow();


            excelService.saveWorkbook(city.getName());
        }
    }

    public void countAndWriteSummaryData() {
        System.out.println("Рассчитываем общие данные");
        excelService.createWorkbook();

        List<ClientTotal> clientTotalList;
        int year = generalJdbcTemplate.getLastYear();
        int month = generalJdbcTemplate.getLastMonth(year);

        //справочные


        excelService.writeMonthes(Arrays.asList(new WorkingMonth(month, year)));
        excelService.addEmptyRow();

        // данные по клиентам
        excelService.writeHeader("К");
        clientTotalList = clientTotalJdbcTemplate.getK(year, month);
        excelService.writeTotalClients(clientTotalList);
        excelService.addEmptyRow();

        excelService.writeHeader("НК");
        clientTotalList = clientTotalJdbcTemplate.getNk(year, month);
        excelService.writeTotalClients(clientTotalList);
        excelService.addEmptyRow();

        excelService.writeHeader("ПК");
        clientTotalList = clientTotalJdbcTemplate.getPk(year, month);
        excelService.writeTotalClients(clientTotalList);
        excelService.addEmptyRow();

        excelService.writeHeader("Анонимы");
        clientTotalList = clientTotalJdbcTemplate.getAnonims(year, month);
        excelService.writeTotalClients(clientTotalList);
        excelService.addEmptyRow();

        excelService.writeHeader("Без ссылки");
        clientTotalList = clientTotalJdbcTemplate.getWithoutLinks(year, month);
        excelService.writeTotalClients(clientTotalList);
        excelService.addEmptyRow();


        // Финансы

        List<DataTotal> dataTotalList;

        excelService.writeHeader("Услуги");
        dataTotalList = dataTotalJdbcTemplate.getFinanceServices(year, month);
        excelService.writeTotalData(dataTotalList);
        excelService.addEmptyRow();

        excelService.writeHeader("Товары");
        dataTotalList = dataTotalJdbcTemplate.getFinanceGoods(year, month);
        excelService.writeTotalData(dataTotalList);
        excelService.addEmptyRow();

        excelService.writeHeader("Выручка");
        dataTotalList = dataTotalJdbcTemplate.getFinanceAll(year, month);
        excelService.writeTotalData(dataTotalList);
        excelService.addEmptyRow();

        excelService.writeHeader("Возраст");
        List<DataTotal> age = dataTotalJdbcTemplate.getAge(year, month);
        excelService.writeTotalData(age);
        excelService.addEmptyRow();

        double result = countingService.countAvgRevenueByAge(dataTotalList, age, 0, 6);
        excelService.writeSingleRow("ср. выручка до 6 мес", result);

        result = countingService.countAvgRevenueByAge(dataTotalList, age, 6, 12);
        excelService.writeSingleRow("ср. выручка 6 - 12 мес", result);

        result = countingService.countAvgRevenueByAge(dataTotalList, age, 12, 100000);
        excelService.writeSingleRow("ср. выручка от 12 мес", result);

        result = countingService.countCitiesByRevenue(dataTotalList, 300_000);
        excelService.writeSingleRow("кол-во п. с выр. От 300т", result);

        result = countingService.countCitiesByRevenue(dataTotalList, 500_000);
        excelService.writeSingleRow("кол-во п. с выр. От 500т", result);

        result = countingService.countCitiesByRevenue(dataTotalList, 800_000);
        excelService.writeSingleRow("кол-во п. с выр. От 800т", result);

        result = countingService.countCitiesByRevenue(dataTotalList, 1_000_000);
        excelService.writeSingleRow("кол-во п. с выр. От 1000т", result);

        excelService.writeSingleRow("кол-во парикмахерских", dataTotalList.size());
        excelService.writeSingleRow("кол-во дней в месяце", LocalDate.of(year, month, 1).plusMonths(1).minusDays(1).getDayOfMonth());
        excelService.addEmptyRow();

        // дней отработано
        excelService.writeHeader("Рабочих дней");
        dataTotalList = dataTotalJdbcTemplate.getWorkingDays(year, month);
        excelService.writeTotalData(dataTotalList);
        excelService.addEmptyRow();

        // кол-во мастеров
        excelService.writeHeader("Кол-во мастеров по городам");
        dataTotalList = dataTotalJdbcTemplate.getMastersCount(year, month);
        excelService.writeTotalData(dataTotalList);
        excelService.addEmptyRow();

        excelService.writeSingleRow("Кол-во мастеров", dataTotalList.stream().mapToDouble(DataTotal::getValue).sum());
        excelService.addEmptyRow();


        // Потенциал
        excelService.writeHeader("Потенциал");
        dataTotalList = dataTotalJdbcTemplate.getPotential(year, month);
        excelService.writeTotalData(dataTotalList);
        excelService.addEmptyRow();


        // Стоимость стрижки
        excelService.writeHeader("Стоимость стрижки");
        dataTotalList = dataTotalJdbcTemplate.getServicePrice();
        excelService.writeTotalData(dataTotalList);
        excelService.addEmptyRow();


        double averageCutPrice = dataTotalJdbcTemplate.getAverageCutPrice(year, month);
        excelService.writeSingleRow("ср. стоимость стрижек", averageCutPrice);

        double servicePartInRevenue = dataTotalJdbcTemplate.getServicePartInRevenue(year, month);
        excelService.writeSingleRow("доля стрижек в выручке", servicePartInRevenue);
        excelService.addEmptyRow();


        // ср. мастеров в день
        excelService.writeHeader("ср. мастеров в день");
        dataTotalList = dataTotalJdbcTemplate.getAverageWorkingMaster(year, month);
        excelService.writeTotalData(dataTotalList);
        excelService.addEmptyRow();


        // ср. К на мастера в день
        excelService.writeHeader("ср. К на мастера в день");
        dataTotalList = dataTotalJdbcTemplate.getAverageClientPerMasterPerDay(year, month);
        excelService.writeTotalData(dataTotalList);
        excelService.addEmptyRow();


        // конверсия
        int kYear = countingService.yearForConversion(year, month, 2);
        int kMonth = countingService.monthForConversion(year, month, 2);
        getAllTypesOfConversion(2, kYear, kMonth, 60);

        kYear = countingService.yearForConversion(year, month, 3);
        kMonth = countingService.monthForConversion(year, month, 3);
        getAllTypesOfConversion(3, kYear, kMonth, 90);

        kYear = countingService.yearForConversion(year, month, 4);
        kMonth = countingService.monthForConversion(year, month, 4);
        excelService.writeHeader("конв. 4 мес все время");
        dataTotalList = dataTotalJdbcTemplate.getConversionAllTime(kYear, kMonth, 120);
        excelService.writeTotalData(dataTotalList);
        excelService.addEmptyRow();

        excelService.writeHeader("конв. общая все время");
        dataTotalList = dataTotalJdbcTemplate.getConversionAllTime(year, month, 100000);
        excelService.writeTotalData(dataTotalList);
        excelService.addEmptyRow();


        // данные по продолжительности
        List<LengthReport> lengthReportList = lengthReportRepository.findAllByMonthAndYear(month, year);
        dataTotalList = lengthReportList.stream()
                .map(lengthReport -> new DataTotal(lengthReport.getCityId(), lengthReport.getCityName(), lengthReport.getAvgServ()))
                .collect(Collectors.toList());
        excelService.writeHeader("ср. продолжительность услуги");
        excelService.writeTotalData(dataTotalList);
        excelService.addEmptyRow();

        dataTotalList = lengthReportList.stream()
                .map(lengthReport -> new DataTotal(lengthReport.getCityId(), lengthReport.getCityName(), lengthReport.getAvgSmena()))
                .collect(Collectors.toList());
        excelService.writeHeader("ср. продолжительность смены");
        excelService.writeTotalData(dataTotalList);
        excelService.addEmptyRow();


        //динамика по клиентам
        excelService.writeHeader("Динамика по клиентам");
        List<DinamicStat> dinamicStatList = dinamicStatJdbcTemplate.getClientsDinamic();
        excelService.writeDinamicStat(dinamicStatList);
        excelService.addEmptyRow();

        //динамика по выручке
        excelService.writeHeader("Динамика по выручке");
        dinamicStatList = dinamicStatJdbcTemplate.getFinancialDinamic();
        excelService.writeDinamicStat(dinamicStatList);
        excelService.addEmptyRow();

        /*  МАСТЕРА  */
        //стаж работы мастеров
        excelService.writeHeader("Стаж работы мастеров");
        List<MasterStat> masterStatList = masterStatJdbcTemplate.getMastersMonthesWorked();
        excelService.writeMasterStat(masterStatList);
        excelService.addEmptyRow();

        //общее кол-во клиентов мастеров
        excelService.writeHeader("Общее кол-во клиентов мастера");
        masterStatList = masterStatJdbcTemplate.getMastersClientsTotal();
        excelService.writeMasterStat(masterStatList);
        excelService.addEmptyRow();

        //кол-во клиентов мастеров за месяц
        excelService.writeHeader("Кол-во клиентов мастеров за месяц");
        masterStatList = masterStatJdbcTemplate.getMastersClientsMonth(year, month);
        excelService.writeMasterStat(masterStatList);
        excelService.addEmptyRow();

        //конв. мастеров 3 - все
        excelService.writeHeader("Конв. мастеров 3 - все");
        masterStatList = masterStatJdbcTemplate.getMastersConversion3MonthAllTime(year, month);
        excelService.writeMasterStat(masterStatList);
        excelService.addEmptyRow();

        //конв. мастеров 3 - 3
        excelService.writeHeader("Конв. мастеров 3 - 3");
        masterStatList = masterStatJdbcTemplate.getMastersConversion3Month3Month(year, month);
        excelService.writeMasterStat(masterStatList);
        excelService.addEmptyRow();

        //доходы мастеров
        excelService.writeHeader("Доходы мастеров");
        masterStatList = masterStatJdbcTemplate.getMastersDailyIncome(year, month);
        excelService.writeMasterStat(masterStatList);
        excelService.addEmptyRow();

        //товары мастеров
        excelService.writeHeader("Товары мастеров");
        masterStatList = masterStatJdbcTemplate.getMastersGoods(year, month);
        excelService.writeMasterStat(masterStatList);
        excelService.addEmptyRow();

        //ср. товары мастеров
        excelService.writeHeader("Ср. товары мастеров");
        masterStatList = masterStatJdbcTemplate.getMastersAverageGoods(year, month);
        excelService.writeMasterStat(masterStatList);
        excelService.addEmptyRow();

        /*  ОБЪЕДИНЕННЫЕ МАСТЕРА  */
        List<MasterMultiCity> masterMultiCityList = (List<MasterMultiCity>) masterMultiCityRepository.findAll();
        Map<String, List<Integer>> multiMap = masterMultiCityList.stream()
                .collect(Collectors.groupingBy(MasterMultiCity::getCityGroupName,
                        Collectors.mapping(MasterMultiCity::getCityId, Collectors.toList())));

        System.out.println(multiMap);

        //стаж работы мастеров сокр
        excelService.writeHeader("Стаж работы мастеров сокр");
        List<MasterSimpleStat> masterSimpleStatList = masterSimpleStatJdbcTemplate.getMastersMonthesWorked(multiMap);
        excelService.writeMasterSimpleStat(masterSimpleStatList);
        excelService.addEmptyRow();

        //общее кол-во клиентов мастеров сокр
        excelService.writeHeader("Общее кол-во клиентов мастера сокр");
        masterSimpleStatList = masterSimpleStatJdbcTemplate.getMastersClientsTotal(multiMap);
        excelService.writeMasterSimpleStat(masterSimpleStatList);
        excelService.addEmptyRow();

        //кол-во клиентов мастеров за месяц сокр
        excelService.writeHeader("Кол-во клиентов мастеров за месяц сокр");
        masterSimpleStatList = masterSimpleStatJdbcTemplate.getMastersClientsMonth(year, month, multiMap);
        excelService.writeMasterSimpleStat(masterSimpleStatList);
        excelService.addEmptyRow();

        //конв. мастеров 3 - все сокр
        excelService.writeHeader("Конв. мастеров 3 - все сокр");
        masterSimpleStatList = masterSimpleStatJdbcTemplate.getMastersConversion3MonthAllTime(year, month, multiMap);
        excelService.writeMasterSimpleStat(masterSimpleStatList);
        excelService.addEmptyRow();

        //конв. мастеров 3 - 3 сокр
        excelService.writeHeader("Конв. мастеров 3 - 3 сокр");
        masterSimpleStatList = masterSimpleStatJdbcTemplate.getMastersConversion3Month3Month(year, month, multiMap);
        excelService.writeMasterSimpleStat(masterSimpleStatList);
        excelService.addEmptyRow();

        //доходы мастеров сокр
        excelService.writeHeader("Доходы мастеров сокр");
        masterSimpleStatList = masterSimpleStatJdbcTemplate.getMastersDailyIncome(year, month, multiMap);
        excelService.writeMasterSimpleStat(masterSimpleStatList);
        excelService.addEmptyRow();

        //товары мастеров сокр
        excelService.writeHeader("Товары мастеров сокр");
        masterSimpleStatList = masterSimpleStatJdbcTemplate.getMastersGoods(year, month, multiMap);
        excelService.writeMasterSimpleStat(masterSimpleStatList);
        excelService.addEmptyRow();

        //ср. товары мастеров сокр
        excelService.writeHeader("Ср. товары мастеров сокр");
        masterSimpleStatList = masterSimpleStatJdbcTemplate.getMastersAverageGoods(year, month, multiMap);
        excelService.writeMasterSimpleStat(masterSimpleStatList);
        excelService.addEmptyRow();

        //ТОВАРЫ
        excelService.writeHeader("Детализация товаров");
        excelService.writeHeaders(new String[] {"id города", "Город", "Наименование товара", "Цена", "Продажи",
            "Кол-во", "Факт. стоимость", "Доля"});
        List<DetailedStat> detailedStatList = goodDetailedStatJdbcTemplate.getDetailesStat(year, month);
        excelService.writeGoodsDetailedStat(detailedStatList);
        excelService.addEmptyRow();

        excelService.writeHeader("Детализация товаров общая");
        excelService.writeHeaders(new String[]{"Наименование товара", "Продажи", "Кол-во", "Доля"});
        List<DetailedTotalStat> detailedTotalStatList = goodDetailedTotalStatJdbcTemplate.getDetailesTotalStat(year, month);
        excelService.writeGoodsDetailedTotalStat(detailedTotalStatList);
        excelService.addEmptyRow();

        //Услуги
        excelService.writeHeader("Детализация услуг");
        excelService.writeHeaders(new String[] {"id города", "Город", "Наименование услуги", "Цена", "Выручка",
                "Кол-во", "Факт. стоимость", "Доля"});
        detailedStatList = serviceDetailedStatJdbcTemplate.getDetailesStat(year, month);
        excelService.writeGoodsDetailedStat(detailedStatList);
        excelService.addEmptyRow();

        excelService.writeHeader("Детализация услуг общая");
        excelService.writeHeaders(new String[]{"Наименование услуги", "Выручка", "Кол-во", "Доля"});
        detailedTotalStatList = serviceDetailedStatJdbcTemplate.getDetailesTotalStat(year, month);
        excelService.writeGoodsDetailedTotalStat(detailedTotalStatList);
        excelService.addEmptyRow();

        excelService.saveWorkbook("Total");
    }

    private void getAllTypesOfConversion (int monthes, int year, int month, int returnTime) {
        List<DataTotal> dataTotalList;

        excelService.writeHeader("конв. " + monthes + " мес за месяц");
        dataTotalList = dataTotalJdbcTemplate.getConversionForMonth(year, month, returnTime);
        excelService.writeTotalData(dataTotalList);

        double totalSummaryConversion = dataTotalJdbcTemplate.getConversionForMonthSum(year, month, returnTime);
        excelService.writeSingleRow("конв. " + monthes + " мес за месяц итого", totalSummaryConversion);
        excelService.addEmptyRow();

        excelService.writeHeader("конв. " + monthes + " мес за месяц анонимы");
        dataTotalList = dataTotalJdbcTemplate.getConversionForMonthAnonim(year, month, returnTime);
        excelService.writeTotalData(dataTotalList);

        totalSummaryConversion = dataTotalJdbcTemplate.getConversionforMonthAnonimSum(year, month, returnTime);
        excelService.writeSingleRow("конв. " + monthes + " мес за месяц анонимы итого", totalSummaryConversion);
        excelService.addEmptyRow();


        excelService.writeHeader("конв. " + monthes + " мес все время");
        dataTotalList = dataTotalJdbcTemplate.getConversionAllTime(year, month, returnTime);
        excelService.writeTotalData(dataTotalList);

        totalSummaryConversion = dataTotalJdbcTemplate.getConversionAllTimeTotal(year, month, returnTime);
        excelService.writeSingleRow("конв. " + monthes + " мес все время итого", totalSummaryConversion);
        excelService.addEmptyRow();

        excelService.writeHeader("конв. " + monthes + " мес все время анонимы");
        dataTotalList = dataTotalJdbcTemplate.getConversionAllTimeAnonim(year, month, returnTime);
        excelService.writeTotalData(dataTotalList);

        totalSummaryConversion = dataTotalJdbcTemplate.getConversionAllTimeAnonimTotal(year, month, returnTime);
        excelService.writeSingleRow("конв. " + monthes + " мес все время анонимы итого", totalSummaryConversion);
        excelService.addEmptyRow();


        excelService.writeHeader("конв. " + monthes + " мес за 3 мес");
        dataTotalList = dataTotalJdbcTemplate.getConversion3Month(year, month, returnTime);
        excelService.writeTotalData(dataTotalList);

        totalSummaryConversion = dataTotalJdbcTemplate.getConversion3MonthTotal(year, month, returnTime);
        excelService.writeSingleRow("конв. " + monthes + " мес за 3 мес итого", totalSummaryConversion);
        excelService.addEmptyRow();

        excelService.writeHeader("конв. " + monthes + " мес за 3 мес анонимы");
        dataTotalList = dataTotalJdbcTemplate.getConversion3MonthAnonim(year, month, returnTime);
        excelService.writeTotalData(dataTotalList);

        totalSummaryConversion = dataTotalJdbcTemplate.getConversion3MonthAnonimTotal(year, month, returnTime);
        excelService.writeSingleRow("конв. " + monthes + " мес за 3 мес анонимы итого", totalSummaryConversion);
        excelService.addEmptyRow();
    }
}
