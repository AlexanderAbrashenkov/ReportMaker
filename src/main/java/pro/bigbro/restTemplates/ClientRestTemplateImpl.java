package pro.bigbro.restTemplates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pro.bigbro.models.cities.City;
import pro.bigbro.models.clients.Client;
import pro.bigbro.models.clients.ClientContainer;
import pro.bigbro.repositories.CityRepository;
import pro.bigbro.repositories.ClientRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ClientRestTemplateImpl implements ClientRestTemplate {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<Client> getAllClients() {
        System.out.println("Starting downloading clients\n");
        List<City> cityList = (List<City>) cityRepository.findAll();
        int i = 1;
        for (City city : cityList) {
            System.out.printf("[%d/%d] downloading clients of %s\n", i, cityList.size(), city.getName());
            List<Client> result = getClientsForCity(city.getId());
            for (Client client : result) {
                clientRepository.save(client);
            }
            i++;
        }
        System.out.println();
        return null;
    }

    private List<Client> getClientsForCity(int cityId) {
        ResponseEntity<ClientContainer> responseEntity = restTemplate.getForEntity("http://api.yclients.com/api/v1/clients/{cityId}?page=1&count=1", ClientContainer.class, cityId);
        ClientContainer clientContainer = responseEntity.getBody();
        int clientsCount = clientContainer.getCount();
        int pages = clientsCount / 300
                + (clientsCount % 300 == 0 ? 0 : 1);
        List<Client> result = new ArrayList<>();
        for (int i = 1; i <= pages; i++) {
            ResponseEntity<ClientContainer> responseEntityPage = restTemplate.getForEntity("http://api.yclients.com/api/v1/clients/{cityId}?page={i}&count=300", ClientContainer.class, cityId, i);
            ClientContainer clientContainerPage = responseEntityPage.getBody();
            result.addAll(Arrays.asList(clientContainerPage.getData()));
        }
        result.forEach(client -> client.setCityId(cityId));
        return result;
    }
}
