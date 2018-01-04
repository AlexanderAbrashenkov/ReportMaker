package pro.bigbro.repositories;

import org.springframework.data.repository.CrudRepository;
import pro.bigbro.models.masters.Staff;

public interface StaffRepository extends CrudRepository<Staff, Long> {
}
