package pro.bigbro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.bigbro.restTemplates.*;
import pro.bigbro.services.corrections.CorrectionService;
import pro.bigbro.services.counting.DataService;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class Main {
    @Autowired
    private StaffRestTemplate staffRestTemplate;
    @Autowired
    private ClientRestTemplate clientRestTemplate;
    @Autowired
    private GoodsTransactionRestTemplate goodsTransactionRestTemplate;
    @Autowired
    private FinancialTransactionRestTemplate financialTransactionRestTemplate;
    @Autowired
    private RecordTransactionRestTemplate recordTransactionRestTemplate;

    @Autowired
    private CorrectionService correctionService;
    @Autowired
    private DataService dataService;

    public void runReportMaker() {
        System.out.println("Main started");
        LocalDate startDate = LocalDate.of(2015, 03, 01);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).minusDays(1);
        System.out.println("Using start date: " + startDate);
        System.out.println("Using end date: " + endDate);

        int returnCode;

        // downloading datas
        LocalDateTime stageStart = LocalDateTime.now();
        //returnCode = downloadReports(startDate, endDate);
        LocalDateTime stageFinish = LocalDateTime.now();
        reportDurationTime("Downloading", stageStart, stageFinish);

        // Пометить мастеров, стрижки
        stageStart = stageFinish;
        //returnCode = askForNewElementsMarks();
        stageFinish = LocalDateTime.now();
        reportDurationTime("Asking marks for new elements", stageStart, stageFinish);

        // Проставить все необходимые данные по посещениям, подкорректировать базу
        stageStart = stageFinish;
        //returnCode = prepareReportDatas();
        stageFinish = LocalDateTime.now();
        reportDurationTime("Report Data Preparation", stageStart, stageFinish);

        // Высчитываем и записываем в файлы по городам данные
        stageStart = stageFinish;
        returnCode = countDatas();
        stageFinish = LocalDateTime.now();
        reportDurationTime("Counting and writing data", stageStart, stageFinish);


        System.exit(0);
    }

    private int downloadReports(LocalDate startDate, LocalDate endDate) {
        staffRestTemplate.getStaffList();
        clientRestTemplate.getAllClients();
        goodsTransactionRestTemplate.getAllGoodsTransactions(startDate, endDate);
        financialTransactionRestTemplate.getAllFinancialTransactions(startDate, endDate);
        recordTransactionRestTemplate.getAllRecordTransactions(startDate, endDate);
        return 1;
    }

    private int askForNewElementsMarks() {
        try {
            correctionService.askStaffIsMaster();
            correctionService.askServiceTypeIsCut();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private int prepareReportDatas() {
        correctionService.makeCorrectionsToDb();
        return 1;
    }

    private int countDatas() {
        dataService.countAndWriteData();
        return 1;
    }

    private void reportDurationTime(String stage, LocalDateTime start, LocalDateTime finish) {
        System.out.println("Stage: " + stage);
        System.out.println("Starting time: " + start);
        System.out.println("Finish time: " + finish);
        long millis = Duration.between(start, finish).toMillis();
        System.out.printf("Total time estimated: %02d:%02d:%02d \n",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
