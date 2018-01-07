package pro.bigbro.models.reportUnits.cities;

import pro.bigbro.models.jdbc.StaffJdbc;

import java.util.List;

public class CityGeneral {
    private int cityId;
    private String cityName;
    private List<WorkingMonth> workingMonthList;
    private List<StaffJdbc> staffJdbcList;

    public CityGeneral() {
    }

    public CityGeneral(int cityId, String cityName, List<WorkingMonth> workingMonthList, List<StaffJdbc> staffJdbcList) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.workingMonthList = workingMonthList;
        this.staffJdbcList = staffJdbcList;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<WorkingMonth> getWorkingMonthList() {
        return workingMonthList;
    }

    public void setWorkingMonthList(List<WorkingMonth> workingMonthList) {
        this.workingMonthList = workingMonthList;
    }

    public List<StaffJdbc> getStaffJdbcList() {
        return staffJdbcList;
    }

    public void setStaffJdbcList(List<StaffJdbc> staffJdbcList) {
        this.staffJdbcList = staffJdbcList;
    }
}
