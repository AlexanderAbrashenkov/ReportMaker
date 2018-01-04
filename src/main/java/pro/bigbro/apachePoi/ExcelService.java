package pro.bigbro.apachePoi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import pro.bigbro.models.jdbc.StaffJdbc;
import pro.bigbro.models.reportUnits.CityGeneral;
import pro.bigbro.models.reportUnits.ClientStatForCity;
import pro.bigbro.models.reportUnits.WorkingMonth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

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
            if (staffJdbc.getName() != null) {
                cell.setCellValue(staffJdbc.getName());
            } else {
                cell.setCellValue("Удаленные");
            }
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

    public void writeCityClientsStat(List<ClientStatForCity> clientStatList, List<WorkingMonth>workingMonthList, String category) {
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellValue(category);
        int cellIndex = 1;
        for (WorkingMonth workingMonth : workingMonthList) {
            int clients = clientStatList.stream()
                    .filter(clientStatForCity -> clientStatForCity.getYear() == workingMonth.getYearNum())
                    .filter(clientStatForCity -> clientStatForCity.getMonth() == workingMonth.getMonthNum())
                    .map(clientStatForCity -> clientStatForCity.getClientCount())
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
