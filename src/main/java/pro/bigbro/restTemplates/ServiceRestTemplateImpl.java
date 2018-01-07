package pro.bigbro.restTemplates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pro.bigbro.models.cities.City;
import pro.bigbro.models.masters.Staff;
import pro.bigbro.models.services.Service;
import pro.bigbro.repositories.CityRepository;
import pro.bigbro.repositories.ServiceRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class ServiceRestTemplateImpl implements ServiceRestTemplate {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<Service> getServiceList() {
        System.out.println("Starting downloading services\n");
        List<City> cityList = (List<City>) cityRepository.findAll();
        int i = 1;
        for (City city : cityList) {
            System.out.printf("[%d/%d] services of %s\n", i, cityList.size(), city.getName());
            List<Service> result = getServiceForCity(city.getId());
            for (Service service : result) {
                serviceRepository.save(service);
            }
            i++;
        }
        System.out.println();
        return null;
    }

    private List<Service> getServiceForCity(int cityId) {
        ResponseEntity<Service[]> responseEntity = restTemplate.getForEntity("http://api.yclients.com/api/v1/services/{cityId}", Service[].class, cityId);
        List<Service> serviceList = Arrays.asList(responseEntity.getBody());
        serviceList.forEach(service -> service.setCityId(cityId));
        return serviceList;
    }
}
