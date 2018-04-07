package pro.bigbro.restTemplates;

import pro.bigbro.models.services.ServiceCategory;

import java.util.List;

public interface ServiceCategoryRestTemplate {
    List<ServiceCategory> getServiceCategoryList();
}
