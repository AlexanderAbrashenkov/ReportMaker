package pro.bigbro.services.counting;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pro.bigbro.models.cities.City;
import pro.bigbro.models.reportUnits.avglength.LengthReport;
import pro.bigbro.models.reportUnits.total.DataTotal;
import pro.bigbro.repositories.LengthReportRepository;
import pro.bigbro.selenium.Selenium;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component @Lazy
public class SeleniumService {

    @Autowired
    private LengthReportRepository lengthReportRepository;

    public int downloadLengthData(List<City> cityList, int year, int month) throws IOException, InterruptedException {
        Selenium selenium = new Selenium();

        selenium.startChromeDriver();

        WebDriver driver = selenium.getDriver();

        String smenaSchema = "https://yclients.com/analytics_workload/%d?start_date=%s&end_date=%s"; //возвр   раб.ч / отраб. дней
        String servSchema = "https://yclients.com/analytics_masters/%d?start_date=%s&end_date=%s"; // сотр   отр.ч / кол-во усл
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, 1)
                .plusMonths(1)
                .minusDays(1);

        for (City city : cityList) {
            int id = city.getId();
            String name = city.getName();
            LengthReport lengthReport = new LengthReport(month, year, id, name);

            String fullLinkSmena = String.format(smenaSchema, id, startDate.format(formatter), endDate.format(formatter));

            driver.get(fullLinkSmena);
            Thread.sleep(500);
            selenium.waitForJSandJQueryToLoad();

            Document document = Jsoup.parse(driver.getPageSource());

            double workingHour = 0d, workingDay = 0d;

            Element summaryElem = document.getElementsContainingOwnText("Всего")
                    .last();
            if (summaryElem != null) {
                String workingHourS = summaryElem.parent()
                        .getElementsByTag("th")
                        .get(8)
                        .text();
                workingHour = Double.parseDouble(workingHourS);

                String workingDayS = summaryElem.parent()
                        .getElementsByTag("th")
                        .get(7)
                        .text();
                workingDay = Double.parseDouble(workingDayS);
            }
            double avgSmena = workingHour / workingDay;

            lengthReport.setAvgSmena(avgSmena);

            String fullLinkServ = String.format(servSchema, id, startDate.format(formatter), endDate.format(formatter));

            driver.get(fullLinkServ);
            Thread.sleep(500);
            selenium.waitForJSandJQueryToLoad();

            document = Jsoup.parse(driver.getPageSource());

            double servCount = 0d, servHour = 0d;

            summaryElem = document.getElementsContainingOwnText("Всего")
                    .last();
            if (summaryElem != null) {
                String servCountS = summaryElem.parent()
                        .getElementsByTag("th")
                        .get(4)
                        .text();
                servCount = Double.parseDouble(servCountS);

                String servHourS = summaryElem.parent()
                        .getElementsByTag("th")
                        .get(8)
                        .text();
                servHour = Double.parseDouble(servHourS);
            }
            double avgServ = servHour / servCount;

            lengthReport.setAvgServ(avgServ);
            lengthReportRepository.save(lengthReport);
        }

        selenium.quitChromeDriver();
        return 1;
    }
}
