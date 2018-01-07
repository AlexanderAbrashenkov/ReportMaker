package pro.bigbro.restTemplates;

import pro.bigbro.models.services.Service;

import java.util.List;

public interface ServiceRestTemplate {
    List<Service> getServiceList();
}
