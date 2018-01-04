package pro.bigbro.services.counting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.bigbro.jdbc.StaffJdbcTemplate;
import pro.bigbro.jdbc.WorkingMonthesJdbcTemplate;
import pro.bigbro.models.cities.City;
import pro.bigbro.models.jdbc.StaffJdbc;
import pro.bigbro.models.reportUnits.CityGeneral;
import pro.bigbro.models.reportUnits.WorkingMonth;

import java.util.List;

@Service
public class GeneralInformationService {

    @Autowired
    private WorkingMonthesJdbcTemplate workingMonthesJdbcTemplate;
    @Autowired
    private StaffJdbcTemplate staffJdbcTemplate;

    public CityGeneral getGeneralInformationForCity(City city) {
        List<WorkingMonth> workingMonthList = workingMonthesJdbcTemplate.findAllWorkingPeriodsByCity(city.getId());
        List<StaffJdbc> workingStaffList = staffJdbcTemplate.findWorkingStaff(city.getId());
        return new CityGeneral(city.getId(), city.getName(), workingMonthList, workingStaffList);
    }
}
