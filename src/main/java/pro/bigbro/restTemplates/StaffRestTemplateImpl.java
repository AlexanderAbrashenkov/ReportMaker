package pro.bigbro.restTemplates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.bigbro.models.cities.City;
import pro.bigbro.models.masters.Staff;
import pro.bigbro.repositories.CityRepository;
import pro.bigbro.repositories.StaffRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class StaffRestTemplateImpl implements StaffRestTemplate {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<Staff> getStaffList() {
        System.out.println("Starting downloading masters\n");
        List<City> cityList = (List<City>) cityRepository.findAll();
        int i = 1;
        for (City city : cityList) {
            System.out.printf("[%d/%d] masters of %s\n", i, cityList.size(), city.getName());
            List<Staff> result = getStaffForCity(city.getId());
            for (Staff staff : result) {
                staffRepository.save(staff);
            }
            i++;
        }
        System.out.println();
        return null;
    }

    private List<Staff> getStaffForCity(int cityId) {
        ResponseEntity<Staff[]> responseEntity = restTemplate.getForEntity("http://api.yclients.com/api/v1/staff/{cityId}", Staff[].class, cityId);
        List<Staff> staffList = Arrays.asList(responseEntity.getBody());
        staffList.forEach(staff -> staff.setCityId(cityId));
        return staffList;
    }
}
