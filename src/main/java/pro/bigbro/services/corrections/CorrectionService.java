package pro.bigbro.services.corrections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.bigbro.jdbc.ClientJdbcTemplate;
import pro.bigbro.jdbc.RecordTransactionJdbcTemplate;
import pro.bigbro.jdbc.ServiceTypeJdbcTemplate;
import pro.bigbro.jdbc.StaffJdbcTemplate;
import pro.bigbro.models.jdbc.ServiceTypeJdbc;
import pro.bigbro.models.jdbc.StaffJdbc;
import pro.bigbro.models.records.RecordTransaction;
import pro.bigbro.repositories.ClientRepository;
import pro.bigbro.repositories.RecordTransactionRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

@Service
public class CorrectionService {

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Autowired
    private RecordTransactionJdbcTemplate recordTransactionJdbcTemplate;

    @Autowired
    private ClientJdbcTemplate clientJdbcTemplate;

    @Autowired
    private StaffJdbcTemplate staffJdbcTemplate;
    @Autowired
    private ServiceTypeJdbcTemplate serviceTypeJdbcTemplate;

    public void makeCorrectionsToDb() {
        System.out.println("Проставляем порядковые номера посещений");
        recordTransactionJdbcTemplate.updateVisitOrdinalNumber();

        System.out.println("Проставляем порядковые номера посещений для анонимов");
        recordTransactionJdbcTemplate.updateVisitAnonimNumber();

        System.out.println("Ставим метку, что клиент вернулся");
        recordTransactionJdbcTemplate.updateClientsWereReturned();

        System.out.println("Рассчитываем время возврата");
        recordTransactionJdbcTemplate.updateReturnTime();

        System.out.println("Ставим метку, есть ли у клиента ссылка на ВК");
        clientJdbcTemplate.updateClientHasLink();
    }

    public void askStaffIsMaster() throws IOException {
        List<StaffJdbc> staffJdbcList = staffJdbcTemplate.findAllStaffWithoutMarks();
        staffJdbcList.sort(Comparator.comparing(staffJdbc -> staffJdbc.getSpecialization()));
        for (StaffJdbc staffJdbc : staffJdbcList) {
            boolean isNoAnswer = true;
            while (isNoAnswer) {
                System.out.println("Этот сотрудник мастер? 1 - да, 0 - нет");
                System.out.println(staffJdbc.getSpecialization()
                        + " (" + staffJdbc.getName()
                        + ", " + staffJdbc.getCityId()
                        + ")");
                String answer = reader.readLine();
                if (answer.equals("1") || answer.equals("0")) {
                    staffJdbcTemplate.updateStaffMarkById(staffJdbc.getId(), answer);
                    isNoAnswer = false;
                }
            }
        }
    }


    public void askServiceTypeIsCut() throws IOException {
        List<ServiceTypeJdbc> serviceTypeJdbcList = serviceTypeJdbcTemplate.findAllServicesWithoutMarks();
        serviceTypeJdbcList.sort(Comparator.comparing(serviceTypeJdbc -> serviceTypeJdbc.getTitle()));
        for (ServiceTypeJdbc serviceTypeJdbc : serviceTypeJdbcList) {
            boolean isNoAnswer = true;
            while (isNoAnswer) {
                System.out.println("Эта услуга относится к стрижкам? 1 - да, 0 - нет");
                System.out.println(serviceTypeJdbc.getTitle() + " (" + serviceTypeJdbc.getServiceId() + ")");
                int answer = Integer.parseInt(reader.readLine());
                if (answer == 1 || answer == 0) {
                    serviceTypeJdbcTemplate.updateServicesMarksById(serviceTypeJdbc.getServiceId(), answer);
                    isNoAnswer = false;
                }
            }
        }
        reader.close();
    }
}
