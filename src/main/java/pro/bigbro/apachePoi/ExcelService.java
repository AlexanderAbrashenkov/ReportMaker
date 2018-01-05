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
import java.util.List;
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
