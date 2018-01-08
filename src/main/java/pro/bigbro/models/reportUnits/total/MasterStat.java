package pro.bigbro.models.reportUnits.total;

public class MasterStat {
    private int cityId;
    private String cityName;
    private int masterId;
    private String masterName;
    private double data;

    public MasterStat() {
    }

    public MasterStat(int cityId, String cityName, int masterId, String masterName, double data) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.masterId = masterId;
        this.masterName = masterName;
        this.data = data;
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

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
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
