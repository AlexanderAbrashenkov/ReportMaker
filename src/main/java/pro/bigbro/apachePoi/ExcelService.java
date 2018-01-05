package pro.bigbro.apachePoi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import pro.bigbro.models.jdbc.StaffJdbc;
import pro.bigbro.models.reportUnits.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExcelService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private XSSFRow row;
    private Cell cell;
    private int rowIndex;

    public void createWorkbook() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Data");
        rowIndex = 0;
    }


    public void writeGeneralInformation(CityGeneral cityGeneral) {
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue(cityGeneral.getCityName());
        cell = row.createCell(1);
        cell.setCellValue(cityGeneral.getCityId());
        rowIndex++;

        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue("Месяцев работает");
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue(cityGeneral.getWorkingMonthList().size());
        rowIndex++;

        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue("Список мастеров");
        for (StaffJdbc staffJdbc : cityGeneral.getStaffJdbcList()) {
            row = sheet.createRow(rowIndex++);
            cell = row.createCell(0);
            cell.setCellValue(staffJdbc.getName());
            cell = row.createCell(1);
            cell.setCellValue(staffJdbc.getId());
        }
        rowIndex++;
    }

    public void writeCityStatHeader(String title) {
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue(title);
    }

    public void writeMonthes(List<WorkingMonth> workingMonthList) {
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue("Категория");
        int cellIndex = 1;
        for (WorkingMonth workingMonth : workingMonthList) {
            cell = row.createCell(cellIndex++);
            cell.setCellValue(workingMonth.getMonthNum() + "." + workingMonth.getYearNum());
        }
    }

    public void writeCityClientsStat(List<ClientStat> clientStatList, List<WorkingMonth>workingMonthList, String category) {
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue(category);
        int cellIndex = 1;
        for (WorkingMonth workingMonth : workingMonthList) {
            int clients = clientStatList.stream()
                    .filter(clientStat -> clientStat.getYear() == workingMonth.getYearNum())
                    .filter(clientStat -> clientStat.getMonth() == workingMonth.getMonthNum())
                    .map(clientStat -> clientStat.getClientCount())
                    .findFirst()
                    .orElse(0);
            cell = row.createCell(cellIndex++);
            cell.setCellValue(clients);
        }
    }

    public void writeAverageClientVisit(List<AverageClientVisit> clientStatList, List<WorkingMonth>workingMonthList, String category) {
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue(category);
        int cellIndex = 1;
        for (WorkingMonth workingMonth : workingMonthList) {
            double averageVisit = clientStatList.stream()
                    .filter(clientStat -> clientStat.getYear() == workingMonth.getYearNum())
                    .filter(clientStat -> clientStat.getMonth() == workingMonth.getMonthNum())
                    .map(clientStat -> clientStat.getAverageVisit())
                    .findFirst()
                    .orElse(0.0d);
            cell = row.createCell(cellIndex++);
            cell.setCellValue(averageVisit);
        }
    }

    public void writeMonthesForConversion(List<WorkingMonth> workingMonthList) {
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue("Мастер");
        cell = row.createCell(1);
        cell.setCellValue("Период");
        int cellIndex = 2;
        for (WorkingMonth workingMonth : workingMonthList) {
            cell = row.createCell(cellIndex++);
            cell.setCellValue(workingMonth.getMonthNum() + "." + workingMonth.getYearNum());
        }
    }

    public void writeConversion(List<MasterConversionStat> masterConversionStatList, CityGeneral cityGeneral) {
        for (StaffJdbc staffJdbc : cityGeneral.getStaffJdbcList()) {
            filterAndWriteConversionRow(masterConversionStatList, cityGeneral.getWorkingMonthList(), staffJdbc.getName(), 0, "Всего клиентов");
            filterAndWriteConversionRow(masterConversionStatList, cityGeneral.getWorkingMonthList(), staffJdbc.getName(), 1, "Всего вернулось");
            filterAndWriteConversionRow(masterConversionStatList, cityGeneral.getWorkingMonthList(), staffJdbc.getName(), 14, "до 2 недель");
            filterAndWriteConversionRow(masterConversionStatList, cityGeneral.getWorkingMonthList(), staffJdbc.getName(), 30, "от 2 недель до 1 месяца");
            filterAndWriteConversionRow(masterConversionStatList, cityGeneral.getWorkingMonthList(), staffJdbc.getName(), 60, "от 1 до 2 месяцев");
            filterAndWriteConversionRow(masterConversionStatList, cityGeneral.getWorkingMonthList(), staffJdbc.getName(), 90, "от 2 до 3 месяцев");
            filterAndWriteConversionRow(masterConversionStatList, cityGeneral.getWorkingMonthList(), staffJdbc.getName(), 120, "от 3 до 4 месяцев");
            filterAndWriteConversionRow(masterConversionStatList, cityGeneral.getWorkingMonthList(), staffJdbc.getName(), 1000, "больше 4 месяцев");
        }
    }

    private void filterAndWriteConversionRow(List<MasterConversionStat> src, List<WorkingMonth> workingMonthList, String masterName, int type, String title) {
        List<MasterConversionStat> dataToWrite = src.stream()
                .filter(masterConversionStat -> masterConversionStat.getMasterName().equals(masterName))
                .filter(masterConversionStat -> masterConversionStat.getComebackTimeType() == type)
                .collect(Collectors.toList());
        writeConversionOneRow(dataToWrite, workingMonthList, masterName, title);
    }

    private void writeConversionOneRow(List<MasterConversionStat> masterConversionStatList, List<WorkingMonth> workingMonthList, String masterName, String category) {
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue(masterName);
        cell = row.createCell(1);
        cell.setCellValue(category);
        int cellIndex = 2;
        for (WorkingMonth workingMonth : workingMonthList) {
            int clients = masterConversionStatList.stream()
                    .filter(clientStat -> clientStat.getYear() == workingMonth.getYearNum())
                    .filter(clientStat -> clientStat.getMonth() == workingMonth.getMonthNum())
                    .map(clientStat -> clientStat.getClients())
                    .findFirst()
                    .orElse(0);
            cell = row.createCell(cellIndex++);
            cell.setCellValue(clients);
        }
    }

    public void writeFrequency(List<FrequencyStat> frequencyStatList, CityGeneral cityGeneral) {
        List<String> masters = cityGeneral.getStaffJdbcList().stream()
                .map(staffJdbc -> staffJdbc.getName())
                .distinct()
                .collect(Collectors.toList());
        for (String master : masters) {
            filterAndWriteFrequencyRow(frequencyStatList, cityGeneral.getWorkingMonthList(), master, 1, "вернулось К", 3, 1);
            filterAndWriteFrequencyRow(frequencyStatList, cityGeneral.getWorkingMonthList(), master, 1, "вернулось НК", 1, 1);
            filterAndWriteFrequencyRow(frequencyStatList, cityGeneral.getWorkingMonthList(), master, 1, "вернулось ПК", 2, 1);
            filterAndWriteFrequencyRow(frequencyStatList, cityGeneral.getWorkingMonthList(), master, 1, "время возврата К, дн", 3, 2);
            filterAndWriteFrequencyRow(frequencyStatList, cityGeneral.getWorkingMonthList(), master, 1, "время возврата НК, дн", 1, 2);
            filterAndWriteFrequencyRow(frequencyStatList, cityGeneral.getWorkingMonthList(), master, 1, "время возврата ПК, дн", 2, 2);

            filterAndWriteFrequencyRow(frequencyStatList, cityGeneral.getWorkingMonthList(), master, 2, "вернулось К позже 3 мес", 3, 1);
            filterAndWriteFrequencyRow(frequencyStatList, cityGeneral.getWorkingMonthList(), master, 2, "вернулось НК позже 3 мес", 1, 1);
            filterAndWriteFrequencyRow(frequencyStatList, cityGeneral.getWorkingMonthList(), master, 2, "вернулось ПК позже 3 мес", 2, 1);
            filterAndWriteFrequencyRow(frequencyStatList, cityGeneral.getWorkingMonthList(), master, 2, "время возврата К общ, дн", 3, 2);
            filterAndWriteFrequencyRow(frequencyStatList, cityGeneral.getWorkingMonthList(), master, 2, "время возврата НК общ, дн", 1, 2);
            filterAndWriteFrequencyRow(frequencyStatList, cityGeneral.getWorkingMonthList(), master, 2, "время возврата ПК общ, дн", 2, 2);
        }
    }

    private void filterAndWriteFrequencyRow(List<FrequencyStat> src, List<WorkingMonth> workingMonthList,
                                            String masterName, int type, String title, int clientType, int reportType) {
        List<FrequencyStat> dataToWrite = src.stream()
                .filter(frequencyStat -> frequencyStat.getMasterName().equals(masterName))
                .filter(frequencyStat -> frequencyStat.getComebackTimeType() == type)
                .filter(frequencyStat -> frequencyStat.getClientType() == clientType)
                .filter(frequencyStat -> frequencyStat.getReportType() == reportType)
                .collect(Collectors.toList());
        writeFrequencyOneRow(dataToWrite, workingMonthList, masterName, title);
    }

    private void writeFrequencyOneRow(List<FrequencyStat> dataToWrite, List<WorkingMonth> workingMonthList, String masterName, String title) {
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue(masterName);
        cell = row.createCell(1);
        cell.setCellValue(title);
        int cellIndex = 2;
        for (WorkingMonth workingMonth : workingMonthList) {
            double clientsOrTime = dataToWrite.stream()
                    .filter(clientStat -> clientStat.getYear() == workingMonth.getYearNum())
                    .filter(clientStat -> clientStat.getMonth() == workingMonth.getMonthNum())
                    .map(clientStat -> clientStat.getClientsOrTime())
                    .findFirst()
                    .orElse(0.0d);
            cell = row.createCell(cellIndex++);
            cell.setCellValue(clientsOrTime);
        }
    }

    public void writeDaysForSpreading() {
        row = sheet.createRow(rowIndex++);
        XSSFRow row2 = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue("Месяц");
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 8; j++) {
                cell = row.createCell(7 * (i - 1) + j);
                cell.setCellValue(i);
                cell = row2.createCell(7 * (i - 1) + j);
                cell.setCellValue(j);
            }
        }
    }

    public void writeSpreadingStat(List<SpreadingStat> spreadingStatList, CityGeneral cityGeneral) {
        for (WorkingMonth workingMonth : cityGeneral.getWorkingMonthList()) {
            List<SpreadingStat> dataToWrite = spreadingStatList.stream()
                    .filter(spreadingStat -> spreadingStat.getMon() == workingMonth.getMonthNum())
                    .filter(spreadingStat -> spreadingStat.getYear() == workingMonth.getYearNum())
                    .collect(Collectors.toList());
            writeSpreadingStatOneRow(dataToWrite, workingMonth.getYearNum(), workingMonth.getMonthNum());
        }
    }

    private void writeSpreadingStatOneRow(List<SpreadingStat> dataToWrite, int year, int month) {
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue(month + "." + year);
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 8; j++) {
                int week = i;
                int dow = j;
                cell = row.createCell(7 * (i - 1) + j);
                int clients = dataToWrite.stream()
                        .filter(spreadingStat -> spreadingStat.getWeek() == week)
                        .filter(spreadingStat -> spreadingStat.getDayOfWeek() == dow)
                        .map(spreadingStat -> spreadingStat.getClients())
                        .findFirst()
                        .orElse(0);
                cell.setCellValue(clients);
            }
        }
    }

    public void writeSpreadingDaysCount(List<SpreadingDaysCount> spreadingDaysCounts) {
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue("Кол-во дней");
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 8; j++) {
                int week = i;
                int dow = j;
                cell = row.createCell(7 * (i - 1) + j);
                int clients = spreadingDaysCounts.stream()
                        .filter(spreadingStat -> spreadingStat.getWeek() == week)
                        .filter(spreadingStat -> spreadingStat.getDayOfWeek() == dow)
                        .map(spreadingStat -> spreadingStat.getDaysCount())
                        .findFirst()
                        .orElse(0);
                cell.setCellValue(clients);
            }
        }
    }

    public void writeGoodsStat(List<GoodsStat> goodsStatList, CityGeneral cityGeneral) {
        Map<Integer, String> goodsMap = goodsStatList.stream()
                .collect(Collectors.toMap(GoodsStat::getGoodId, GoodsStat::getGoodTitle, (s, s2) -> s));

        for (Map.Entry<Integer, String> pair : goodsMap.entrySet()) {
            List<GoodsStat> dataToWrite = goodsStatList.stream()
                    .filter(goodsStat -> goodsStat.getGoodId() == pair.getKey())
                    .collect(Collectors.toList());
            writeGoodsStatOneRow(dataToWrite, cityGeneral.getWorkingMonthList(), pair.getValue());
        }
    }

    private void writeGoodsStatOneRow(List<GoodsStat> dataToWrite, List<WorkingMonth> workingMonthList, String goodTitle) {
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue(goodTitle);
        int cellIndex = 1;
        for (WorkingMonth workingMonth : workingMonthList) {
            double sales = dataToWrite.stream()
                    .filter(clientStat -> clientStat.getYear() == workingMonth.getYearNum())
                    .filter(clientStat -> clientStat.getMon() == workingMonth.getMonthNum())
                    .map(clientStat -> clientStat.getSales())
                    .findFirst()
                    .orElse(0.0d);
            cell = row.createCell(cellIndex++);
            cell.setCellValue(sales);
        }
    }

    public void writeMonthesForGoodsByMasters(List<WorkingMonth> workingMonthList) {
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue("Мастер");
        int cellIndex = 1;
        for (WorkingMonth workingMonth : workingMonthList) {
            cell = row.createCell(cellIndex++);
            cell.setCellValue(workingMonth.getMonthNum() + "." + workingMonth.getYearNum());
            cell = row.createCell(cellIndex++);
            cell.setCellValue(workingMonth.getMonthNum() + "." + workingMonth.getYearNum());
        }
    }

    public void writeGoodsByMastersStat(List<GoodsMasterStat> goodsMasterStatList, CityGeneral cityGeneral) {
        Map<Long, String> mastersMap = cityGeneral.getStaffJdbcList().stream()
                .collect(Collectors.toMap(StaffJdbc::getId, StaffJdbc::getName, (s, s2) -> s));
        mastersMap = mastersMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (s, s2) -> s,
                        LinkedHashMap::new
                ));

        for (Map.Entry<Long, String> pair : mastersMap.entrySet()) {
            List<GoodsMasterStat> dataToWrite = goodsMasterStatList.stream()
                    .filter(goodsStat -> goodsStat.getMasterId() == pair.getKey())
                    .collect(Collectors.toList());
            writeGoodsByMastersStatOneRow(dataToWrite, cityGeneral.getWorkingMonthList(), pair.getValue());
        }
    }

    private void writeGoodsByMastersStatOneRow(List<GoodsMasterStat> dataToWrite, List<WorkingMonth> workingMonthList, String masterName) {
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue(masterName);
        int cellIndex = 1;
        for (WorkingMonth workingMonth : workingMonthList) {
            double sales = dataToWrite.stream()
                    .filter(clientStat -> clientStat.getYear() == workingMonth.getYearNum())
                    .filter(clientStat -> clientStat.getMonth() == workingMonth.getMonthNum())
                    .map(clientStat -> clientStat.getSales())
                    .findFirst()
                    .orElse(0.0d);
            double spc = dataToWrite.stream()
                    .filter(goodsMasterStat -> goodsMasterStat.getYear() == workingMonth.getYearNum())
                    .filter(goodsMasterStat -> goodsMasterStat.getMonth() == workingMonth.getMonthNum())
                    .map(goodsMasterStat -> goodsMasterStat.getSalesPerClient())
                    .findFirst()
                    .orElse(0.0d);
            cell = row.createCell(cellIndex++);
            cell.setCellValue(sales);
            cell = row.createCell(cellIndex++);
            cell.setCellValue(spc);
        }
    }

    public void addEmptyRow() {
        rowIndex++;
    }

    public void saveWorkbook(String name) {
        File directory = new File("C:\\Подработка\\Данные");
        if (!directory.exists())
            directory.mkdirs();
        File file = new File("C:\\Подработка\\Данные\\" + name + "-данные.xlsx");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
