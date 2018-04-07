package pro.bigbro.services.corrections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.bigbro.jdbc.cities.ClientJdbcTemplate;
import pro.bigbro.jdbc.cities.RecordTransactionJdbcTemplate;
import pro.bigbro.jdbc.cities.ServiceTypeJdbcTemplate;
import pro.bigbro.jdbc.cities.StaffJdbcTemplate;
import pro.bigbro.models.jdbc.ServiceTypeJdbc;
import pro.bigbro.models.jdbc.StaffJdbc;
import pro.bigbro.models.services.ServiceLib;
import pro.bigbro.repositories.ServiceLibRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private ServiceLibRepository serviceLibRepository;

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

        staffJdbcList.forEach(staffJdbc -> {
            String spec = staffJdbc.getSpecialization().toLowerCase();
            if (spec.contains("админ")
                    || spec.contains("главный")
                    || spec.contains("директор")
                    || spec.contains("управляющий")
                    || spec.contains("руководитель")
                    || spec.contains("управление")) {
                staffJdbcTemplate.updateStaffMarkById(staffJdbc.getId(), "0");
                staffJdbc.setUseInRecords("0");
            } else if (spec.contains("барбер")
                    || spec.contains("мастер")
                    || spec.contains("стажер")
                    || spec.contains("стрижет")
                    || spec.contains("парикмахер")
                    || spec.contains("ученик")) {
                staffJdbcTemplate.updateStaffMarkById(staffJdbc.getId(), "1");
                staffJdbc.setUseInRecords("1");
            }
        });

        staffJdbcList = staffJdbcList.stream()
                .filter(staffJdbc -> staffJdbc.getUseInRecords() == null)
                .collect(Collectors.toList());

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

        List<ServiceLib> serviceLibList = (List<ServiceLib>) serviceLibRepository.findAll();
        List<Long> notCutIds = serviceLibList.stream()
                .filter(serviceLib -> serviceLib.getCutType() == 0)
                .map(serviceLib -> serviceLib.getServiceId())
                .collect(Collectors.toList());

        List<Long> cutIds = serviceLibList.stream()
                .filter(serviceLib -> serviceLib.getCutType() == 1)
                .map(serviceLib -> serviceLib.getServiceId())
                .collect(Collectors.toList());

        serviceTypeJdbcList.removeIf(serviceTypeJdbc -> notCutIds.contains(serviceTypeJdbc.getServiceId()));

        serviceTypeJdbcList.forEach(serviceTypeJdbc -> {
            if (cutIds.contains(serviceTypeJdbc.getServiceId())) {
                serviceTypeJdbcTemplate.updateServicesMarksById(serviceTypeJdbc.getServiceId(), 1);
            }
        });

        serviceTypeJdbcList.removeIf(serviceTypeJdbc -> cutIds.contains(serviceTypeJdbc.getServiceId()));

        for (ServiceTypeJdbc serviceTypeJdbc : serviceTypeJdbcList) {
            boolean isNoAnswer = true;
            while (isNoAnswer) {
                System.out.println("Эта услуга относится к стрижкам? 1 - да, 0 - нет");
                System.out.println(serviceTypeJdbc.getTitle() + " (" + serviceTypeJdbc.getServiceId() + ")");
                String ans = reader.readLine();
                if (!ans.equals("0") && !ans.equals("1")) {
                    continue;
                }
                int answer = Integer.parseInt(ans);
                serviceTypeJdbcTemplate.updateServicesMarksById(serviceTypeJdbc.getServiceId(), answer);
                serviceLibRepository.save(new ServiceLib(serviceTypeJdbc.getServiceId(), answer));
                isNoAnswer = false;
            }
        }
    }

    public void askServiceGroupes() throws IOException {
        System.out.println("Пожалуйста, заполните таблицу с распределением товаров по группам");
        System.out.println("SQL скрипты находятся в resources/sql/services.sql");
        System.out.println("По завершении введите слово 'done'");
        while (!reader.readLine().equals("done")) {
            System.out.println("'done' для завершения");
        }
    }

    public void askServiceCategiryIsMaster() throws IOException {
        System.out.println("Пожалуйста, пометьте услуги с категорией 'мастер' для определения цены на услуги");
        System.out.println("Внести коррективы надо в таблицу service_category в столбец master_category");
        System.out.println("По завершении введите слово 'done'");
        while (!reader.readLine().equals("done")) {
            System.out.println("'done' для завершения");
        }
        reader.close();
    }
}
