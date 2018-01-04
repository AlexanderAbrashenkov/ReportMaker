package pro.bigbro.repositories;

import org.springframework.data.repository.CrudRepository;
import pro.bigbro.models.cities.City;

public interface CityRepository extends CrudRepository<City, Integer> {
}
