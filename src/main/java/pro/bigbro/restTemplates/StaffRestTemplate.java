package pro.bigbro.restTemplates;

import pro.bigbro.models.masters.Staff;

import java.util.List;

public interface StaffRestTemplate {
    List<Staff> getStaffList();
}
