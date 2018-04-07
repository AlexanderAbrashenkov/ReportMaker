package pro.bigbro.restTemplates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pro.bigbro.models.cities.City;
import pro.bigbro.models.services.Service;
import pro.bigbro.models.services.ServiceCategory;
import pro.bigbro.repositories.CityRepository;
import pro.bigbro.repositories.ServiceCategoryRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class ServiceCategoryRestTemplateImpl implements ServiceCategoryRestTemplate {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;
    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<ServiceCategory> getServiceCategoryList() {
        System.out.println("Starting downloading service categories\n");
        List<City> cityList = (List<City>) cityRepository.findAll();
        int i = 1;
        for (City city : cityList) {
            System.out.printf("[%d/%d] service categories of %s\n", i, cityList.size(), city.getName());
            List<ServiceCategory> result = getServiceCategoriesForCity(city.getId());
            for (ServiceCategory serviceCategory : result) {
                serviceCategoryRepository.save(serviceCategory);
            }
            i++;
        }
        System.out.println();
        return null;
    }

    private List<ServiceCategory> getServiceCategoriesForCity(int cityId) {
        ResponseEntity<ServiceCategory[]> responseEntity = restTemplate.getForEntity("http://api.yclients.com/api/v1/service_categories/{cityId}", ServiceCategory[].class, cityId);
        List<ServiceCategory> serviceCategoryList = Arrays.asList(responseEntity.getBody());
        serviceCategoryList.forEach(category -> category.setCityId(cityId));
        return serviceCategoryList;
    }
}
