package pro.bigbro.models.reportUnits.total;

public class MasterSimpleStat {
    private String cityName;
    private String masterName;
    private double data;

    public MasterSimpleStat() {
    }

    public MasterSimpleStat(String masterName, double data) {
        this.masterName = masterName;
        this.data = data;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }
}
